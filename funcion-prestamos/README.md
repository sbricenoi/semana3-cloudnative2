# Función Serverless - Gestión de Préstamos

Función serverless para gestionar operaciones CRUD de préstamos en el sistema de biblioteca.

## Endpoints

### Crear Préstamo
```
POST /prestamos
Content-Type: application/json

{
  "idUsuario": 1,
  "idLibro": 5,
  "fechaDevolucionEsperada": "2026-04-15"
}
```

### Obtener Préstamo
```
GET /prestamos/{id}
```

### Listar Préstamos
```
GET /prestamos
```

### Listar Préstamos por Usuario
```
GET /prestamos/usuario/{idUsuario}
```

### Actualizar Préstamo
```
PUT /prestamos/{id}
Content-Type: application/json

{
  "idUsuario": 1,
  "idLibro": 5,
  "fechaDevolucionEsperada": "2026-04-20",
  "fechaDevolucionReal": null,
  "estado": "PRESTADO"
}
```

### Devolver Libro
```
PUT /prestamos/{id}/devolver
```

### Eliminar Préstamo
```
DELETE /prestamos/{id}
```

### Health Check
```
GET /health
```

## Variables de Entorno

- `PORT`: Puerto del servicio (default: 8082)
- `DB_URL`: URL de conexión a Oracle (default: jdbc:oracle:thin:@localhost:1521:XE)
- `DB_USER`: Usuario de la base de datos (default: system)
- `DB_PASSWORD`: Contraseña de la base de datos (default: system123)

## Compilación y Ejecución

### Local
```bash
mvn clean package
java -jar target/funcion-prestamos-1.0.0.jar
```

### Docker
```bash
docker build -t funcion-prestamos .
docker run -p 8082:8082 \
  -e DB_URL=jdbc:oracle:thin:@host.docker.internal:1521:XE \
  -e DB_USER=system \
  -e DB_PASSWORD=system123 \
  funcion-prestamos
```

## Pruebas

```bash
# Health check
curl http://localhost:8082/health

# Crear préstamo
curl -X POST http://localhost:8082/prestamos \
  -H "Content-Type: application/json" \
  -d '{"idUsuario":1,"idLibro":2,"fechaDevolucionEsperada":"2026-04-15"}'

# Listar préstamos
curl http://localhost:8082/prestamos

# Devolver libro
curl -X PUT http://localhost:8082/prestamos/1/devolver
```
