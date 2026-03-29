# Arquitectura del Sistema de Biblioteca Serverless

## Visión General

El Sistema de Biblioteca implementa una arquitectura serverless basada en microservicios y funciones serverless (FaaS) para gestionar usuarios y préstamos de libros. La arquitectura sigue el patrón BFF (Backend For Frontend) donde un microservicio orquestador coordina las llamadas a funciones serverless especializadas.

## Componentes del Sistema

### 1. Microservicio BFF (Backend For Frontend)

**Responsabilidad**: Punto de entrada único que orquesta las operaciones del sistema.

**Tecnología**: Spring Boot 3.2.3

**Características**:
- Expone APIs REST para todas las operaciones
- Valida requests entrantes con Bean Validation
- Delega operaciones CRUD a funciones serverless
- Maneja errores de forma centralizada
- Proporciona respuestas estandarizadas en JSON

**Decisiones de Diseño**:
- Se eligió Spring Boot por su ecosistema maduro y soporte empresarial
- RestTemplate para comunicación HTTP síncrona con funciones serverless
- Timeouts configurados (10s conexión, 30s lectura) para evitar bloqueos
- Patrón de respuesta unificado para consistencia en todas las APIs

### 2. Función Serverless - Gestión de Usuarios

**Responsabilidad**: Ejecutar operaciones CRUD sobre la entidad Usuario.

**Tecnología**: Java 17 con Spark Framework

**Características**:
- Endpoints HTTP independientes
- Conexión directa a Oracle Database
- HikariCP para pooling de conexiones
- Validación de datos antes de persistir
- Manejo de excepciones SQL

**Decisiones de Diseño**:
- Spark Framework por su simplicidad y bajo overhead para FaaS
- HikariCP para optimizar el uso de conexiones a BD
- Validaciones en el handler antes de delegar al DAO
- Respuestas consistentes con el formato ApiResponse

### 3. Función Serverless - Gestión de Préstamos

**Responsabilidad**: Ejecutar operaciones CRUD sobre préstamos y gestionar lógica de negocio relacionada.

**Tecnología**: Java 17 con Spark Framework

**Características**:
- Endpoints HTTP independientes
- Conexión directa a Oracle Database
- Validación de disponibilidad de libros
- Actualización automática de cantidades disponibles
- Cálculo de estados (prestado, devuelto, retrasado)

**Decisiones de Diseño**:
- Lógica de negocio en la función para validar disponibilidad antes de crear préstamo
- Triggers de BD para mantener consistencia en cantidades
- Endpoint especializado `/devolver` para simplificar devoluciones
- Consultas optimizadas con índices en columnas frecuentemente buscadas

### 4. Base de Datos Oracle

**Responsabilidad**: Persistencia de datos del sistema.

**Tecnología**: Oracle Database 21c Express Edition

**Esquema**:
- **usuarios**: Información de los usuarios de la biblioteca
- **libros**: Catálogo de libros con inventario
- **prestamos**: Registro histórico y actual de préstamos

**Decisiones de Diseño**:
- Secuencias para IDs autoincrementales
- Foreign Keys con CASCADE DELETE para integridad referencial
- Triggers para automatizar actualizaciones de cantidades
- Trigger para marcar préstamos retrasados automáticamente
- Índices en columnas de búsqueda frecuente (email, rut, isbn, estado)
- Constraints CHECK para validar estados y cantidades

## Flujo de Comunicación

### Flujo Típico: Crear Préstamo

```
┌────────┐      ┌─────────┐      ┌──────────────────┐      ┌──────────┐
│Cliente │      │   BFF   │      │Función Préstamos │      │  Oracle  │
└───┬────┘      └────┬────┘      └────────┬─────────┘      └────┬─────┘
    │                │                     │                     │
    │ POST /api/prestamos                 │                     │
    ├───────────────>│                     │                     │
    │                │                     │                     │
    │                │ POST /prestamos     │                     │
    │                ├────────────────────>│                     │
    │                │                     │                     │
    │                │                     │ Verificar disponibilidad
    │                │                     ├────────────────────>│
    │                │                     │<────────────────────┤
    │                │                     │                     │
    │                │                     │ INSERT prestamo     │
    │                │                     ├────────────────────>│
    │                │                     │<────────────────────┤
    │                │                     │                     │
    │                │                     │ (Trigger actualiza cantidad)
    │                │                     │                     │
    │                │ JSON response       │                     │
    │                │<────────────────────┤                     │
    │                │                     │                     │
    │ JSON response  │                     │                     │
    │<───────────────┤                     │                     │
    │                │                     │                     │
```

## Patrones y Principios Aplicados

### 1. Backend For Frontend (BFF)
El BFF actúa como agregador y orquestador, simplificando la interacción con múltiples funciones serverless.

### 2. Function as a Service (FaaS)
Las funciones serverless son independientes, escalables y ejecutan tareas específicas sin mantener estado.

### 3. Separation of Concerns
Cada componente tiene una responsabilidad clara y bien definida.

### 4. API Gateway Pattern
El BFF funciona como un API Gateway ligero que centraliza el acceso al sistema.

### 5. Dependency Injection
Spring Boot en el BFF utiliza DI para gestionar dependencias de forma desacoplada.

## Decisiones Técnicas

### ¿Por qué Funciones Serverless en lugar de Microservicios Tradicionales?

