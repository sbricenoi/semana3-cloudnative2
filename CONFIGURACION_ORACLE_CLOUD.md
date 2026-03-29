# ✅ Configuración de Oracle Cloud - LISTA

## Estado: CONFIGURADO

El proyecto está completamente configurado para usar Oracle Cloud Autonomous Database.

## Datos de Conexión

- **Usuario**: `biblioteca`
- **Password**: `#Ng3naUQa*THhTd`
- **Host**: `adb.sa-santiago-1.oraclecloud.com`
- **Puerto**: `1522`
- **Service**: `g64afca1579a0d2_s58onuxcx4c1qxe9_high.adb.oraclecloud.com`
- **Wallet**: `wallet/` (copiado y listo)

## ¿Qué está Configurado?

### ✅ Funciones Serverless

Ambas funciones (`funcion-usuarios` y `funcion-prestamos`) están configuradas para:
- Conectarse a Oracle Cloud usando el connection string completo
- Usar las credenciales del usuario `biblioteca`
- Cargar el wallet automáticamente
- Connection pooling con HikariCP

### ✅ Dockerfiles

Los Dockerfiles incluyen:
- Copia del wallet al contenedor
- Variables de entorno con connection string completo
- Configuración TNS_ADMIN para el wallet

### ✅ Docker Compose

El archivo `docker-compose.yml`:
- No incluye Oracle local (usa Oracle Cloud)
- Monta el wallet como volumen read-only
- Configura todas las variables de entorno necesarias
- Listo para ejecutar con `docker-compose up`

## Flujo de Trabajo

### 1. Ejecutar Scripts de BD (UNA VEZ)

Usa SQL Developer para ejecutar:
1. `database/schema.sql` - Crea tablas, secuencias y triggers
2. `database/seed.sql` - Inserta datos de prueba

**Guía detallada**: `USAR_SQL_DEVELOPER.md`

### 2. Iniciar Funciones Serverless

```bash
docker-compose up --build
```

Esto inicia:
- Función Usuarios (puerto 8081)
- Función Préstamos (puerto 8082)
- Microservicio BFF (puerto 8080)

Todos se conectan automáticamente a Oracle Cloud.

### 3. Probar el Sistema

```bash
# Health checks
curl http://localhost:8080/actuator/health
curl http://localhost:8081/health
curl http://localhost:8082/health

# Listar datos
curl http://localhost:8080/api/usuarios
curl http://localhost:8080/api/prestamos
```

## Archivos Modificados para Oracle Cloud

- ✅ `funcion-usuarios/Dockerfile` - Connection string y wallet
- ✅ `funcion-prestamos/Dockerfile` - Connection string y wallet
- ✅ `funcion-usuarios/src/.../dao/UsuarioDAO.java` - URL de Oracle Cloud
- ✅ `funcion-prestamos/src/.../dao/PrestamoDAO.java` - URL de Oracle Cloud
- ✅ `docker-compose.yml` - Eliminado Oracle local, agregado wallet
- ✅ `.env` - Credenciales configuradas
- ✅ `wallet/` - Wallet copiado desde proyecto anterior

## Ventajas de Esta Configuración

1. **No requiere Oracle local** - Ahorra RAM y espacio
2. **Datos persistentes** - Los datos están en la nube
3. **Misma BD para todos** - Puedes compartir con tu compañero
4. **Listo para Docker Lab** - Solo cambias URLs, la BD ya está en la nube
5. **Connection string embebido** - No depende de archivos externos

## Próximo Paso

**Ejecutar scripts de BD con SQL Developer**:
1. Abrir SQL Developer
2. Configurar conexión con wallet (ver `USAR_SQL_DEVELOPER.md`)
3. Ejecutar `database/schema.sql`
4. Ejecutar `database/seed.sql`
5. Verificar que hay datos
6. Luego: `docker-compose up --build`

## Troubleshooting

### "ORA-01017: invalid username/password"
- Verifica que el usuario `biblioteca` existe en Oracle Cloud
- Si no existe, créalo o usa `ADMIN` temporalmente

### "ORA-00942: table or view does not exist"
- Los scripts no se han ejecutado aún
- Ejecuta `schema.sql` primero

### "Could not establish connection"
- Verifica que tienes acceso a internet
- Verifica que la carpeta `wallet/` existe
- Verifica que los archivos del wallet están completos

## Comandos Útiles

```bash
# Ver logs de funciones
docker-compose logs -f funcion-usuarios
docker-compose logs -f funcion-prestamos

# Reiniciar solo una función
docker-compose restart funcion-usuarios

# Detener todo
docker-compose down

# Iniciar solo funciones (sin BFF)
docker-compose up funcion-usuarios funcion-prestamos
```

## Verificar Configuración

```bash
# Ver variables de entorno
cat .env

# Ver que wallet existe
ls -la wallet/

# Ver archivos del wallet
ls wallet/*.jks wallet/*.ora wallet/*.sso 2>/dev/null | wc -l
# Debería mostrar varios archivos
```

---

**Estado**: ✅ Configuración completa
**Siguiente acción**: Ejecutar scripts con SQL Developer
**Tiempo estimado**: 10 minutos
