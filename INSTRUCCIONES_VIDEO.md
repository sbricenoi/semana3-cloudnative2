# Instrucciones para el Video Explicativo

## Estructura Sugerida del Video (10-12 minutos)

### 1. Introducción (1 minuto)
- Presentación de los integrantes
- Descripción breve del sistema de biblioteca
- Objetivo del proyecto

**Script sugerido**:
> "Buenos días/tardes, somos [Nombre 1] y [Nombre 2]. En este video presentamos el Sistema de Biblioteca implementado con arquitectura serverless. El sistema permite gestionar usuarios y préstamos de libros mediante funciones serverless orquestadas por un microservicio BFF."

### 2. Arquitectura del Sistema (2-3 minutos)

**Qué mostrar**:
- Diagrama de arquitectura (pantalla completa)
- Explicar cada componente:
  - Microservicio BFF (Spring Boot)
  - Función Serverless de Usuarios
  - Función Serverless de Préstamos
  - Base de Datos Oracle
- Flujo de comunicación entre componentes

**Script sugerido**:
> "La arquitectura consta de cuatro componentes principales..."
> "El microservicio BFF actúa como orquestador..."
> "Las funciones serverless ejecutan las operaciones CRUD..."
> "La comunicación entre componentes se realiza mediante HTTP/REST..."

### 3. Código del Microservicio BFF (2 minutos)

**Qué mostrar**:
- Abrir IntelliJ/VSCode con el proyecto
- Mostrar estructura de carpetas
- Abrir `BffApplication.java` brevemente
- Abrir un Controller (ejemplo: `UsuarioController.java`)
- Explicar cómo delega a los services
- Mostrar `ServerlessClient.java` y cómo hace las llamadas HTTP

**Puntos clave**:
- "El BFF expone endpoints REST"
- "Validamos los requests con Bean Validation"
- "Delegamos operaciones a las funciones serverless"
- "Manejamos errores de forma centralizada"

### 4. Código de Funciones Serverless (2 minutos)

**Qué mostrar**:
- Abrir `UsuariosHandler.java`
- Explicar estructura de endpoints
- Mostrar `UsuarioDAO.java` y conexión a BD
- Abrir brevemente `PrestamosHandler.java`
- Mostrar validación de disponibilidad en `PrestamoDAO.java`

**Puntos clave**:
- "Usamos Spark Framework por su simplicidad"
- "HikariCP optimiza las conexiones a la base de datos"
- "Validamos la disponibilidad antes de crear préstamos"
- "Todas las respuestas siguen el formato ApiResponse"

### 5. Base de Datos (1 minuto)

**Qué mostrar**:
- Abrir `schema.sql`
- Explicar brevemente las tablas (usuarios, libros, prestamos)
- Mencionar triggers para automatizar actualizaciones
- Mostrar `seed.sql` con datos de prueba

**Puntos clave**:
- "Tres tablas principales con relaciones"
- "Triggers automatizan actualización de cantidades"
- "Constraints garantizan integridad de datos"

### 6. Demostración Funcional (3-4 minutos)

**Qué mostrar**:

#### a) Abrir Postman/Insomnia

#### b) Health Checks
```bash
GET https://biblioteca-bff-xyz.dockerlab.com/actuator/health
```
Mostrar que responde OK

#### c) Crear Usuario
```bash
POST /api/usuarios
{
  "nombre": "Demo",
  "apellido": "Usuario",
  "email": "demo@email.com",
  "rut": "99999999-9",
  "telefono": "+56999999999"
}
```
Mostrar la respuesta con el usuario creado

#### d) Listar Usuarios
```bash
GET /api/usuarios
```
Mostrar que aparece el usuario recién creado junto con los del seed

#### e) Crear Préstamo
```bash
POST /api/prestamos
{
  "idUsuario": 1,
  "idLibro": 1,
  "fechaDevolucionEsperada": "2026-04-30"
}
```
Mostrar la respuesta exitosa

#### f) Verificar que se Actualizó Cantidad
Hacer consulta a Oracle (opcional) o mostrar en logs que la cantidad disminuyó

#### g) Devolver Libro
```bash
PUT /api/prestamos/{id}/devolver
```
Mostrar que el estado cambia a DEVUELTO

#### h) Listar Préstamos
```bash
GET /api/prestamos
```
Mostrar todos los préstamos con sus estados

### 7. Docker y Docker Lab (1 minuto)

**Qué mostrar**:
- Abrir Docker Lab en el navegador
- Mostrar los 4 contenedores corriendo:
  - oracle-db
  - funcion-usuarios
  - funcion-prestamos
  - microservicio-bff
- Mostrar las URLs asignadas
- Mostrar logs de un contenedor brevemente

**Puntos clave**:
- "Todo el sistema está dockerizado"
- "Docker Lab proporciona URLs públicas"
- "Los contenedores se comunican entre sí"

