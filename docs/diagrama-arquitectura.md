# Diagrama de Arquitectura del Sistema

## Diagrama de Componentes

```
┌─────────────────────────────────────────────────────────────────┐
│                         CLIENTE                                  │
│                    (Postman / Frontend)                          │
└────────────────────────────┬────────────────────────────────────┘
                             │
                             │ HTTP/REST (JSON)
                             │
                             ▼
┌─────────────────────────────────────────────────────────────────┐
│                  MICROSERVICIO BFF                               │
│                   (Spring Boot)                                  │
│                   Puerto: 8080                                   │
│                                                                  │
│  - UsuarioController                                            │
│  - PrestamoController                                           │
│  - UsuarioService (orquestación)                                │
│  - PrestamoService (orquestación)                               │
│  - ServerlessClient (comunicación HTTP)                         │
└──────────────┬─────────────────────────────┬────────────────────┘
               │                             │
               │ HTTP/REST                   │ HTTP/REST
               │                             │
               ▼                             ▼
┌──────────────────────────┐  ┌──────────────────────────────────┐
│ FUNCIÓN SERVERLESS       │  │ FUNCIÓN SERVERLESS               │
│ Gestión de Usuarios      │  │ Gestión de Préstamos             │
│ (Java + Spark)           │  │ (Java + Spark)                   │
│ Puerto: 8081             │  │ Puerto: 8082                     │
│                          │  │                                  │
│ - UsuariosHandler        │  │ - PrestamosHandler               │
│ - UsuarioDAO             │  │ - PrestamoDAO                    │
│ - Validaciones           │  │ - Validaciones de disponibilidad │
│ - HikariCP Pool          │  │ - HikariCP Pool                  │
└────────────┬─────────────┘  └────────────┬─────────────────────┘
             │                              │
             │ JDBC                         │ JDBC
             │                              │
             └──────────────┬───────────────┘
                            │
                            ▼
            ┌───────────────────────────────┐
            │    ORACLE DATABASE            │
            │    Puerto: 1521               │
            │                               │
            │  Tablas:                      │
            │  - USUARIOS                   │
            │  - LIBROS                     │
            │  - PRESTAMOS                  │
            │                               │
            │  Features:                    │
            │  - Secuencias                 │
            │  - Triggers                   │
            │  - Foreign Keys               │
            │  - Índices                    │
            └───────────────────────────────┘
```

## Diagrama de Despliegue Docker

```
┌─────────────────────────────────────────────────────────────────┐
│                     DOCKER COMPOSE                               │
│                                                                  │
│  ┌────────────────────────────────────────────────────────────┐ │
│  │                  biblioteca-network                         │ │
│  │                                                              │ │
│  │  ┌─────────────────┐  ┌─────────────────┐  ┌────────────┐ │ │
│  │  │ Container       │  │ Container       │  │ Container  │ │ │
│  │  │ microservicio-  │  │ funcion-        │  │ funcion-   │ │ │
│  │  │ bff             │  │ usuarios        │  │ prestamos  │ │ │
│  │  │ :8080           │  │ :8081           │  │ :8082      │ │ │
│  │  └────────┬────────┘  └────────┬────────┘  └──────┬─────┘ │ │
│  │           │                    │                   │        │ │
│  │           └────────────────────┴───────────────────┘        │ │
│  │                              │                               │ │
│  │                              ▼                               │ │
│  │                    ┌──────────────────┐                     │ │
│  │                    │ Container        │                     │ │
│  │                    │ oracle-db        │                     │ │
│  │                    │ :1521            │                     │ │
│  │                    │                  │                     │ │
│  │                    │ Volume:          │                     │ │
│  │                    │ oracle-data      │                     │ │
│  │                    └──────────────────┘                     │ │
│  │                                                              │ │
│  └──────────────────────────────────────────────────────────────┘ │
│                                                                  │
│  Puertos Expuestos al Host:                                     │
│  - 8080 → Microservicio BFF                                     │
│  - 8081 → Función Usuarios                                      │
│  - 8082 → Función Préstamos                                     │
│  - 1521 → Oracle Database                                       │
│  - 5500 → Oracle EM Express                                     │
└─────────────────────────────────────────────────────────────────┘
```

## Flujo de Datos Detallado

### Caso de Uso: Registrar Préstamo de Libro

1. **Cliente envía request al BFF**
   ```
   POST /api/prestamos
   {
     "idUsuario": 1,
     "idLibro": 5,
     "fechaDevolucionEsperada": "2026-04-15"
   }
   ```

2. **BFF valida el request**
   - Valida campos requeridos con Bean Validation
   - Si falla, retorna 400 Bad Request

