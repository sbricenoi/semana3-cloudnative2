# Configuración de Base de Datos Oracle Cloud

## Información de Conexión

Ya tenemos:
- ✅ Usuario: `biblioteca`
- ✅ Password: `#Ng3naUQa*THhTd`

**Falta completar** en el archivo `.env`:
- ❓ Host de la base de datos
- ❓ Puerto (generalmente 1521 o 1522 para Oracle Cloud)
- ❓ Service Name

## Paso 1: Obtener Datos de Conexión

### Opción A: Desde Oracle Cloud Console

1. Ir a **Oracle Cloud Console** (cloud.oracle.com)
2. Navegar a **Autonomous Database**
3. Seleccionar tu base de datos `biblioteca`
4. Click en botón **DB Connection**
5. Verás algo como:

```
Connection Strings:
  biblioteca_high: (description=(retry_count=20)...(host=xyz.oraclecloud.com)(port=1522)...
```

De ahí extraes:
- **Host**: `xyz.oraclecloud.com`
- **Puerto**: `1522`
- **Service**: `xyz_biblioteca_high.adb.oraclecloud.com`

### Opción B: Si tienes el Wallet

Si descargaste el Wallet de Oracle Cloud:

1. Extraer el ZIP del Wallet en el proyecto:
   ```bash
   unzip Wallet_biblioteca.zip -d wallet/
   ```

2. Abrir `wallet/tnsnames.ora` y buscar la entrada `biblioteca_high`:
   ```
   biblioteca_high = (description=...)
   ```

3. Copiar el Wallet a las funciones:
   ```bash
   cp -r wallet/ funcion-usuarios/
   cp -r wallet/ funcion-prestamos/
   ```

4. Actualizar `.env`:
   ```
   DB_URL=jdbc:oracle:thin:@biblioteca_high?TNS_ADMIN=/app/wallet
   ```

## Paso 2: Actualizar archivo .env

Edita el archivo `.env` en la raíz del proyecto:

```bash
# Reemplaza <COMPLETAR_...> con los valores reales
DB_URL=jdbc:oracle:thin:@adb.sa-santiago-1.oraclecloud.com:1522/xyz_biblioteca_high.adb.oraclecloud.com
DB_USER=biblioteca
DB_PASSWORD=#Ng3naUQa*THhTd
```

**Ejemplo real**:
```bash
DB_URL=jdbc:oracle:thin:@adb.us-phoenix-1.oraclecloud.com:1522/g12abc3def4_biblioteca_high.adb.oraclecloud.com
DB_USER=biblioteca
DB_PASSWORD=#Ng3naUQa*THhTd
```

## Paso 3: Ejecutar Scripts en la BD Cloud

### Opción 1: Con SQLPlus (Recomendado)

```bash
./ejecutar-scripts-bd.sh
```

Este script:
- Lee las credenciales de `.env`
- Conecta a Oracle Cloud
- Ejecuta `schema.sql`
- Ejecuta `seed.sql`
- Verifica que los datos se insertaron

### Opción 2: Con Python

Si no tienes sqlplus instalado:

```bash
# Instalar driver de Oracle
pip install oracledb

# Ejecutar script
./ejecutar-scripts-python.py
```

### Opción 3: Manual con SQL Developer

1. Abrir Oracle SQL Developer
2. Crear nueva conexión:
   - Name: Biblioteca Cloud
   - Username: biblioteca
   - Password: #Ng3naUQa*THhTd
   - Connection Type: Cloud Wallet o Basic
   - Hostname: (del .env)
   - Port: (del .env)
   - Service name: (del .env)
3. Conectar
4. Abrir `database/schema.sql` → Ejecutar (F5)
5. Abrir `database/seed.sql` → Ejecutar (F5)

## Paso 4: Verificar Conexión

### Prueba Rápida con Python

```python
import oracledb

connection = oracledb.connect(
    user="biblioteca",
    password="#Ng3naUQa*THhTd",
    dsn="<HOST>:<PUERTO>/<SERVICE>"
)

cursor = connection.cursor()
cursor.execute("SELECT COUNT(*) FROM usuarios")
print(f"Usuarios: {cursor.fetchone()[0]}")

cursor.execute("SELECT COUNT(*) FROM libros")
print(f"Libros: {cursor.fetchone()[0]}")

cursor.execute("SELECT COUNT(*) FROM prestamos")
print(f"Préstamos: {cursor.fetchone()[0]}")

connection.close()
print("✓ Conexión exitosa")
```

### Prueba con cURL a través de la función

Una vez que tengas la BD configurada:

```bash
# Iniciar función usuarios
cd funcion-usuarios
export DB_URL="jdbc:oracle:thin:@<HOST>:<PUERTO>/<SERVICE>"
export DB_USER="biblioteca"
export DB_PASSWORD="#Ng3naUQa*THhTd"
mvn clean package
java -jar target/funcion-usuarios-1.0.0.jar

# En otra terminal, probar
curl http://localhost:8081/usuarios
```

## Paso 5: Actualizar Dockerfiles (Si usas Wallet)

### funcion-usuarios/Dockerfile

```dockerfile
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/target/funcion-usuarios-1.0.0.jar app.jar

# Copiar wallet
COPY wallet/ /app/wallet/

EXPOSE 8081
ENV PORT=8081
ENV DB_URL=jdbc:oracle:thin:@biblioteca_high?TNS_ADMIN=/app/wallet
ENV DB_USER=biblioteca
ENV DB_PASSWORD=#Ng3naUQa*THhTd

ENTRYPOINT ["java", "-jar", "app.jar"]
```

Hacer lo mismo para `funcion-prestamos/Dockerfile`

## Paso 6: Probar Sistema Completo

```bash
# Asegúrate de que .env esté completo
cat .env

# Iniciar servicios (sin Oracle local)
docker-compose up --build
```

## Troubleshooting

### Error: "IO Error: The Network Adapter could not establish the connection"

**Solución**:
- Verifica el host y puerto
- Verifica que tienes acceso de red a Oracle Cloud
- Verifica firewall/VPN

### Error: "ORA-01017: invalid username/password"

**Solución**:
- Verifica el usuario: `biblioteca`
- Verifica el password (cuidado con caracteres especiales)
- Intenta entre comillas: `"#Ng3naUQa*THhTd"`

### Error: "ORA-12154: TNS:could not resolve the connect identifier"

**Solución**:
- Verifica el service name
- Si usas Wallet, verifica TNS_ADMIN
- Verifica que el service name existe en tnsnames.ora

### Error: "SSL handshake failed"

**Solución**:
- Oracle Cloud requiere SSL
- Usa el Wallet en lugar de conexión directa
- O configura el truststore apropiado

## Información Adicional Necesaria

Por favor proporciona:

1. **Connection String completo** de Oracle Cloud, o
2. **Archivo Wallet** (ZIP descargado desde Oracle Cloud), o
3. Individualmente:
   - Host: `______________________`
   - Puerto: `______________________`
   - Service Name: `______________________`

Una vez que proporciones esta información, actualizaré automáticamente todos los archivos y ejecutaré los scripts en la base de datos cloud.
