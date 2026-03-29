# Checklist de Entrega

## Archivos y Código

### Microservicio BFF
- [x] pom.xml configurado correctamente
- [x] BffApplication.java (clase principal)
- [x] Controllers (UsuarioController, PrestamoController)
- [x] Services (UsuarioService, PrestamoService)
- [x] Client (ServerlessClient)
- [x] Models (Usuario, Prestamo, ApiResponse)
- [x] Exception handling (GlobalExceptionHandler)
- [x] Configuration (ServerlessConfig, application.properties)
- [x] Dockerfile
- [x] README.md

### Función Serverless de Usuarios
- [x] pom.xml configurado correctamente
- [x] UsuariosHandler.java (handler principal)
- [x] UsuarioDAO.java (acceso a datos)
- [x] Model Usuario
- [x] Model ApiResponse
- [x] Dockerfile
- [x] README.md

### Función Serverless de Préstamos
- [x] pom.xml configurado correctamente
- [x] PrestamosHandler.java (handler principal)
- [x] PrestamoDAO.java (acceso a datos)
- [x] Model Prestamo
- [x] Model ApiResponse
- [x] Dockerfile
- [x] README.md

### Base de Datos
- [x] schema.sql (creación de tablas, secuencias, triggers)
- [x] seed.sql (datos de prueba)
- [x] README.md

### Infraestructura
- [x] docker-compose.yml
- [x] .gitignore
- [x] Scripts de inicialización (init-database.sh, start-system.sh)

### Documentación
- [x] README.md principal
- [x] docs/arquitectura.md
- [x] docs/diagrama-arquitectura.md
- [x] POSTMAN_COLLECTION.json
- [x] GUIA_DESPLIEGUE_DOCKER_LAB.md
- [x] INSTRUCCIONES_VIDEO.md
- [x] PRUEBAS.md

## Verificaciones Técnicas

### Compilación
- [ ] Microservicio BFF compila sin errores: `cd microservicio-bff && mvn clean package`
- [ ] Función usuarios compila sin errores: `cd funcion-usuarios && mvn clean package`
- [ ] Función préstamos compila sin errores: `cd funcion-prestamos && mvn clean package`

### Docker
- [ ] Dockerfile del BFF construye correctamente
- [ ] Dockerfile de función usuarios construye correctamente
- [ ] Dockerfile de función préstamos construye correctamente
- [ ] docker-compose up levanta todos los servicios
- [ ] Contenedores se comunican entre sí

### Base de Datos
- [ ] schema.sql se ejecuta sin errores
- [ ] seed.sql se ejecuta sin errores
- [ ] Tablas creadas correctamente
- [ ] Secuencias funcionan
- [ ] Triggers funcionan
- [ ] Foreign keys funcionan
- [ ] Datos de prueba cargados

### Funcionalidad
- [ ] Health checks responden
- [ ] Crear usuario funciona
- [ ] Listar usuarios funciona
- [ ] Obtener usuario funciona
- [ ] Actualizar usuario funciona
- [ ] Eliminar usuario funciona
- [ ] Crear préstamo funciona
- [ ] Validación de disponibilidad funciona
- [ ] Listar préstamos funciona
- [ ] Obtener préstamo funciona
- [ ] Listar préstamos por usuario funciona
- [ ] Devolver libro funciona
- [ ] Actualizar préstamo funciona
- [ ] Eliminar préstamo funciona

### Validaciones
- [ ] No se puede crear usuario sin datos requeridos
- [ ] No se puede crear préstamo de libro sin stock
- [ ] Email y RUT únicos se validan
- [ ] Errores retornan códigos HTTP apropiados
- [ ] Mensajes de error son claros

### Integración
- [ ] BFF conecta con función usuarios
- [ ] BFF conecta con función préstamos
- [ ] Funciones conectan con Oracle
- [ ] Flujo end-to-end funciona
- [ ] Cantidad de libros se actualiza correctamente

## Git y Colaboración

- [ ] Repositorio Git inicializado
- [ ] .gitignore configurado
- [ ] Commits realizados
- [ ] Ambos integrantes tienen commits
- [ ] Mensajes de commit son descriptivos
- [ ] Estructura de branches clara (si aplica)
- [ ] README tiene instrucciones claras

## Despliegue en Docker Lab

- [ ] Oracle Database desplegado
- [ ] Scripts ejecutados en Oracle
- [ ] Función usuarios desplegada
- [ ] Función préstamos desplegada
- [ ] Microservicio BFF desplegado
- [ ] Variables de entorno actualizadas con URLs de Docker Lab
- [ ] Sistema funciona en Docker Lab
- [ ] URLs de Docker Lab documentadas

## Documentación

- [ ] README.md principal completo
- [ ] Instrucciones de instalación claras
- [ ] Instrucciones de ejecución detalladas
- [ ] Endpoints documentados con ejemplos
- [ ] Variables de entorno documentadas
- [ ] Arquitectura documentada
- [ ] Decisiones técnicas justificadas
- [ ] Diagramas incluidos

## Video

- [ ] Video grabado
- [ ] Duración: 8-12 minutos
- [ ] Audio claro
- [ ] Ambos integrantes participan
- [ ] Se explica la arquitectura
- [ ] Se muestra el código
- [ ] Se demuestra el funcionamiento
- [ ] Se muestra Docker Lab
- [ ] Se muestra repositorio Git
- [ ] Conclusiones incluidas

## Archivo Comprimido Final

- [ ] Crear carpeta con todo el código
- [ ] Incluir carpeta .git (o link al repositorio)
- [ ] Incluir todos los archivos fuente
- [ ] Incluir documentación
- [ ] Incluir diagrama
- [ ] Comprimir en .zip o .rar
- [ ] Verificar que el archivo no sea excesivamente grande
- [ ] Nombre del archivo: `Biblioteca-Serverless-[Apellido1]-[Apellido2].zip`

## Puntos de Evaluación

### Criterio 1: Implementación (30 pts)
- [ ] Todas las funciones implementadas
- [ ] Alta calidad de código
- [ ] Sin errores
- [ ] Buenas prácticas aplicadas

### Criterio 2: Video (30 pts)
- [ ] Explicaciones claras
- [ ] Dominio del tema evidente
- [ ] Coherencia con desarrollo
- [ ] Duración apropiada

### Criterio 3: Integración (30 pts)
- [ ] Integración completa
- [ ] Comunicación correcta entre componentes
- [ ] Funciona end-to-end
- [ ] Desplegado en Docker Lab

### Criterio 4: Git (10 pts)
- [ ] Uso correcto de Git
- [ ] Organización del desarrollo
- [ ] Participación equitativa evidente

## Notas Finales

### Antes de Entregar

1. Probar el sistema completo al menos 3 veces
2. Verificar que el video se vea y escuche correctamente
3. Revisar que el archivo comprimido contenga todo
4. Confirmar que las URLs de Docker Lab están documentadas
5. Hacer un último commit con todo finalizado

### Durante la Presentación (si aplica)

- Tener el sistema corriendo en Docker Lab
- Tener Postman o alternativa lista
- Tener repositorio Git accesible
- Estar preparado para responder preguntas técnicas

### Recordatorios Importantes

- Las URLs de Docker Lab deben estar configuradas en el BFF
- El video debe mostrar evidencia real (no screenshots estáticos)
- Ambos integrantes deben demostrar conocimiento del sistema completo
- El sistema debe estar completamente funcional en la nube
