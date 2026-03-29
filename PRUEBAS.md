# Guía de Pruebas del Sistema

## Pruebas con cURL

### Health Checks

```bash
# Verificar BFF
curl http://localhost:8080/actuator/health

# Verificar Función Usuarios
curl http://localhost:8081/health

# Verificar Función Préstamos
curl http://localhost:8082/health
```

### Usuarios

#### Crear Usuario

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

Respuesta esperada:
```json
{
  "success": true,
  "data": {
    "id": 6,
    "nombre": "Roberto",
    "apellido": "Sánchez",
    "email": "roberto.sanchez@email.com",
    "rut": "20456789-3",
    "telefono": "+56987654321",
    "fechaRegistro": "2026-03-28T14:30:00.000+00:00",
    "estado": "ACTIVO"
  },
  "message": "Usuario creado exitosamente"
}
```

#### Listar Usuarios

```bash
curl http://localhost:8080/api/usuarios
```

#### Obtener Usuario Específico

```bash
curl http://localhost:8080/api/usuarios/1
```

#### Actualizar Usuario

```bash
curl -X PUT http://localhost:8080/api/usuarios/1 \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Carlos",
    "apellido": "González Pérez",
    "email": "carlos.gonzalez@email.com",
    "rut": "18456789-2",
    "telefono": "+56912345678",
    "estado": "ACTIVO"
  }'
```

#### Eliminar Usuario

```bash
curl -X DELETE http://localhost:8080/api/usuarios/6
```

### Préstamos

#### Crear Préstamo

```bash
curl -X POST http://localhost:8080/api/prestamos \
  -H "Content-Type: application/json" \
  -d '{
    "idUsuario": 1,
    "idLibro": 2,
    "fechaDevolucionEsperada": "2026-04-15"
  }'
```

Respuesta esperada:
```json
{
  "success": true,
  "data": {
    "id": 8,
    "idUsuario": 1,
    "idLibro": 2,
    "fechaPrestamo": "2026-03-28T14:35:00.000+00:00",
    "fechaDevolucionEsperada": "2026-04-15",
    "fechaDevolucionReal": null,
    "estado": "PRESTADO"
  },
  "message": "Préstamo registrado exitosamente"
}
```

#### Listar Préstamos

```bash
curl http://localhost:8080/api/prestamos
```

#### Obtener Préstamo Específico

```bash
curl http://localhost:8080/api/prestamos/1
```

#### Listar Préstamos de un Usuario

```bash
curl http://localhost:8080/api/prestamos/usuario/1
```

#### Devolver Libro

```bash
curl -X PUT http://localhost:8080/api/prestamos/1/devolver
```

#### Actualizar Préstamo

```bash
curl -X PUT http://localhost:8080/api/prestamos/1 \
  -H "Content-Type: application/json" \
  -d '{
    "idUsuario": 1,
    "idLibro": 2,
    "fechaDevolucionEsperada": "2026-04-20",
    "estado": "PRESTADO"
  }'
```

#### Eliminar Préstamo

```bash
curl -X DELETE http://localhost:8080/api/prestamos/8
```

## Casos de Prueba

### Caso 1: Flujo Completo de Préstamo

```bash
# 1. Crear usuario
USER_RESPONSE=$(curl -s -X POST http://localhost:8080/api/usuarios \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Test",
    "apellido": "Prueba",
    "email": "test.prueba@email.com",
    "rut": "11111111-1",
    "telefono": "+56911111111"
  }')

echo "Usuario creado: $USER_RESPONSE"

# 2. Verificar que hay libros disponibles
curl -s http://localhost:8081/libros/1 | grep -o '"cantidad_disponible":[0-9]*'

# 3. Crear préstamo
PRESTAMO_RESPONSE=$(curl -s -X POST http://localhost:8080/api/prestamos \
  -H "Content-Type: application/json" \
  -d '{
    "idUsuario": 6,
    "idLibro": 1,
    "fechaDevolucionEsperada": "2026-04-30"
  }')

echo "Préstamo creado: $PRESTAMO_RESPONSE"

# 4. Verificar que disminuyó la cantidad disponible
curl -s http://localhost:8081/libros/1 | grep -o '"cantidad_disponible":[0-9]*'

# 5. Devolver libro
curl -s -X PUT http://localhost:8080/api/prestamos/8/devolver

# 6. Verificar que aumentó la cantidad disponible
curl -s http://localhost:8081/libros/1 | grep -o '"cantidad_disponible":[0-9]*'
```

### Caso 2: Validación de Libro No Disponible

```bash
# Intentar prestar un libro sin stock (cantidad_disponible = 0)
curl -X POST http://localhost:8080/api/prestamos \
  -H "Content-Type: application/json" \
  -d '{
    "idUsuario": 1,
    "idLibro": 9,
    "fechaDevolucionEsperada": "2026-04-15"
  }'
```

Respuesta esperada:
```json
{
  "success": false,
  "error": "Libro no disponible para préstamo"
}
```

### Caso 3: Validación de Datos

