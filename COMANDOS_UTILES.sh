#!/bin/bash

# Comandos útiles para el Sistema de Biblioteca Serverless

echo "═══════════════════════════════════════════════════════════"
echo "  COMANDOS ÚTILES - Sistema de Biblioteca Serverless"
echo "═══════════════════════════════════════════════════════════"
echo ""

function mostrar_menu() {
    echo "Selecciona una opción:"
    echo ""
    echo "  [1] Iniciar sistema completo"
    echo "  [2] Ver logs en tiempo real"
    echo "  [3] Ver estado de servicios"
    echo "  [4] Health checks"
    echo "  [5] Probar crear usuario"
    echo "  [6] Probar crear préstamo"
    echo "  [7] Listar usuarios"
    echo "  [8] Listar préstamos"
    echo "  [9] Detener sistema"
    echo "  [10] Compilar componentes"
    echo "  [11] Ver commits de Git"
    echo "  [0] Salir"
    echo ""
}

function iniciar_sistema() {
    echo "Iniciando sistema..."
    ./start-system.sh
}

function ver_logs() {
    echo "Mostrando logs (Ctrl+C para salir)..."
    docker-compose logs -f
}

function ver_estado() {
    echo "Estado de los servicios:"
    docker-compose ps
}

function health_checks() {
    echo "Verificando health checks..."
    echo ""
    echo "BFF:"
    curl -s http://localhost:8080/actuator/health | grep -o '"status":"[^"]*"' || echo "No responde"
    echo ""
    echo "Función Usuarios:"
    curl -s http://localhost:8081/health | grep -o '"success":[^,]*' || echo "No responde"
    echo ""
    echo "Función Préstamos:"
    curl -s http://localhost:8082/health | grep -o '"success":[^,]*' || echo "No responde"
    echo ""
}

function probar_crear_usuario() {
    echo "Creando usuario de prueba..."
    curl -X POST http://localhost:8080/api/usuarios \
      -H "Content-Type: application/json" \
      -d '{
        "nombre": "Test",
        "apellido": "Prueba",
        "email": "test.prueba@email.com",
        "rut": "11111111-1",
        "telefono": "+56911111111"
      }'
    echo ""
}

function probar_crear_prestamo() {
    echo "Creando préstamo de prueba..."
    curl -X POST http://localhost:8080/api/prestamos \
      -H "Content-Type: application/json" \
      -d '{
        "idUsuario": 1,
        "idLibro": 1,
        "fechaDevolucionEsperada": "2026-04-30"
      }'
    echo ""
}

function listar_usuarios() {
    echo "Listando usuarios..."
    curl -s http://localhost:8080/api/usuarios | python3 -m json.tool 2>/dev/null || curl http://localhost:8080/api/usuarios
    echo ""
}

function listar_prestamos() {
    echo "Listando préstamos..."
    curl -s http://localhost:8080/api/prestamos | python3 -m json.tool 2>/dev/null || curl http://localhost:8080/api/prestamos
    echo ""
}

function detener_sistema() {
    echo "Deteniendo sistema..."
    docker-compose down
    echo "Sistema detenido"
}

function compilar_componentes() {
    echo "Compilando componentes..."
    echo ""
    echo "1. Microservicio BFF..."
    cd microservicio-bff && mvn clean package -DskipTests && cd ..
    echo ""
    echo "2. Función Usuarios..."
    cd funcion-usuarios && mvn clean package -DskipTests && cd ..
    echo ""
    echo "3. Función Préstamos..."
    cd funcion-prestamos && mvn clean package -DskipTests && cd ..
    echo ""
    echo "Compilación completada"
}

function ver_commits() {
    echo "Commits de Git:"
    git log --oneline --graph --all
    echo ""
    echo "Commits por autor:"
    git shortlog -s -n
}

# Menú interactivo
while true; do
    echo ""
    mostrar_menu
    read -p "Opción: " opcion
    echo ""
    
    case $opcion in
        1) iniciar_sistema ;;
        2) ver_logs ;;
        3) ver_estado ;;
        4) health_checks ;;
        5) probar_crear_usuario ;;
        6) probar_crear_prestamo ;;
        7) listar_usuarios ;;
        8) listar_prestamos ;;
        9) detener_sistema ;;
        10) compilar_componentes ;;
        11) ver_commits ;;
        0) echo "¡Hasta luego!"; exit 0 ;;
        *) echo "Opción inválida" ;;
    esac
    
    read -p "Presiona Enter para continuar..."
done