**Ventajas**:
- Escalabilidad automática basada en demanda
- Menor overhead operacional
- Costos basados en uso real
- Despliegue independiente
- Actualizaciones sin downtime

**Trade-offs**:
- Cold start en primera invocación
- Limitaciones de tiempo de ejecución
- Requiere gestión de conexiones a BD eficiente

### ¿Por qué Spring Boot para el BFF?

**Ventajas**:
- Ecosistema maduro con amplio soporte
- Validación automática con Bean Validation
- Manejo de excepciones centralizado
- Configuración externa sencilla
- Actuator para health checks y métricas

### ¿Por qué Spark Framework para Funciones?

**Ventajas**:
- Ligero y rápido (crítico para FaaS)
- Sintaxis simple y expresiva
- Bajo consumo de memoria
- Ideal para funciones de corta duración

### ¿Por qué Oracle Database?

- Requisito del proyecto
- Soporte robusto para transacciones
- Triggers y procedimientos almacenados
- Excelente para aplicaciones empresariales

### ¿Por qué HikariCP?

- Pool de conexiones de alto rendimiento
- Reduce latencia en conexiones a BD
- Gestión automática del ciclo de vida de conexiones
- Configuración simple

## Seguridad

### Implementado
- Validación de inputs en todos los endpoints
- Prepared Statements para prevenir SQL injection
- Variables de entorno para credenciales sensibles
- Logs de auditoría en todas las operaciones

### Pendiente para Producción
- Autenticación y autorización (JWT, OAuth2)
- Encriptación de datos sensibles
- Rate limiting
- HTTPS/TLS
- Secrets management (Vault, AWS Secrets Manager)

## Escalabilidad

### Estrategias Implementadas
- Connection pooling en funciones serverless
- Arquitectura stateless
- Contenedores independientes escalables

### Mejoras Futuras
- Implementar caché (Redis) para consultas frecuentes
- Balanceador de carga para el BFF
- Réplicas de lectura en base de datos
- Circuit breaker con Resilience4j
- Message queue para operaciones asíncronas

## Monitoreo y Observabilidad

### Implementado
- Logs estructurados en todos los componentes
- Health checks en todos los servicios
- Spring Boot Actuator en el BFF

### Mejoras Futuras
- Integración con ELK Stack (Elasticsearch, Logstash, Kibana)
- Métricas con Prometheus
- Tracing distribuido con Jaeger o Zipkin
- Alertas automáticas

## Testing

### Estrategia de Pruebas

**Pruebas Unitarias**:
- Validaciones de modelos
- Lógica de servicios
- Transformaciones de datos

**Pruebas de Integración**:
- Comunicación BFF → Funciones
- Funciones → Base de Datos
- Flujos completos end-to-end

**Pruebas de Carga**:
- Apache JMeter para simular carga
- Verificar escalabilidad de funciones
- Identificar cuellos de botella

## Configuración para Docker Lab

Cuando despliegues en Docker Lab, actualiza las siguientes variables de entorno:

### En el Microservicio BFF

```bash
USUARIOS_SERVICE_URL=<url-funcion-usuarios-docker-lab>
PRESTAMOS_SERVICE_URL=<url-funcion-prestamos-docker-lab>
```

### En las Funciones Serverless

```bash
DB_URL=jdbc:oracle:thin:@<host-oracle-docker-lab>:1521:XE
DB_USER=system
DB_PASSWORD=<password-configurado>
```

## Mantenimiento

### Agregar Nueva Funcionalidad

1. Si es una nueva entidad:
   - Crear nueva función serverless
   - Agregar servicio en el BFF
   - Crear controller en el BFF

2. Si es extensión de entidad existente:
   - Agregar endpoint en función serverless
   - Agregar método en servicio del BFF
   - Agregar endpoint en controller del BFF

### Actualizar Dependencias

```bash
# Maven
mvn versions:display-dependency-updates

# Actualizar Spring Boot
# Editar pom.xml parent version
```

## Limitaciones Conocidas

1. **Cold Start**: Primera invocación de funciones puede ser lenta
2. **Connection Pooling**: Limitado en funciones serverless por naturaleza efímera
3. **Transacciones Distribuidas**: No implementadas (eventual consistency)
4. **Sin Caché**: Cada request va a la base de datos
5. **Sincrónico**: Todas las operaciones son síncronas (no hay message queue)

## Roadmap de Mejoras

### Corto Plazo
- [ ] Implementar pruebas unitarias
- [ ] Agregar Swagger/OpenAPI documentation
- [ ] Implementar paginación en listados
- [ ] Agregar filtros y búsqueda

### Mediano Plazo
- [ ] Agregar autenticación JWT
- [ ] Implementar caché con Redis
- [ ] Circuit breaker con Resilience4j
- [ ] Métricas con Micrometer/Prometheus

### Largo Plazo
- [ ] Message queue para operaciones asíncronas
- [ ] Event sourcing para auditoría completa
- [ ] Multi-tenancy
- [ ] Migración a plataforma FaaS nativa (AWS Lambda, Azure Functions)

## Referencias

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Spark Framework](https://sparkjava.com/)
- [Oracle Database Documentation](https://docs.oracle.com/en/database/)
- [Docker Documentation](https://docs.docker.com/)
- [Twelve-Factor App](https://12factor.net/)
