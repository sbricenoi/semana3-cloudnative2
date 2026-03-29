# 🎯 LEE ESTO PRIMERO

## ✅ PROYECTO COMPLETAMENTE CONFIGURADO PARA ORACLE CLOUD

Tu sistema de biblioteca serverless está **100% listo** para funcionar con Oracle Cloud.

---

## 🚀 INICIO RÁPIDO EN 3 PASOS

### Paso 1: Ejecutar Scripts de Base de Datos (5 minutos)

**Abre SQL Developer** y sigue esta guía → `USAR_SQL_DEVELOPER.md`

Resumen:
1. Nueva Conexión → Cloud Wallet → Selecciona carpeta `wallet/`
2. Usuario: `biblioteca` | Password: `#Ng3naUQa*THhTd`
3. Service: `s58onuxcx4c1qxe9_high`
4. Ejecutar `database/schema.sql` (F5)
5. Ejecutar `database/seed.sql` (F5)

### Paso 2: Iniciar Funciones Serverless (2 minutos)

```bash
docker-compose up --build
```

Esto inicia:
- ✅ Función Usuarios (puerto 8081)
- ✅ Función Préstamos (puerto 8082)  
- ✅ Microservicio BFF (puerto 8080)

**Todos se conectan automáticamente a Oracle Cloud.**

### Paso 3: Probar el Sistema (3 minutos)

```bash
# Ver usuarios del seed
curl http://localhost:8080/api/usuarios

# Ver préstamos
curl http://localhost:8080/api/prestamos

# Crear un usuario nuevo
curl -X POST http://localhost:8080/api/usuarios \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Test",
    "apellido": "Usuario",
    "email": "test@email.com",
    "rut": "99999999-9",
    "telefono": "+56999999999"
  }'
```

---

## 📊 ¿QUÉ INCLUYE ESTE PROYECTO?

### Código Implementado
- ✅ **Microservicio BFF** (Spring Boot) - 14 clases Java
- ✅ **Función Serverless de Usuarios** (Java + Spark) - 4 clases
- ✅ **Función Serverless de Préstamos** (Java + Spark) - 4 clases
- ✅ **Scripts de Base de Datos** (schema + seed)
- ✅ **Dockerfiles** optimizados para cada componente
- ✅ **Docker Compose** configurado para Oracle Cloud

### Documentación Completa
- ✅ README principal con instrucciones detalladas
- ✅ Documentación de arquitectura
- ✅ Guías de despliegue
- ✅ Guías de pruebas
- ✅ Instrucciones para video
- ✅ Checklist de entrega
- ✅ Colección de Postman

### Configuración Oracle Cloud
- ✅ Wallet copiado y funcional
- ✅ Connection strings configurados
- ✅ Credenciales en variables de entorno
- ✅ SSL/TLS configurado automáticamente

---

## 🎯 CARACTERÍSTICAS ESPECIALES

### Sin Oracle Local
No necesitas instalar Oracle localmente. Todo funciona con Oracle Cloud:
- Ahorra RAM (no hay contenedor de Oracle)
- Ahorra tiempo (no esperar inicialización de Oracle)
- Datos persistentes en la nube
- Compartible con tu compañero de equipo

### Connection String Completo
Las funciones usan el connection string completo de Oracle Cloud:
```
jdbc:oracle:thin:@(description=(protocol=tcps)(port=1522)
(host=adb.sa-santiago-1.oraclecloud.com)
(service_name=g64afca1579a0d2_s58onuxcx4c1qxe9_high.adb.oraclecloud.com))
```

Incluye:
- Protocolo seguro (TCPS)
- Retry automático (20 intentos)
- SSL server DN match

### Wallet Integrado
El wallet se monta automáticamente en los contenedores Docker:
- Lectura read-only por seguridad
- No se expone fuera de los contenedores
- Configuración SSL/TLS automática

---

## 📁 ARCHIVOS IMPORTANTES

| Archivo | Para qué |
|---------|----------|
| `USAR_SQL_DEVELOPER.md` | ⭐ Ejecutar scripts en BD |
| `INICIO_RAPIDO.md` | ⭐ Iniciar el sistema |
| `README.md` | Documentación completa |
| `PRUEBAS.md` | Casos de prueba |
| `POSTMAN_COLLECTION.json` | Importar en Postman |
| `CHECKLIST_ENTREGA.md` | Antes de entregar |
| `INSTRUCCIONES_VIDEO.md` | Para grabar video |

---

## ⚡ COMANDOS ESENCIALES

```bash
# Ver que wallet existe
ls wallet/

# Iniciar sistema
docker-compose up --build

# Ver logs
docker-compose logs -f

# Probar APIs
curl http://localhost:8080/api/usuarios

# Detener
docker-compose down
```

---

## 📝 CHECKLIST RÁPIDO

Antes de comenzar, verifica:

- [ ] SQL Developer instalado
- [ ] Docker Desktop corriendo
- [ ] Carpeta `wallet/` existe (debe tener 10 archivos)
- [ ] Archivo `.env` existe
- [ ] Tienes acceso a internet (para Oracle Cloud)

---

## 🆘 ¿PROBLEMAS?

### Funciones no conectan a BD
→ Ver `CONFIGURACION_ORACLE_CLOUD.md`

### Error al ejecutar scripts
→ Ver `EJECUTAR_SCRIPTS_MANUAL.md`

### Error con Docker
→ Ver `README.md` sección Troubleshooting

---

## 🎬 FLUJO COMPLETO DE TRABAJO

```
1. Ejecutar scripts en Oracle Cloud (SQL Developer)
   ↓
2. docker-compose up --build
   ↓
3. curl http://localhost:8080/api/usuarios
   ↓
4. Importar POSTMAN_COLLECTION.json
   ↓
5. Probar todos los endpoints
   ↓
6. Desplegar en Docker Lab (ver GUIA_DESPLIEGUE_DOCKER_LAB.md)
   ↓
7. Grabar video (ver INSTRUCCIONES_VIDEO.md)
   ↓
8. Entregar (ver CHECKLIST_ENTREGA.md)
```

---

## 💡 TIP IMPORTANTE

Los datos de la BD Oracle Cloud son **persistentes**. Una vez que ejecutes los scripts, los datos quedarán ahí. No necesitas volver a ejecutarlos cada vez que inicies el sistema.

---

## ✨ SIGUIENTE ACCIÓN

**→ Abre `USAR_SQL_DEVELOPER.md` y ejecuta los scripts de BD**

Una vez hecho eso, el resto es automático con `docker-compose up`.

---

**¡Éxito en tu proyecto!** 🚀
