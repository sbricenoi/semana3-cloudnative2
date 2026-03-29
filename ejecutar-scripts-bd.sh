#!/bin/bash

echo "════════════════════════════════════════════════════════════"
echo "  Ejecutar Scripts en Oracle Cloud"
echo "════════════════════════════════════════════════════════════"
echo ""

# Cargar variables de entorno
if [ -f .env ]; then
    export $(cat .env | grep -v '^#' | xargs)
fi

# Verificar que las variables estén configuradas
if [[ "$DB_URL" == *"COMPLETAR"* ]]; then
    echo "❌ Error: Debes completar las credenciales en el archivo .env"
    echo ""
    echo "Edita el archivo .env y completa:"
    echo "  - DB_URL con el host, puerto y service name"
    echo ""
    exit 1
fi

echo "Configuración detectada:"
echo "  Usuario: $DB_USER"
echo "  URL: $DB_URL"
echo ""

# Extraer datos de la URL
if [[ $DB_URL =~ jdbc:oracle:thin:@([^:]+):([0-9]+)/(.+) ]]; then
    HOST="${BASH_REMATCH[1]}"
    PORT="${BASH_REMATCH[2]}"
    SERVICE="${BASH_REMATCH[3]}"
else
    echo "❌ Error: Formato de DB_URL inválido"
    exit 1
fi

echo "Conectando a Oracle Cloud..."
echo "  Host: $HOST"
echo "  Puerto: $PORT"
echo "  Service: $SERVICE"
echo ""

# Verificar si sqlplus está instalado
if ! command -v sqlplus &> /dev/null; then
    echo "❌ sqlplus no está instalado"
    echo ""
    echo "Opciones:"
    echo "  1. Instalar Oracle Instant Client"
    echo "  2. Usar SQL Developer con GUI"
    echo "  3. Usar script Python (ver opción abajo)"
    exit 1
fi

echo "Ejecutando schema.sql..."
sqlplus -s "$DB_USER/\"$DB_PASSWORD\"@//$HOST:$PORT/$SERVICE" @database/schema.sql

if [ $? -eq 0 ]; then
    echo "✓ schema.sql ejecutado exitosamente"
else
    echo "❌ Error al ejecutar schema.sql"
    exit 1
fi

echo ""
echo "Ejecutando seed.sql..."
sqlplus -s "$DB_USER/\"$DB_PASSWORD\"@//$HOST:$PORT/$SERVICE" @database/seed.sql

if [ $? -eq 0 ]; then
    echo "✓ seed.sql ejecutado exitosamente"
else
    echo "❌ Error al ejecutar seed.sql"
    exit 1
fi

echo ""
echo "════════════════════════════════════════════════════════════"
echo "  Scripts ejecutados correctamente en Oracle Cloud"
echo "════════════════════════════════════════════════════════════"
echo ""
echo "Puedes verificar con:"
echo "  sqlplus $DB_USER/\"***\"@//$HOST:$PORT/$SERVICE"
echo "  SQL> SELECT COUNT(*) FROM usuarios;"
echo "  SQL> SELECT COUNT(*) FROM libros;"
echo "  SQL> SELECT COUNT(*) FROM prestamos;"
