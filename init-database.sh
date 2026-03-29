#!/bin/bash

echo "=========================================="
echo "Inicializando Base de Datos Oracle"
echo "=========================================="

CONTAINER_NAME="oracle-biblioteca"
DB_PASSWORD="system123"

echo "Esperando a que Oracle esté listo..."
sleep 30

echo "Verificando estado del contenedor..."
if ! docker ps | grep -q $CONTAINER_NAME; then
    echo "Error: El contenedor $CONTAINER_NAME no está corriendo"
    echo "Ejecuta: docker-compose up -d oracle-db"
    exit 1
fi

echo "Copiando scripts al contenedor..."
docker cp database/schema.sql $CONTAINER_NAME:/tmp/
docker cp database/seed.sql $CONTAINER_NAME:/tmp/

echo "Ejecutando schema.sql..."
docker exec -it $CONTAINER_NAME sqlplus -s system/$DB_PASSWORD@XE @/tmp/schema.sql

echo "Ejecutando seed.sql..."
docker exec -it $CONTAINER_NAME sqlplus -s system/$DB_PASSWORD@XE @/tmp/seed.sql

echo ""
echo "=========================================="
echo "Base de datos inicializada correctamente"
echo "=========================================="
echo ""
echo "Puedes verificar con:"
echo "docker exec -it $CONTAINER_NAME sqlplus system/$DB_PASSWORD@XE"
echo "SQL> SELECT COUNT(*) FROM usuarios;"
echo "SQL> SELECT COUNT(*) FROM libros;"
echo "SQL> SELECT COUNT(*) FROM prestamos;"
