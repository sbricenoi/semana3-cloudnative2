# Guía Rápida: Ejecutar Scripts con SQL Developer

## Paso 1: Abrir SQL Developer

Descargar de: https://www.oracle.com/database/sqldeveloper/ (si no lo tienes)

## Paso 2: Nueva Conexión

1. Click en el icono verde "+"
2. Llenar los siguientes datos:

```
Connection Name: Biblioteca Cloud
Username: biblioteca
Password: #Ng3naUQa*THhTd
Connection Type: Cloud Wallet
Configuration File: [Click Browse] → Seleccionar carpeta wallet/
Service: s58onuxcx4c1qxe9_high
```

3. Click en **Test** (debería decir "Status: Success")
4. Click en **Connect**

## Paso 3: Ejecutar schema.sql

1. File → Open → `database/schema.sql`
2. Presionar F5 o click en ícono "Run Script" (📄▶)
3. Esperar a que termine (aparecerá "PL/SQL procedure successfully completed")

## Paso 4: Ejecutar seed.sql

1. File → Open → `database/seed.sql`
2. Presionar F5 o click en ícono "Run Script"
3. Esperar mensajes: "1 row inserted"

## Paso 5: Verificar

En SQL Developer, ejecutar:

```sql
SELECT COUNT(*) FROM usuarios;   -- Debe retornar 5
SELECT COUNT(*) FROM libros;     -- Debe retornar 10
SELECT COUNT(*) FROM prestamos;  -- Debe retornar 7
```

## ✅ Listo

Una vez verificado:

```bash
cd proyecto-biblioteca-serverless
docker-compose up --build
```

Las funciones se conectarán automáticamente a Oracle Cloud.

## Screenshots del Proceso

1. **Nueva Conexión**:
   - Selecciona "Cloud Wallet"
   - Apunta a la carpeta `wallet/`

2. **Ejecutar Script**:
   - F5 o botón "Run Script"
   - Ver output en panel inferior

3. **Verificar**:
   - Ejecutar SELECT COUNT(*)
   - Ver resultados
