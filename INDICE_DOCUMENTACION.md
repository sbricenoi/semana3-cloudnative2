# Índice de Documentación

Guía rápida de todos los archivos de documentación del proyecto.

## 📖 Documentación de Entrada

### Para Empezar
| Archivo | Descripción | Tiempo de Lectura |
|---------|-------------|-------------------|
| **EMPEZAR_AQUI.md** | Punto de entrada principal | 5 min |
| **INICIO_RAPIDO.md** | Guía de inicio en 5 minutos | 5 min |
| **README.md** | Documentación completa del sistema | 15 min |

### Resumen y Estado
| Archivo | Descripción |
|---------|-------------|
| **IMPLEMENTACION_COMPLETA.txt** | Estado del proyecto con estadísticas |
| **RESUMEN_IMPLEMENTACION.md** | Resumen detallado de implementación |

## 🏗️ Documentación Técnica

### Arquitectura
| Archivo | Descripción |
|---------|-------------|
| **docs/arquitectura.md** | Decisiones arquitectónicas detalladas |
| **docs/diagrama-arquitectura.md** | Diagramas ASCII del sistema |
| **docs/diagrama.mmd** | Diagrama de componentes (Mermaid) |
| **docs/modelo-datos.mmd** | Modelo entidad-relación (Mermaid) |

### Componentes
| Archivo | Componente |
|---------|------------|
| **microservicio-bff/README.md** | Documentación del BFF |
| **funcion-usuarios/README.md** | Documentación función usuarios |
| **funcion-prestamos/README.md** | Documentación función préstamos |
| **database/README.md** | Documentación de base de datos |

## 🧪 Pruebas y Validación

| Archivo | Descripción |
|---------|-------------|
| **PRUEBAS.md** | Casos de prueba con cURL |
| **POSTMAN_COLLECTION.json** | Colección de Postman importable |
| **CHECKLIST_ENTREGA.md** | Verificación antes de entregar |

## 🚀 Despliegue

| Archivo | Descripción |
|---------|-------------|
| **GUIA_DESPLIEGUE_DOCKER_LAB.md** | Paso a paso para Docker Lab |
| **.env.example** | Template de variables de entorno |
| **docker-compose.yml** | Orquestación de servicios |

## 🎥 Video

| Archivo | Descripción |
|---------|-------------|
| **INSTRUCCIONES_VIDEO.md** | Guía completa para grabar video |

## 👥 Equipo

| Archivo | Descripción |
|---------|-------------|
| **CONTRIBUIDORES.md** | Información de integrantes |

## 🛠️ Scripts y Herramientas

| Archivo | Descripción |
|---------|-------------|
| **start-system.sh** | Inicia sistema automáticamente |
| **init-database.sh** | Inicializa base de datos |
| **COMANDOS_UTILES.sh** | Menú interactivo con comandos |

## 📊 Por Tipo de Actividad

### Si vas a Codificar
1. `README.md` de cada componente
2. `docs/arquitectura.md`
3. Código fuente en `src/`

### Si vas a Probar
1. `INICIO_RAPIDO.md`
2. `PRUEBAS.md`
3. `POSTMAN_COLLECTION.json`

### Si vas a Desplegar
1. `GUIA_DESPLIEGUE_DOCKER_LAB.md`
2. `.env.example`
3. `docker-compose.yml`

### Si vas a Grabar Video
1. `INSTRUCCIONES_VIDEO.md`
2. `docs/diagrama-arquitectura.md`
3. `POSTMAN_COLLECTION.json` (para demos)

### Si vas a Entregar
1. `CHECKLIST_ENTREGA.md`
2. `CONTRIBUIDORES.md`
3. `.gitignore`

## 🎯 Lectura Recomendada por Orden

### Día 1 - Entender
1. `EMPEZAR_AQUI.md`
2. `README.md`
3. `docs/arquitectura.md`
4. `docs/diagrama-arquitectura.md`

### Día 2 - Ejecutar y Probar
1. `INICIO_RAPIDO.md`
2. `PRUEBAS.md`
3. READMEs de componentes

### Día 3 - Desplegar
1. `GUIA_DESPLIEGUE_DOCKER_LAB.md`
2. `.env.example`

### Día 4 - Finalizar
1. `INSTRUCCIONES_VIDEO.md`
2. `CHECKLIST_ENTREGA.md`
3. `CONTRIBUIDORES.md`

## 📏 Estadísticas de Documentación

- **Total archivos Markdown**: 16
- **Total palabras**: ~15,000
- **Páginas equivalentes**: ~50 páginas
- **Tiempo total de lectura**: ~2 horas
- **Diagramas incluidos**: 3

## 🔗 Enlaces Internos Útiles

### Desde el README Principal
- Arquitectura → `docs/arquitectura.md`
- Despliegue → `GUIA_DESPLIEGUE_DOCKER_LAB.md`
- Pruebas → `PRUEBAS.md`

### Desde Arquitectura
- Diagrama visual → `docs/diagrama-arquitectura.md`
- Modelo de datos → `docs/modelo-datos.mmd`

### Desde Componentes
- Configuración → `src/main/resources/application.properties`
- Build → `pom.xml`
- Deploy → `Dockerfile`

## 💡 Tips de Navegación

1. **Buscar en archivos**:
   ```bash
   grep -r "ServerlessClient" .
   grep -r "HikariCP" .
   ```

2. **Ver estructura de carpetas**:
   ```bash
   find . -type d -maxdepth 3
   ```

3. **Buscar documentación específica**:
   ```bash
   find . -name "README.md"
   find . -name "*.md"
   ```

## ✅ Documentación Completa

Todos los aspectos del proyecto están documentados:
- ✓ Arquitectura y decisiones técnicas
- ✓ Instrucciones de instalación y ejecución
- ✓ Guías de pruebas
- ✓ Proceso de despliegue
- ✓ Preparación del video
- ✓ Checklist de entrega
- ✓ Código comentado apropiadamente

---

**Última actualización**: 28 de marzo de 2026
