# Conexión a Base de Datos Oracle Cloud

## Credenciales

- **Usuario**: `biblioteca`
- **Password**: `#Ng3naUQa*THhTd`

## Información Requerida

Para completar la configuración, necesitas:

1. **Host/URL** de la base de datos Oracle Cloud
2. **Puerto** (generalmente 1521 o 1522)
3. **Service Name** (ej: `biblioteca_high`, `biblioteca_low`, etc.)

## Formatos de Conexión

### Conexión Simple (Thin)

```
jdbc:oracle:thin:@<HOST>:<PUERTO>/<SERVICE_NAME>
```

Ejemplo:
```
jdbc:oracle:thin:@adb.sa-santiago-1.oraclecloud.com:1522/biblioteca_high
```

### Conexión con Wallet (Oracle Cloud Autonomous)

Si tienes un Wallet descargado:

```
jdbc:oracle:thin:@<SERVICE_NAME>?TNS_ADMIN=/path/to/wallet
```

## Actualizar Configuración del Proyecto

### 1. Actualizar docker-compose.yml

Elimina el servicio `oracle-db` y actualiza las variables de entorno:

```yaml
funcion-usuarios:
  environment:
    - DB_URL=jdbc:oracle:thin:@<HOST>:<PUERTO>/<SERVICE_NAME>
    - DB_USER=biblioteca
    - DB_PASSWORD=#Ng3naUQa*THhTd

funcion-prestamos:
  environment:
    - DB_URL=jdbc:oracle:thin:@<HOST>:<PUERTO>/<SERVICE_NAME>
    - DB_USER=biblioteca
    - DB_PASSWORD=#Ng3naUQa*THhTd
```

### 2. Actualizar .env (crear archivo)

```bash
DB_URL=jdbc:oracle:thin:@<HOST>:<PUERTO>/<SERVICE_NAME>
DB_USER=biblioteca
DB_PASSWORD=#Ng3naUQa*THhTd
```

### 3. Si usas Oracle Wallet

1. Descargar Wallet desde Oracle Cloud Console
2. Extraer en carpeta del proyecto
3. Actualizar connection string:
   ```
   DB_URL=jdbc:oracle:thin:@biblioteca_high?TNS_ADMIN=/app/wallet
   ```
4. Copiar wallet en Dockerfiles

## Ejecutar Scripts en BD Cloud

### Opción 1: SQL Developer / SQL*Plus

```bash
sqlplus biblioteca/"#Ng3naUQa*THhTd"@<HOST>:<PUERTO>/<SERVICE_NAME>

SQL> @database/schema.sql
SQL> @database/seed.sql
```

### Opción 2: Con Wallet

```bash
# Configurar TNS_ADMIN
export TNS_ADMIN=/path/to/wallet

# Conectar
sqlplus biblioteca/"#Ng3naUQa*THhTd"@biblioteca_high

SQL> @database/schema.sql
SQL> @database/seed.sql
```

### Opción 3: Desde Python (alternativa)

```python
import cx_Oracle

connection = cx_Oracle.connect(
    user="biblioteca",
    password="#Ng3naUQa*THhTd",
    dsn="<HOST>:<PUERTO>/<SERVICE_NAME>"
)

with open('database/schema.sql', 'r') as f:
    cursor = connection.cursor()
    cursor.execute(f.read())

connection.commit()
```

## Próximos Pasos

1. **Proporciona los datos faltantes**:
   - Host de la BD
   - Puerto
   - Service Name
   - O proporciona el Wallet de Oracle Cloud

2. **Ejecutar scripts**:
   - schema.sql
   - seed.sql

3. **Probar conexión**:
   ```bash
   # Actualizar variables y probar
   docker-compose up funcion-usuarios
   ```

## Dónde Encontrar la Información

### Oracle Cloud Console

1. Ir a **Oracle Cloud** → **Autonomous Database**
2. Seleccionar tu base de datos `biblioteca`
3. Click en **DB Connection**
4. Copiar:
   - Connection String
   - Service Names disponibles
5. Opcional: Descargar Wallet

### Formato típico de Oracle Cloud

```
(description= 
  (retry_count=20)
  (retry_delay=3)
  (address=
    (protocol=tcps)
    (port=1522)
    (host=adb.sa-santiago-1.oraclecloud.com))
  (connect_data=
    (service_name=xyz_biblioteca_high.adb.oraclecloud.com))
  (security=
    (ssl_server_dn_match=yes)))
```

De aquí extraes:
- Host: `adb.sa-santiago-1.oraclecloud.com`
- Puerto: `1522`
- Service: `xyz_biblioteca_high.adb.oraclecloud.com`

## Notas Importantes

- Oracle Cloud Autonomous DB generalmente usa puerto **1522** (no 1521)
- Requiere SSL/TLS (protocolo `tcps`)
- Si usa Wallet, es más sencillo
- El password contiene caracteres especiales, asegúrate de escaparlos correctamente en comandos shell
