#!/bin/bash

echo "════════════════════════════════════════════════════════════"
echo "  Ejecutar Scripts en Oracle Cloud con SQLcl"
echo "════════════════════════════════════════════════════════════"
echo ""

WALLET_DIR="$(pwd)/wallet"
USER="biblioteca"
PASSWORD='#Ng3naUQa*THhTd'
SERVICE="s58onuxcx4c1qxe9_high"

if [ ! -d "$WALLET_DIR" ]; then
    echo "❌ Error: Carpeta wallet no encontrada"
    exit 1
fi

echo "Configuración:"
echo "  Wallet: $WALLET_DIR"
echo "  Usuario: $USER"
echo "  Service: $SERVICE"
echo ""

export TNS_ADMIN="$WALLET_DIR"

echo "Método 1: Intentando con sqlplus..."
if command -v sqlplus &> /dev/null; then
    echo "sqlplus encontrado, conectando..."
    sqlplus -s "$USER/\"$PASSWORD\"@$SERVICE" <<EOF
@database/schema.sql
@database/seed.sql
SELECT 'Usuarios: ' || COUNT(*) FROM usuarios;
SELECT 'Libros: ' || COUNT(*) FROM libros;
SELECT 'Prestamos: ' || COUNT(*) FROM prestamos;
EXIT;
EOF
    
    if [ $? -eq 0 ]; then
        echo ""
        echo "✅ Scripts ejecutados exitosamente"
        exit 0
    fi
fi

echo ""
echo "Método 2: Usando SQLcl..."
if command -v sql &> /dev/null; then
    sql "$USER/\"$PASSWORD\"@$SERVICE" <<EOF
@database/schema.sql
@database/seed.sql
SELECT 'Usuarios: ' || COUNT(*) FROM usuarios;
SELECT 'Libros: ' || COUNT(*) FROM libros;
SELECT 'Prestamos: ' || COUNT(*) FROM prestamos;
EXIT;
EOF
    
    if [ $? -eq 0 ]; then
        echo ""
        echo "✅ Scripts ejecutados exitosamente"
        exit 0
    fi
fi

echo ""
echo "❌ No se encontró sqlplus ni SQLcl"
echo ""
echo "Opciones:"
echo "  1. Instalar Oracle Instant Client + sqlplus"
echo "  2. Usar SQL Developer (GUI) para ejecutar los scripts manualmente"
echo "  3. Usar otro cliente SQL compatible con Oracle"
echo ""
echo "Archivos a ejecutar:"
echo "  - database/schema.sql"
echo "  - database/seed.sql"
echo ""
echo "Datos de conexión:"
echo "  - User: biblioteca"
echo "  - Password: #Ng3naUQa*THhTd"
echo "  - Service: s58onuxcx4c1qxe9_high"
echo "  - Wallet: $WALLET_DIR"
