# Función Serverless - Gestión de Usuarios

Función serverless para gestionar operaciones CRUD de usuarios en el sistema de biblioteca.

## Endpoints

### Crear Usuario
```
POST /usuarios
Content-Type: application/json

{
  "nombre": "Juan",
  "apellido": "Pérez",
  "email": "juan.perez@email.com",
  "rut": "12345678-9",
  "telefono": "+56912345678"
}
```

### Obtener Usuario
```
GET /usuarios/{id}
```

### Listar Usuarios
```
GET /usuarios
```

### Actualizar Usuario
```
PUT /usuarios/{id}
Content-Type: application/json

{
  "nombre": "Juan",
  "apellido": "Pérez",
  "email": "juan.perez@email.com",
  "rut": "12345678-9",
  "telefono": "+56912345678",
  "estado": "ACTIVO"
}
```

### Eliminar Usuario
```
DELETE /usuarios/{id}
```

### Health Check
```
GET /health
```

## Variables de Entorno

- `PORT`: Puerto del servicio (default: 8081)
- `DB_URL`: URL de conexión a Oracle (default: jdbc:oracle:thin:@localhost:1521:XE)
- `DB_USER`: Usuario de la base de datos (default: system)
- `DB_PASSWORD`: Contraseña de la base de datos (default: system123)

## Compilación y Ejecución

### Local
```bash
mvn clean package
java -jar target/funcion-usuarios-1.0.0.jar
```

### Docker
```bash
docker build -t funcion-usuarios .
docker run -p 8081:8081 \
  -e DB_URL=jdbc:oracle:thin:@host.docker.internal:1521:XE \
  -e DB_USER=system \
  -e DB_PASSWORD=system123 \
  funcion-usuarios
```

## Pruebas

```bash
# Health check
curl http://localhost:8081/health

# Crear usuario
curl -X POST http://localhost:8081/usuarios \
  -H "Content-Type: application/json" \
  -d '{"nombre":"Carlos","apellido":"González","email":"carlos@email.com","rut":"18456789-2","telefono":"+56912345678"}'

# Listar usuarios
curl http://localhost:8081/usuarios

# Obtener usuario específico
curl http://localhost:8081/usuarios/1
```
