# Base de Datos - Sistema de Biblioteca

## Esquema de Base de Datos

El sistema utiliza Oracle Database con las siguientes tablas:

- **usuarios**: Gestión de usuarios de la biblioteca
- **libros**: Catálogo de libros disponibles
- **prestamos**: Registro de préstamos realizados

## Instalación

### Ejecutar Schema

```bash
sqlplus usuario/password@conexion @schema.sql
```

### Cargar Datos de Prueba

```bash
sqlplus usuario/password@conexion @seed.sql
```

## Configuración con Docker

Para ejecutar Oracle Database en Docker:

```bash
docker run -d \
  --name oracle-db \
  -p 1521:1521 \
  -p 5500:5500 \
  -e ORACLE_PWD=system123 \
  container-registry.oracle.com/database/express:latest
```

Esperar a que la base de datos esté lista (2-3 minutos) y luego ejecutar los scripts.
