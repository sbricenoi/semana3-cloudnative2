# 🚀 EMPEZAR AQUÍ

Bienvenido al Sistema de Biblioteca Serverless. Esta es tu guía de entrada rápida.

## ¿Qué es este proyecto?

Sistema backend de gestión de biblioteca con arquitectura serverless:
- **Microservicio BFF** orquesta llamadas
- **2 Funciones Serverless** ejecutan operaciones CRUD
- **Base de Datos Oracle** persiste datos
- **Todo dockerizado** y listo para desplegar

## Flujo de Trabajo Recomendado

### Fase 1: Entender el Sistema (15 minutos)

1. Lee `README.md` - Visión general del sistema
2. Revisa `docs/arquitectura.md` - Decisiones técnicas
3. Mira `docs/diagrama-arquitectura.md` - Visualiza componentes

### Fase 2: Ejecutar Localmente (30 minutos)

1. Sigue `INICIO_RAPIDO.md` - Levanta el sistema con Docker
2. Importa `POSTMAN_COLLECTION.json` en Postman
3. Ejecuta pruebas siguiendo `PRUEBAS.md`
4. Verifica que todo funciona

### Fase 3: Revisar Código (1 hora)

1. **Microservicio BFF**: 
   - `microservicio-bff/src/main/java/com/biblioteca/bff/`
   - Empieza por `BffApplication.java`
   - Revisa Controllers → Services → Client

2. **Función Usuarios**:
   - `funcion-usuarios/src/main/java/com/biblioteca/usuarios/`
   - Revisa `UsuariosHandler.java` → `UsuarioDAO.java`

3. **Función Préstamos**:
   - `funcion-prestamos/src/main/java/com/biblioteca/prestamos/`
   - Revisa `PrestamosHandler.java` → `PrestamoDAO.java`

4. **Base de Datos**:
   - `database/schema.sql` - Estructura
   - `database/seed.sql` - Datos de prueba

### Fase 4: Desplegar en Docker Lab (2 horas)

1. Sigue `GUIA_DESPLIEGUE_DOCKER_LAB.md` paso a paso
2. Anota las URLs generadas por Docker Lab
3. Actualiza variables de entorno
4. Prueba todo el flujo en la nube

### Fase 5: Preparar Video (1 hora)

1. Lee `INSTRUCCIONES_VIDEO.md`
2. Practica la demostración
3. Graba el video (8-12 minutos)
4. Revisa calidad de audio y video

### Fase 6: Entregar (30 minutos)

1. Revisa `CHECKLIST_ENTREGA.md`
2. Verifica todos los puntos
3. Comprime el proyecto en .zip
4. Sube video a plataforma indicada
5. Entrega según instrucciones del profesor

## Archivos Clave por Rol

### Si vas a trabajar en el BFF:
- `microservicio-bff/pom.xml`
- `microservicio-bff/src/main/java/com/biblioteca/bff/controller/`
- `microservicio-bff/src/main/java/com/biblioteca/bff/service/`
- `microservicio-bff/src/main/resources/application.properties`

### Si vas a trabajar en las Funciones:
- `funcion-usuarios/src/main/java/com/biblioteca/usuarios/`
- `funcion-prestamos/src/main/java/com/biblioteca/prestamos/`
- `funcion-*/pom.xml`

### Si vas a trabajar en BD:
- `database/schema.sql`
- `database/seed.sql`

### Si vas a trabajar en Docker:
- `docker-compose.yml`
- `*/Dockerfile`
- `init-database.sh`
- `start-system.sh`

## Comandos Más Usados

```bash
# Iniciar todo
./start-system.sh

# Ver logs
docker-compose logs -f

# Reiniciar un servicio
docker-compose restart microservicio-bff

# Reconstruir un servicio
docker-compose up --build microservicio-bff

# Detener todo
docker-compose down

# Limpiar completamente
docker-compose down -v
docker system prune -a
```

## Verificación Rápida

```bash
# ¿Está todo funcionando?
curl http://localhost:8080/actuator/health
curl http://localhost:8081/health
curl http://localhost:8082/health

# ¿Hay datos en la BD?
curl http://localhost:8080/api/usuarios
curl http://localhost:8080/api/prestamos
```

## Estructura del Proyecto

```
proyecto-biblioteca-serverless/
│
├── EMPEZAR_AQUI.md              ← Estás aquí
├── INICIO_RAPIDO.md             ← Instrucciones de inicio
├── README.md                    ← Documentación completa
├── PRUEBAS.md                   ← Casos de prueba
├── CHECKLIST_ENTREGA.md         ← Verificación pre-entrega
│
├── docs/                        ← Documentación técnica
├── database/                    ← Scripts de Oracle
├── microservicio-bff/           ← BFF Spring Boot
├── funcion-usuarios/            ← Función serverless
├── funcion-prestamos/           ← Función serverless
│
├── docker-compose.yml           ← Orquestación Docker
├── POSTMAN_COLLECTION.json      ← Tests de API
└── .git/                        ← Control de versiones
```

## Recursos Adicionales

- **Postman Collection**: Importa `POSTMAN_COLLECTION.json` para probar APIs
- **Diagramas Mermaid**: Visualiza en https://mermaid.live
- **Guía Docker Lab**: `GUIA_DESPLIEGUE_DOCKER_LAB.md`
- **Guía Video**: `INSTRUCCIONES_VIDEO.md`

## ¿Necesitas Ayuda?

1. **Error al compilar**: Revisa que tienes Java 17 y Maven 3.9+
2. **Error en Docker**: Verifica Docker Desktop esté corriendo
3. **Error en Oracle**: Espera más tiempo (puede tardar 5 minutos)
4. **Error en comunicación**: Revisa que todos los contenedores estén up

Consulta la sección Troubleshooting en `README.md` para más detalles.

## Objetivo de la Actividad

Implementar y desplegar un sistema serverless funcional que:
- ✅ Use Spring Boot para el BFF
- ✅ Use funciones serverless en Java
- ✅ Use Oracle Database
- ✅ Esté dockerizado
- ✅ Se despliegue en Docker Lab
- ✅ Tenga Git con participación equitativa
- ✅ Se explique en video

## Evaluación

- **30 pts**: Implementación en la nube
- **30 pts**: Video explicativo
- **30 pts**: Integración completa
- **10 pts**: Git y trabajo colaborativo

**Total**: 100 puntos

## ¡Éxito en tu Proyecto!

El código está completo y listo para usar. Ahora solo necesitas:
1. Probar localmente
2. Desplegar en Docker Lab
3. Grabar video
4. Entregar

**Tiempo estimado total**: 4-6 horas de trabajo
