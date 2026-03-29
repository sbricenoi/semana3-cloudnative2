# Guía de Despliegue en Docker Lab

Esta guía detalla los pasos para desplegar el sistema de biblioteca en Docker Lab.

## Preparación Previa

### 1. Construir Imágenes Localmente

```bash
cd proyecto-biblioteca-serverless

# Construir imagen del BFF
docker build -t biblioteca-bff:latest ./microservicio-bff

# Construir imagen de función usuarios
docker build -t funcion-usuarios:latest ./funcion-usuarios

# Construir imagen de función préstamos
docker build -t funcion-prestamos:latest ./funcion-prestamos
```

### 2. Probar Localmente

```bash
docker-compose up
```

Verifica que todo funcione correctamente antes de subir a Docker Lab.

## Despliegue en Docker Lab

### Paso 1: Desplegar Oracle Database

1. Buscar imagen de Oracle Database en Docker Lab o usar una compatible
2. Crear contenedor con configuración:
   - Puerto: 1521
   - Variables de entorno:
     - `ORACLE_PWD=system123`
   - Volumen para persistencia de datos

3. Anotar la URL generada por Docker Lab:
   ```
   Ejemplo: oracle-db-xyz.dockerlab.com:1521
   ```

4. Conectar y ejecutar scripts:
   ```bash
   # Opción 1: Copiar scripts al contenedor
   docker cp database/schema.sql <container-id>:/tmp/
   docker cp database/seed.sql <container-id>:/tmp/
   docker exec -it <container-id> sqlplus system/system123@XE @/tmp/schema.sql
   docker exec -it <container-id> sqlplus system/system123@XE @/tmp/seed.sql
   
   # Opción 2: Usar cliente SQL desde tu máquina
   sqlplus system/system123@<url-docker-lab>:1521/XE @database/schema.sql
   sqlplus system/system123@<url-docker-lab>:1521/XE @database/seed.sql
   ```

### Paso 2: Etiquetar y Subir Función de Usuarios

1. Etiquetar imagen para Docker Lab:
   ```bash
   docker tag funcion-usuarios:latest <docker-lab-registry>/funcion-usuarios:latest
   ```

2. Subir imagen:
   ```bash
   docker push <docker-lab-registry>/funcion-usuarios:latest
   ```

3. Crear contenedor en Docker Lab:
   - Imagen: funcion-usuarios:latest
   - Puerto: 8081
   - Variables de entorno:
     - `PORT=8081`
     - `DB_URL=jdbc:oracle:thin:@<url-oracle-docker-lab>:1521:XE`
     - `DB_USER=system`
     - `DB_PASSWORD=system123`

4. Anotar la URL generada:
   ```
   Ejemplo: https://funcion-usuarios-xyz.dockerlab.com
   ```

### Paso 3: Etiquetar y Subir Función de Préstamos

1. Etiquetar imagen:
   ```bash
   docker tag funcion-prestamos:latest <docker-lab-registry>/funcion-prestamos:latest
   ```

2. Subir imagen:
   ```bash
   docker push <docker-lab-registry>/funcion-prestamos:latest
   ```

3. Crear contenedor en Docker Lab:
   - Imagen: funcion-prestamos:latest
   - Puerto: 8082
   - Variables de entorno:
     - `PORT=8082`
     - `DB_URL=jdbc:oracle:thin:@<url-oracle-docker-lab>:1521:XE`
     - `DB_USER=system`
     - `DB_PASSWORD=system123`

4. Anotar la URL generada:
   ```
   Ejemplo: https://funcion-prestamos-xyz.dockerlab.com
   ```

### Paso 4: Etiquetar y Subir Microservicio BFF

1. Etiquetar imagen:
   ```bash
   docker tag biblioteca-bff:latest <docker-lab-registry>/biblioteca-bff:latest
   ```

2. Subir imagen:
   ```bash
   docker push <docker-lab-registry>/biblioteca-bff:latest
   ```

3. Crear contenedor en Docker Lab:
   - Imagen: biblioteca-bff:latest
   - Puerto: 8080
   - Variables de entorno (IMPORTANTE - usar URLs de Docker Lab):
     - `PORT=8080`
     - `USUARIOS_SERVICE_URL=https://funcion-usuarios-xyz.dockerlab.com`
     - `PRESTAMOS_SERVICE_URL=https://funcion-prestamos-xyz.dockerlab.com`

4. Anotar la URL generada:
   ```
   Ejemplo: https://biblioteca-bff-xyz.dockerlab.com
   ```

## Verificación Post-Despliegue

### 1. Health Checks

```bash
# Verificar BFF
curl https://biblioteca-bff-xyz.dockerlab.com/actuator/health

# Verificar Función Usuarios
curl https://funcion-usuarios-xyz.dockerlab.com/health

# Verificar Función Préstamos
curl https://funcion-prestamos-xyz.dockerlab.com/health
```