### 8. Repositorio Git (1 minuto)

**Qué mostrar**:
- Abrir GitHub/GitLab
- Mostrar estructura del repositorio
- Abrir la pestaña de commits
- Mostrar que ambos integrantes tienen commits
- Mencionar uso de branches si aplica

**Puntos clave**:
- "Usamos Git para control de versiones"
- "Commits atómicos y descriptivos"
- "Participación equitativa de ambos integrantes"

### 9. Conclusiones (1 minuto)

**Qué mencionar**:
- Logros alcanzados
- Desafíos enfrentados
- Aprendizajes clave
- Posibles mejoras futuras

**Script sugerido**:
> "Implementamos exitosamente un sistema serverless funcional. Los principales desafíos fueron [mencionar 1-2]. Aprendimos sobre arquitecturas serverless, orquestación de servicios y despliegue con Docker. Como mejoras futuras consideramos implementar autenticación, caché y pruebas automatizadas."

## Checklist Pre-Grabación

### Preparación Técnica
- [ ] Sistema completamente funcional en Docker Lab
- [ ] URLs de Docker Lab anotadas y accesibles
- [ ] Postman configurado con todas las requests
- [ ] Datos de prueba cargados en BD
- [ ] Repositorio Git actualizado con todos los commits

### Preparación de Materiales
- [ ] Diagrama de arquitectura visible y claro
- [ ] IDE abierto con código organizado
- [ ] Terminal/consola preparada
- [ ] Docker Lab abierto en navegador
- [ ] GitHub/GitLab abierto en navegador
- [ ] Notas o script cerca (pero no leer directamente)

### Configuración Técnica
- [ ] Micrófono funcionando correctamente
- [ ] Audio claro y sin ruido de fondo
- [ ] Pantalla en resolución adecuada (1920x1080 recomendado)
- [ ] Software de grabación probado
- [ ] Cerrar notificaciones y aplicaciones innecesarias

### Ensayo
- [ ] Practicar el flujo completo al menos una vez
- [ ] Verificar que todas las demos funcionen
- [ ] Cronometrar para ajustar tiempos
- [ ] Preparar respuestas a posibles preguntas del profesor

## Tips para una Mejor Presentación

### Durante la Grabación

1. **Hablar claro y pausado**
   - No apresurarse
   - Hacer pausas entre secciones
   - Pronunciar términos técnicos correctamente

2. **Mostrar, no solo contar**
   - Ejecutar comandos en vivo
   - Mostrar respuestas reales del sistema
   - Hacer zoom en código importante

3. **Explicar el "por qué", no solo el "qué"**
   - "Usamos HikariCP porque optimiza las conexiones..."
   - "El BFF orquesta las llamadas para simplificar..."

4. **Ser honesto sobre limitaciones**
   - Mencionar qué falta para producción
   - Explicar trade-offs de decisiones

5. **Demostrar dominio técnico**
   - Explicar flujos con confianza
   - Mencionar alternativas consideradas
   - Responder preguntas implícitas

### Errores a Evitar

- ❌ Leer directamente de un script (se nota)
- ❌ Ir demasiado rápido por nerviosismo
- ❌ Mostrar código sin explicar qué hace
- ❌ Video más largo de 15 minutos
- ❌ Audio de mala calidad
- ❌ Pantalla ilegible por tamaño de fuente pequeño
- ❌ No probar nada en vivo (solo mostrar código)

### Checklist de Calidad del Video

- [ ] Audio claro sin ruido de fondo
- [ ] Pantalla legible (fuente grande)
- [ ] Ambos integrantes participan
- [ ] Demostraciones en vivo funcionan
- [ ] Duración adecuada (8-12 minutos ideal)
- [ ] Explicaciones claras y técnicamente correctas
- [ ] Se muestra el sistema funcionando en Docker Lab
- [ ] Se evidencia uso de Git con participación equitativa

## Herramientas de Grabación Recomendadas

- **OBS Studio** (gratuito, profesional)
- **Loom** (fácil de usar, nube)
- **Zoom** (grabar reunión)
- **QuickTime** (Mac)
- **Windows Game Bar** (Windows)

## Formato de Entrega del Video

- **Formato**: MP4, MOV, o según indicaciones del profesor
- **Resolución**: 1920x1080 (Full HD) recomendado
- **Duración**: 8-12 minutos
- **Tamaño**: Comprimir si es mayor a 500MB
- **Plataforma**: YouTube (unlisted), Google Drive, o según indicaciones

## Ejemplo de División de Participación

### Integrante 1:
- Introducción
- Explicación de arquitectura
- Código del microservicio BFF
- Demostración de usuarios

### Integrante 2:
- Código de funciones serverless
- Base de datos y triggers
- Demostración de préstamos
- Git y conclusiones

**Importante**: Ambos deben demostrar dominio técnico de todo el sistema, no solo de su parte.
