# Microservicio BFF - Sistema de Biblioteca

Backend For Frontend que orquesta las llamadas a las funciones serverless del sistema de biblioteca.

## Arquitectura

El BFF actúa como capa de orquestación entre los clientes y las funciones serverless:
- Expone APIs REST unificadas
- Delega operaciones CRUD a funciones serverless
- Maneja errores y validaciones
- Proporciona respuestas consistentes

## Endpoints

### Usuarios

#### Crear Usuario
```
POST /api/usuarios
Content-Type: application/json

{
  "nombre": "Juan",
  "apellido": "Pérez",
  "email": "juan.perez@email.com",
  "rut": "12345678-9",
  "telefono": "+56912345678"
}
```

#### Obtener Usuario
```
GET /api/usuarios/{id}
```

#### Listar Usuarios
```
GET /api/usuarios
```

#### Actualizar Usuario
```
PUT /api/usuarios/{id}
Content-Type: application/json

{
  "nombre": "Juan",
  "apellido": "Pérez González",
  "email": "juan.perez@email.com",
  "rut": "12345678-9",
  "telefono": "+56912345678",
  "estado": "ACTIVO"
}
```

#### Eliminar Usuario
```
DELETE /api/usuarios/{id}
```

### Préstamos

#### Crear Préstamo
```
POST /api/prestamos
Content-Type: application/json

{
  "idUsuario": 1,
  "idLibro": 5,
  "fechaDevolucionEsperada": "2026-04-15"
}
```

#### Obtener Préstamo
```
GET /api/prestamos/{id}
```

#### Listar Préstamos
```
GET /api/prestamos
```

#### Listar Préstamos por Usuario
```
GET /api/prestamos/usuario/{idUsuario}
```

#### Actualizar Préstamo
```
PUT /api/prestamos/{id}
Content-Type: application/json

{
  "idUsuario": 1,
  "idLibro": 5,
  "fechaDevolucionEsperada": "2026-04-20",
  "estado": "PRESTADO"
}
```

#### Devolver Libro
```
PUT /api/prestamos/{id}/devolver
```

#### Eliminar Préstamo
```
DELETE /api/prestamos/{id}
```

### Health Check
```
GET /actuator/health
```

## Variables de Entorno

- `PORT`: Puerto del servicio (default: 8080)
- `USUARIOS_SERVICE_URL`: URL de la función serverless de usuarios (default: http://localhost:8081)
- `PRESTAMOS_SERVICE_URL`: URL de la función serverless de préstamos (default: http://localhost:8082)

## Compilación y Ejecución

### Local
```bash
mvn clean package
java -jar target/microservicio-bff-1.0.0.jar
```

### Docker
```bash
docker build -t microservicio-bff .
docker run -p 8080:8080 \
  -e USUARIOS_SERVICE_URL=http://funcion-usuarios:8081 \
  -e PRESTAMOS_SERVICE_URL=http://funcion-prestamos:8082 \
  microservicio-bff
```

## Pruebas

```bash
# Health check
curl http://localhost:8080/actuator/health

# Crear usuario
curl -X POST http://localhost:8080/api/usuarios \
  -H "Content-Type: application/json" \
  -d '{"nombre":"Carlos","apellido":"González","email":"carlos@email.com","rut":"18456789-2","telefono":"+56912345678"}'

# Crear préstamo
curl -X POST http://localhost:8080/api/prestamos \
  -H "Content-Type: application/json" \
  -d '{"idUsuario":1,"idLibro":2,"fechaDevolucionEsperada":"2026-04-15"}'

# Devolver libro
curl -X PUT http://localhost:8080/api/prestamos/1/devolver
```

## Dependencias de Funciones Serverless

El BFF requiere que las siguientes funciones estén operativas:
- Función de Usuarios (puerto 8081)
- Función de Préstamos (puerto 8082)
