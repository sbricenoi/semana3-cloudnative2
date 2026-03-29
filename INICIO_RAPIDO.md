# Inicio Rápido

Guía de 5 minutos para poner en marcha el sistema.

## Requisitos

- Docker Desktop instalado y corriendo
- Puerto 8080, 8081, 8082 y 1521 disponibles
- 8GB RAM disponible
- Terminal/CMD

## Pasos

### 1. Navegar al Proyecto

```bash
cd proyecto-biblioteca-serverless
```

### 2. Iniciar el Sistema

```bash
./start-system.sh
```

**Nota**: Este script:
- Levanta todos los servicios con Docker Compose
- Espera a que Oracle esté listo
- Inicializa la base de datos automáticamente
- Verifica health checks

**Tiempo estimado**: 3-5 minutos

### 3. Verificar que Todo Funciona

```bash
# Listar usuarios del seed
curl http://localhost:8080/api/usuarios

# Listar préstamos del seed
curl http://localhost:8080/api/prestamos
```

Si ves datos en formato JSON, el sistema está funcionando correctamente.

## Pruebas Rápidas

### Crear un Usuario

```bash
curl -X POST http://localhost:8080/api/usuarios \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Test",
    "apellido": "Usuario",
    "email": "test@email.com",
    "rut": "99999999-9",
    "telefono": "+56999999999"
  }'
```

### Crear un Préstamo

```bash
curl -X POST http://localhost:8080/api/prestamos \
  -H "Content-Type: application/json" \
  -d '{
    "idUsuario": 1,
    "idLibro": 1,
    "fechaDevolucionEsperada": "2026-04-30"
  }'
```

### Devolver un Libro

```bash
curl -X PUT http://localhost:8080/api/prestamos/1/devolver
```

## Usar Postman

1. Abrir Postman
2. Importar `POSTMAN_COLLECTION.json`
3. Ejecutar los requests de la colección

## Detener el Sistema

```bash
docker-compose down
```

Para eliminar también los datos:

```bash
docker-compose down -v
```

## Problemas Comunes

### Oracle no inicia
- Verifica que tienes suficiente RAM disponible
- Espera 3-5 minutos (Oracle tarda en iniciar)
- Revisa logs: `docker-compose logs oracle-db`

### Puerto ya en uso
```bash
# Ver qué está usando el puerto
lsof -i :8080
lsof -i :1521

# Cambiar puertos en docker-compose.yml o detener proceso
```

### Funciones no conectan a BD
- Espera a que Oracle termine de inicializar
- Verifica health check: `docker exec oracle-biblioteca sqlplus system/system123@XE`

## Siguiente Paso

Una vez que el sistema funciona localmente:
1. Lee `GUIA_DESPLIEGUE_DOCKER_LAB.md` para subir a Docker Lab
2. Lee `INSTRUCCIONES_VIDEO.md` para grabar el video
3. Revisa `CHECKLIST_ENTREGA.md` antes de entregar

## Más Información

- Documentación completa: `README.md`
- Arquitectura detallada: `docs/arquitectura.md`
- Pruebas exhaustivas: `PRUEBAS.md`
