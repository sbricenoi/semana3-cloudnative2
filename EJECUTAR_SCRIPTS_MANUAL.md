# Ejecutar Scripts Manualmente en Oracle Cloud

Debido a limitaciones con los scripts automatizados, aquí están las instrucciones para ejecutar los scripts manualmente.

## Opción 1: SQL Developer (Recomendado)

### Paso 1: Abrir SQL Developer

1. Abrir Oracle SQL Developer
2. Click en el icono "+" (Nueva conexión)

### Paso 2: Configurar Conexión con Wallet

1. **Name**: Biblioteca Cloud
2. **Authentication Type**: Cloud Wallet
3. **Configuration File**: Seleccionar la carpeta `wallet/`
4. **Username**: `biblioteca`
5. **Password**: `#Ng3naUQa*THhTd`
6. **Service**: Seleccionar `s58onuxcx4c1qxe9_high` del dropdown
7. Click en **Test** para verificar conexión
8. Click en **Connect**

### Paso 3: Ejecutar schema.sql

1. File → Open → Seleccionar `database/schema.sql`
2. Click en el botón de ejecutar (▶) o presionar F5
3. Verificar en la salida que todo se ejecutó correctamente

### Paso 4: Ejecutar seed.sql

1. File → Open → Seleccionar `database/seed.sql`
2. Click en el botón de ejecutar (▶) o presionar F5
3. Verificar en la salida que se insertaron los datos

### Paso 5: Verificar Datos

Ejecutar estas consultas:

```sql
SELECT COUNT(*) FROM usuarios;
SELECT COUNT(*) FROM libros;
SELECT COUNT(*) FROM prestamos;

SELECT * FROM usuarios;
SELECT * FROM libros;
SELECT * FROM prestamos;
```

Deberías ver:
- 5 usuarios
- 10 libros
- 7 préstamos

---

## Opción 2: Oracle Cloud SQL Worksheet

### Paso 1: Acceder a SQL Worksheet

1. Ir a Oracle Cloud Console
2. Autonomous Database → Tu base de datos
3. Click en **Database Actions** → **SQL**

### Paso 2: Ejecutar Scripts

1. Copiar el contenido completo de `database/schema.sql`
2. Pegar en SQL Worksheet
3. Click en **Run Script** (ícono de play verde)
4. Esperar a que termine
5. Repetir con `database/seed.sql`

### Paso 3: Verificar

```sql
SELECT COUNT(*) FROM usuarios;
SELECT COUNT(*) FROM libros;
SELECT COUNT(*) FROM prestamos;
```

---

## Opción 3: Desde el Proyecto Anterior

Si ya tienes el proyecto anterior configurado y funcionando:

```bash
cd "/Users/sbriceno/Documents/DUOC/CLOUDNATIVE/Semana 3 Sumativa 1"

# Copiar scripts al proyecto anterior
cp "/Users/sbriceno/Documents/DUOC/CLOUDNATIVE2/semana 3/proyecto-biblioteca-serverless/database/schema.sql" ./temp-schema.sql
cp "/Users/sbriceno/Documents/DUOC/CLOUDNATIVE2/semana 3/proyecto-biblioteca-serverless/database/seed.sql" ./temp-seed.sql

# Ejecutar con el método que ya funciona en ese proyecto
```

---

## Datos de Conexión

Para cualquier método, usa:

- **Usuario**: `biblioteca`
- **Password**: `#Ng3naUQa*THhTd`
- **Service**: `s58onuxcx4c1qxe9_high`
- **Wallet**: `wallet/` (carpeta en el proyecto)
- **Connection String**: 
  ```
  (description=(retry_count=20)(retry_delay=3)
   (address=(protocol=tcps)(port=1522)(host=adb.sa-santiago-1.oraclecloud.com))
   (connect_data=(service_name=g64afca1579a0d2_s58onuxcx4c1qxe9_high.adb.oraclecloud.com))
   (security=(ssl_server_dn_match=yes)))
  ```

---

## Verificación Post-Ejecución

Una vez ejecutados los scripts, verifica que se crearon correctamente:

```sql
-- Ver tablas creadas
SELECT table_name FROM user_tables;

-- Ver secuencias
SELECT sequence_name FROM user_sequences;

-- Ver triggers
SELECT trigger_name FROM user_triggers;

-- Contar registros
SELECT 'usuarios', COUNT(*) FROM usuarios UNION ALL
SELECT 'libros', COUNT(*) FROM libros UNION ALL
SELECT 'prestamos', COUNT(*) FROM prestamos;
```

Resultado esperado:
- 3 tablas (USUARIOS, LIBROS, PRESTAMOS)
- 3 secuencias
- 3 triggers
- 5 usuarios, 10 libros, 7 préstamos

---

## Siguiente Paso

Una vez que los scripts estén ejecutados exitosamente en Oracle Cloud, puedes iniciar las funciones serverless:

```bash
docker-compose up --build
```

Las funciones se conectarán automáticamente a Oracle Cloud usando las credenciales configuradas.