3. **BFF delega a Función Préstamos**
   - PrestamoService construye request HTTP
   - ServerlessClient ejecuta POST a función serverless

4. **Función Préstamos procesa**
   - PrestamosHandler recibe request
   - Valida datos del préstamo
   - PrestamoDAO verifica disponibilidad del libro
   - Si no hay stock, retorna error 400
   - Si hay stock, inserta registro en tabla prestamos

5. **Trigger de BD se ejecuta**
   - Oracle ejecuta trigger trg_prestamo_insert
   - Decrementa cantidad_disponible del libro

6. **Respuesta regresa al BFF**
   - Función serverless retorna JSON con préstamo creado
   - BFF recibe response y extrae data

7. **BFF responde al cliente**
   ```json
   {
     "success": true,
     "data": {
       "id": 8,
       "idUsuario": 1,
       "idLibro": 5,
       "fechaPrestamo": "2026-03-28T14:30:00",
       "fechaDevolucionEsperada": "2026-04-15",
       "fechaDevolucionReal": null,
       "estado": "PRESTADO"
     },
     "message": "Préstamo registrado exitosamente"
   }
   ```

## Modelo de Datos

```
┌─────────────────┐         ┌─────────────────┐         ┌─────────────────┐
│    USUARIOS     │         │    PRESTAMOS    │         │     LIBROS      │
├─────────────────┤         ├─────────────────┤         ├─────────────────┤
│ id (PK)         │────┐    │ id (PK)         │    ┌────│ id (PK)         │
│ nombre          │    │    │ id_usuario (FK) │────┘    │ titulo          │
│ apellido        │    └────│ id_libro (FK)   │         │ autor           │
│ email (UNIQUE)  │         │ fecha_prestamo  │         │ isbn (UNIQUE)   │
│ rut (UNIQUE)    │         │ fecha_dev_esp   │         │ categoria       │
│ telefono        │         │ fecha_dev_real  │         │ cantidad_disp   │
│ fecha_registro  │         │ estado          │         │ cantidad_total  │
│ estado          │         └─────────────────┘         └─────────────────┘
└─────────────────┘
```

### Relaciones

- Un **usuario** puede tener múltiples **préstamos** (1:N)
- Un **libro** puede tener múltiples **préstamos** (1:N)
- Un **préstamo** pertenece a un **usuario** y un **libro** (N:1)

### Estados

**Usuario.estado**:
- ACTIVO: Usuario puede realizar préstamos
- INACTIVO: Usuario deshabilitado temporalmente
- SUSPENDIDO: Usuario con sanciones

**Prestamo.estado**:
- PRESTADO: Libro actualmente prestado
- DEVUELTO: Libro devuelto
- RETRASADO: Fecha de devolución esperada superada

## Consideraciones de Red

### Docker Compose (Desarrollo Local)
- Red bridge interna: `biblioteca-network`
- Comunicación entre servicios por nombre de servicio
- Puertos expuestos al host para pruebas

### Docker Lab (Producción)
- Cada contenedor obtiene URL pública
- Configurar variables de entorno con URLs públicas
- Considerar latencia entre servicios

## Resiliencia

### Estrategias Implementadas

**Timeouts**:
- Conexión: 10 segundos
- Lectura: 30 segundos

**Connection Pooling**:
- Max Pool Size: 10 conexiones
- Min Idle: 2 conexiones
- Connection Timeout: 30 segundos

**Manejo de Errores**:
- Try-catch en todos los handlers
- Exceptions personalizadas
- Logs detallados para debugging

### Mejoras Futuras

- Retry con backoff exponencial
- Circuit breaker
- Bulkhead pattern
- Fallback responses
- Dead letter queue para operaciones fallidas

## Performance

### Optimizaciones Aplicadas

**Base de Datos**:
- Índices en columnas de búsqueda frecuente
- Prepared statements (previene recompilación)
- Connection pooling

**Aplicación**:
- Multi-stage builds en Docker (imágenes más pequeñas)
- JRE Alpine (menor tamaño, arranque más rápido)
- RestTemplate reutilizable con pooling HTTP

### Métricas Esperadas

- Latencia promedio BFF: < 100ms (sin cold start)
- Latencia funciones serverless: < 50ms (sin cold start)
- Cold start: 2-5 segundos primera invocación
- Throughput: ~100 requests/segundo por función

## Conclusión

Esta arquitectura proporciona un sistema escalable, mantenible y alineado con las mejores prácticas de desarrollo serverless. La separación clara de responsabilidades facilita el testing, despliegue y evolución futura del sistema.
