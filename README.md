# Sistema de Biblioteca - Arquitectura Serverless

Sistema de gestión de biblioteca implementado con arquitectura serverless, utilizando microservicios y funciones serverless para operaciones CRUD sobre usuarios y préstamos de libros.

## Arquitectura

El sistema está compuesto por tres componentes principales:

1. **Microservicio BFF** (Backend For Frontend)
   - Framework: Spring Boot
   - Puerto: 8080
   - Función: Orquesta las llamadas a las funciones serverless

2. **Función Serverless de Usuarios**
   - Lenguaje: Java con Spark Framework
   - Puerto: 8081
   - Función: Gestiona operaciones CRUD de usuarios

3. **Función Serverless de Préstamos**
   - Lenguaje: Java con Spark Framework
   - Puerto: 8082
   - Función: Gestiona operaciones CRUD de préstamos

4. **Base de Datos Oracle**
   - Puerto: 1521
   - Esquema: usuarios, libros, prestamos

## Tecnologías Utilizadas

- Java 17
- Spring Boot 3.2.3
- Spark Framework 2.9.4
- Oracle Database 21c Express Edition
- Docker & Docker Compose
- Maven
- HikariCP (Connection Pooling)

## Requisitos Previos

- Docker Desktop instalado
- Java 17 JDK
- Maven 3.9+
- 8GB RAM mínimo
- 20GB espacio en disco

## Instalación y Ejecución

### 1. Clonar el Repositorio

```bash
git clone <url-del-repositorio>
cd proyecto-biblioteca-serverless
```

### 2. Ejecutar con Docker Compose

```bash
docker-compose up --build
```

Este comando:
- Construye las imágenes de los tres servicios
- Levanta Oracle Database
- Espera a que Oracle esté listo
- Inicia las funciones serverless
- Inicia el microservicio BFF

### 3. Inicializar la Base de Datos

Espera 2-3 minutos después de iniciar los contenedores para que Oracle se inicialice completamente.

```bash
# Conectar a Oracle y ejecutar scripts
docker exec -it oracle-biblioteca sqlplus system/system123@XE @/docker-entrypoint-initdb.d/setup/schema.sql
docker exec -it oracle-biblioteca sqlplus system/system123@XE @/docker-entrypoint-initdb.d/setup/seed.sql
```

### 4. Verificar que los Servicios Están Operativos

```bash
# BFF Health Check
curl http://localhost:8080/actuator/health

# Función Usuarios Health Check
curl http://localhost:8081/health

# Función Préstamos Health Check
curl http://localhost:8082/health
```

## Uso del Sistema

### Ejemplo Completo de Flujo

#### 1. Crear un Usuario

```bash
curl -X POST http://localhost:8080/api/usuarios \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Roberto",
    "apellido": "Sánchez",
    "email": "roberto.sanchez@email.com",
    "rut": "20456789-3",
    "telefono": "+56987654321"
  }'
```

#### 2. Listar Usuarios

```bash
curl http://localhost:8080/api/usuarios
```

#### 3. Crear un Préstamo

```bash
curl -X POST http://localhost:8080/api/prestamos \
  -H "Content-Type: application/json" \
  -d '{
    "idUsuario": 1,
    "idLibro": 2,
    "fechaDevolucionEsperada": "2026-04-15"
  }'
```

#### 4. Listar Préstamos

```bash
curl http://localhost:8080/api/prestamos
```

#### 5. Devolver un Libro

```bash
curl -X PUT http://localhost:8080/api/prestamos/1/devolver
```

#### 6. Consultar Préstamos de un Usuario

```bash
curl http://localhost:8080/api/prestamos/usuario/1
```

## Desarrollo Local (Sin Docker)

### 1. Base de Datos Oracle

Instala Oracle XE y ejecuta los scripts:

```bash
sqlplus system/password@localhost:1521/XE @database/schema.sql
sqlplus system/password@localhost:1521/XE @database/seed.sql
```

### 2. Función de Usuarios

```bash
cd funcion-usuarios
mvn clean package
java -jar target/funcion-usuarios-1.0.0.jar
```

### 3. Función de Préstamos

```bash
cd funcion-prestamos
mvn clean package
java -jar target/funcion-prestamos-1.0.0.jar
```

### 4. Microservicio BFF

```bash
cd microservicio-bff
mvn clean package
java -jar target/microservicio-bff-1.0.0.jar
```

## Despliegue en Docker Lab

### 1. Preparar Imágenes

```bash
docker build -t biblioteca-bff:latest ./microservicio-bff
docker build -t funcion-usuarios:latest ./funcion-usuarios
docker build -t funcion-prestamos:latest ./funcion-prestamos
```

### 2. Subir a Docker Lab

Sigue las instrucciones de Docker Lab para:
- Crear repositorios para cada imagen
- Etiquetar y subir las imágenes
- Desplegar los contenedores

### 3. Configurar Variables de Entorno

Una vez desplegado en Docker Lab, actualiza las variables de entorno del BFF:

```
USUARIOS_SERVICE_URL=<url-generada-por-docker-lab>
PRESTAMOS_SERVICE_URL=<url-generada-por-docker-lab>
```

## Estructura de Respuestas

Todas las APIs devuelven respuestas en el siguiente formato:

### Respuesta Exitosa

```json
{
  "success": true,
  "data": { ... },
  "message": "Operación exitosa"
}
```

### Respuesta de Error

```json
{
  "success": false,
  "error": "Descripción del error"
}
```

## Troubleshooting

### Oracle no inicia
- Verifica que tienes suficiente RAM (mínimo 4GB disponibles)
- Espera 2-3 minutos para la inicialización completa
- Revisa logs: `docker logs oracle-biblioteca`

### Funciones no conectan a Oracle
- Verifica que Oracle esté healthy: `docker ps`
- Verifica la red: `docker network inspect biblioteca-network`
- Revisa logs de las funciones

### BFF no conecta a funciones
- Verifica que las funciones estén operativas
- Revisa las URLs configuradas
- Verifica logs: `docker logs microservicio-bff`

## Monitoreo

### Ver Logs en Tiempo Real

```bash
# Todos los servicios
docker-compose logs -f

# Servicio específico
docker-compose logs -f microservicio-bff
docker-compose logs -f funcion-usuarios
docker-compose logs -f funcion-prestamos
```

### Estado de los Contenedores

```bash
docker-compose ps
```

## Detener el Sistema

```bash
docker-compose down

# Para eliminar también los volúmenes (datos de BD)
docker-compose down -v
```

## Autores

- [Nombre Integrante 1]
- [Nombre Integrante 2]

## Licencia

Proyecto académico - DUOC UC