```bash
# Intentar crear usuario sin datos requeridos
curl -X POST http://localhost:8080/api/usuarios \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Test"
  }'
```

Respuesta esperada: Error 400 con detalles de validación

### Caso 4: Préstamos de un Usuario

```bash
# Listar todos los préstamos del usuario 1
curl http://localhost:8080/api/prestamos/usuario/1

# Verificar que solo retorna préstamos de ese usuario
```

### Caso 5: Estados de Préstamos

```bash
# Listar todos los préstamos
curl http://localhost:8080/api/prestamos

# Verificar que hay préstamos en diferentes estados:
# - PRESTADO
# - DEVUELTO
# - RETRASADO
```

## Pruebas Directas a Funciones Serverless

### Función Usuarios

```bash
# Crear usuario directamente en la función
curl -X POST http://localhost:8081/usuarios \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Directo",
    "apellido": "Función",
    "email": "directo@email.com",
    "rut": "22222222-2",
    "telefono": "+56922222222"
  }'

# Listar usuarios
curl http://localhost:8081/usuarios
```

### Función Préstamos

```bash
# Crear préstamo directamente en la función
curl -X POST http://localhost:8082/prestamos \
  -H "Content-Type: application/json" \
  -d '{
    "idUsuario": 1,
    "idLibro": 3,
    "fechaDevolucionEsperada": "2026-05-01"
  }'

# Listar préstamos
curl http://localhost:8082/prestamos
```

## Consultas SQL para Verificación

```sql
-- Ver todos los usuarios
SELECT * FROM usuarios;

-- Ver todos los libros con disponibilidad
SELECT id, titulo, cantidad_disponible, cantidad_total FROM libros;

-- Ver préstamos activos
SELECT p.id, u.nombre, u.apellido, l.titulo, p.estado 
FROM prestamos p
JOIN usuarios u ON p.id_usuario = u.id
JOIN libros l ON p.id_libro = l.id
WHERE p.estado = 'PRESTADO';

-- Ver préstamos retrasados
SELECT p.id, u.nombre, l.titulo, p.fecha_devolucion_esperada 
FROM prestamos p
JOIN usuarios u ON p.id_usuario = u.id
JOIN libros l ON p.id_libro = l.id
WHERE p.estado = 'RETRASADO';
```

## Pruebas de Carga (Opcional)

### Con Apache Bench

```bash
# Instalar Apache Bench
# Mac: brew install httpd
# Ubuntu: sudo apt-get install apache2-utils

# Prueba de carga: 100 requests, 10 concurrentes
ab -n 100 -c 10 http://localhost:8080/api/usuarios
```

### Con wrk

```bash
# Instalar wrk
# Mac: brew install wrk

# Prueba de carga: 30 segundos, 10 threads, 50 conexiones
wrk -t10 -c50 -d30s http://localhost:8080/api/usuarios
```

## Checklist de Pruebas

### Funcionalidad Básica
- [ ] Health check del BFF responde
- [ ] Health check de función usuarios responde
- [ ] Health check de función préstamos responde
- [ ] Crear usuario funciona
- [ ] Listar usuarios retorna datos del seed
- [ ] Obtener usuario por ID funciona
- [ ] Actualizar usuario funciona
- [ ] Eliminar usuario funciona

### Préstamos
- [ ] Crear préstamo funciona
- [ ] Cantidad disponible disminuye al crear préstamo
- [ ] No se puede prestar libro sin stock
- [ ] Listar préstamos funciona
- [ ] Obtener préstamo por ID funciona
- [ ] Listar préstamos por usuario funciona
- [ ] Devolver libro funciona
- [ ] Cantidad disponible aumenta al devolver
- [ ] Estado cambia a DEVUELTO al devolver
- [ ] Eliminar préstamo funciona

### Validaciones
- [ ] No se puede crear usuario sin datos requeridos
- [ ] No se puede crear usuario con email duplicado
- [ ] No se puede crear usuario con RUT duplicado
- [ ] No se puede crear préstamo sin idUsuario
- [ ] No se puede crear préstamo sin idLibro
- [ ] No se puede crear préstamo sin fecha de devolución

### Integración
- [ ] BFF se comunica correctamente con función usuarios
- [ ] BFF se comunica correctamente con función préstamos
- [ ] Funciones se conectan a Oracle correctamente
- [ ] Triggers de BD funcionan correctamente
- [ ] Foreign keys mantienen integridad referencial

### Error Handling
- [ ] Request con datos inválidos retorna 400
- [ ] Recurso no encontrado retorna 404
- [ ] Errores de BD se manejan correctamente
- [ ] Errores de comunicación se reportan apropiadamente

## Resultados Esperados

Todas las pruebas deberían pasar exitosamente. Si alguna falla:

1. Revisar logs del servicio específico
2. Verificar configuración de variables de entorno
3. Verificar conectividad de red entre servicios
4. Verificar que la base de datos esté operativa
5. Verificar que los scripts de BD se ejecutaron correctamente
