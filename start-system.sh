#!/bin/bash

echo "=========================================="
echo "Iniciando Sistema de Biblioteca"
echo "=========================================="

echo "1. Levantando servicios con Docker Compose..."
docker-compose up -d

echo ""
echo "2. Esperando a que Oracle Database esté listo..."
echo "   (Esto puede tomar 2-3 minutos)"

MAX_ATTEMPTS=60
ATTEMPT=0

while [ $ATTEMPT -lt $MAX_ATTEMPTS ]; do
    if docker exec oracle-biblioteca sqlplus -s system/system123@XE <<< "SELECT 1 FROM DUAL;" > /dev/null 2>&1; then
        echo "   Oracle está listo!"
        break
    fi
    ATTEMPT=$((ATTEMPT + 1))
    echo "   Esperando... ($ATTEMPT/$MAX_ATTEMPTS)"
    sleep 5
done

if [ $ATTEMPT -eq $MAX_ATTEMPTS ]; then
    echo "   Error: Oracle no respondió a tiempo"
    exit 1
fi

echo ""
echo "3. Inicializando base de datos..."
./init-database.sh

echo ""
echo "4. Esperando a que los servicios estén listos..."
sleep 10

echo ""
echo "5. Verificando health checks..."

echo "   - Microservicio BFF..."
if curl -s http://localhost:8080/actuator/health | grep -q "UP"; then
    echo "     ✓ BFF operativo"
else
    echo "     ✗ BFF no responde (puede estar iniciando)"
fi

echo "   - Función Usuarios..."
if curl -s http://localhost:8081/health | grep -q "success"; then
    echo "     ✓ Función Usuarios operativa"
else
    echo "     ✗ Función Usuarios no responde"
fi

echo "   - Función Préstamos..."
if curl -s http://localhost:8082/health | grep -q "success"; then
    echo "     ✓ Función Préstamos operativa"
else
    echo "     ✗ Función Préstamos no responde"
fi

echo ""
echo "=========================================="
echo "Sistema iniciado correctamente"
echo "=========================================="
echo ""
echo "URLs disponibles:"
echo "  - BFF:              http://localhost:8080"
echo "  - Función Usuarios: http://localhost:8081"
echo "  - Función Préstamos: http://localhost:8082"
echo "  - Oracle Database:  localhost:1521"
echo ""
echo "Prueba el sistema con:"
echo "  curl http://localhost:8080/api/usuarios"
echo ""
echo "Ver logs:"
echo "  docker-compose logs -f"
echo ""
echo "Detener sistema:"
echo "  docker-compose down"