### 2. Prueba de Flujo Completo

```bash
# Crear usuario
curl -X POST https://biblioteca-bff-xyz.dockerlab.com/api/usuarios \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Test",
    "apellido": "Usuario",
    "email": "test@email.com",
    "rut": "11111111-1",
    "telefono": "+56911111111"
  }'

# Listar usuarios (debería incluir el recién creado + los del seed)
curl https://biblioteca-bff-xyz.dockerlab.com/api/usuarios

# Crear préstamo
curl -X POST https://biblioteca-bff-xyz.dockerlab.com/api/prestamos \
  -H "Content-Type: application/json" \
  -d '{
    "idUsuario": 1,
    "idLibro": 1,
    "fechaDevolucionEsperada": "2026-04-30"
  }'

# Listar préstamos
curl https://biblioteca-bff-xyz.dockerlab.com/api/prestamos
```

## Actualizar Variables de Entorno

Si necesitas cambiar las URLs después del despliegue:

### Opción 1: Actualizar desde Docker Lab UI
1. Ir al contenedor del BFF
2. Editar variables de entorno
3. Reiniciar contenedor

### Opción 2: Redesplegar con Nuevas Variables
```bash
# Reconstruir solo el BFF con nuevas URLs
docker build -t biblioteca-bff:latest ./microservicio-bff
docker tag biblioteca-bff:latest <docker-lab-registry>/biblioteca-bff:latest
docker push <docker-lab-registry>/biblioteca-bff:latest
```

## Troubleshooting en Docker Lab

### Problema: Funciones no conectan a Oracle

**Solución**:
- Verifica que Oracle esté corriendo: consultar logs del contenedor
- Verifica la URL de conexión en las variables de entorno
- Asegúrate de que los scripts schema.sql y seed.sql se ejecutaron correctamente

### Problema: BFF retorna "Service Unavailable"

**Solución**:
- Verifica que las URLs de las funciones sean correctas
- Verifica que las funciones estén corriendo y respondan a /health
- Revisa los logs del BFF para ver el error específico

### Problema: "Libro no disponible"

**Solución**:
- Verifica que el libro exista en la tabla libros
- Verifica que cantidad_disponible > 0
- Consulta directamente en Oracle:
  ```sql
  SELECT id, titulo, cantidad_disponible FROM libros;
  ```

## URLs de Ejemplo para Documentación

Después del despliegue, documenta las URLs reales:

```
Base de Datos Oracle:
- Host: <url-docker-lab>
- Puerto: 1521
- SID: XE

Función Usuarios:
- URL Base: https://funcion-usuarios-xyz.dockerlab.com
- Health: https://funcion-usuarios-xyz.dockerlab.com/health

Función Préstamos:
- URL Base: https://funcion-prestamos-xyz.dockerlab.com
- Health: https://funcion-prestamos-xyz.dockerlab.com/health

Microservicio BFF:
- URL Base: https://biblioteca-bff-xyz.dockerlab.com
- Health: https://biblioteca-bff-xyz.dockerlab.com/actuator/health
- API Usuarios: https://biblioteca-bff-xyz.dockerlab.com/api/usuarios
- API Préstamos: https://biblioteca-bff-xyz.dockerlab.com/api/prestamos
```

## Checklist de Despliegue

- [ ] Oracle Database desplegado y funcionando
- [ ] Scripts schema.sql y seed.sql ejecutados exitosamente
- [ ] Función de Usuarios desplegada con variables de entorno correctas
- [ ] Función de Préstamos desplegada con variables de entorno correctas
- [ ] Microservicio BFF desplegado con URLs de funciones correctas
- [ ] Health checks de todos los servicios responden OK
- [ ] Prueba de creación de usuario exitosa
- [ ] Prueba de creación de préstamo exitosa
- [ ] Prueba de devolución de libro exitosa
- [ ] URLs documentadas para el video y entrega final

## Monitoreo en Docker Lab

### Ver Logs

Desde la interfaz de Docker Lab:
1. Ir a cada contenedor
2. Ver sección "Logs"
3. Filtrar por errores o timestamps específicos

### Métricas

Monitorear:
- CPU usage de cada contenedor
- Memory usage
- Network I/O
- Número de requests

## Respaldo de Base de Datos

Antes de realizar cambios importantes:

```bash
# Exportar datos
docker exec oracle-biblioteca sh -c 'exp system/system123@XE file=/tmp/backup.dmp full=y'
docker cp <container-id>:/tmp/backup.dmp ./backup-$(date +%Y%m%d).dmp
```

## Notas Importantes

1. Docker Lab asigna URLs únicas - anótalas en cuanto las obtengas
2. Los contenedores pueden reiniciarse - verifica que las variables de entorno persistan
3. Guarda las URLs finales para incluir en el video explicativo
4. Toma screenshots del sistema funcionando como evidencia
