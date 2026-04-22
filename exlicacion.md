# 📚 Explicación del render.yaml

## 🎯 ¿Qué es render.yaml?

Es un archivo de configuración que define cómo desplegar múltiples aplicaciones en **Render.com**. Especifica dónde está cada código, qué dependencias instalar, y cómo iniciar cada servicio.

---

## 📋 Estructura General

```yaml
services:  # Lista de servicios a desplegar
  - type: web  # Cada uno es una aplicación web
```

---

## 🟢 **API Node.js**

```yaml
- type: web
  name: api-node
  runtime: node                    # Usa Node.js como entorno
  rootDir: Api-Node              # Carpeta donde está el código Node
  buildCommand: npm ci           # Instala dependencias (npm ci = instalación segura)
  startCommand: npm start        # Comando para iniciar la aplicación
  envVars:
    - key: PORT
      value: 10000               # Puerto en el que corre la aplicación
```

### 📝 Desglose:
- **runtime: node** → Render detecta que es una app Node.js
- **buildCommand: npm ci** → Instala las dependencias definidas en `package.json`
  - `npm ci` es más seguro que `npm install` para CI/CD
- **startCommand: npm start** → Ejecuta el script `start` del `package.json` (generalmente `node index.js`)
- **PORT: 10000** → La app Node escucha en el puerto 10000

---

## 🔵 **API Spring Boot**

```yaml
- type: web
  name: api-springboot
  runtime: docker                        # Usa Docker para la ejecución
  rootDir: Api-SpringBoot               # Carpeta del proyecto Spring Boot
  dockerfilePath: ./Dockerfile          # Ruta al archivo Dockerfile
  dockerContext: .                      # Contexto de compilación (carpeta raíz)
```

### 📝 Desglose:
- **runtime: docker** → Render va a usar Docker para desplegar
- **dockerfilePath: ./Dockerfile** → Lee las instrucciones de compilación desde el Dockerfile
- **dockerContext: .** → Envía el contexto completo del proyecto a Docker
- **Ventaja:** Todo está encapsulado en la imagen Docker (JVM, dependencias, etc.)

### ¿Qué hace el Dockerfile?
Generalmente:
1. Toma una imagen base de Java (ej: `openjdk:11`)
2. Compila el proyecto (`mvn package`)
3. Copia el JAR compilado
4. Define el comando para iniciar Spring Boot

---

## 🟡 **API Django**

```yaml
- type: web
  name: api-django
  runtime: python                # Usa Python como entorno
  rootDir: Api-Django           # Carpeta donde está el código Django
  buildCommand: |
    pip install -r requirements.txt
    && python manage.py migrate --noinput
  startCommand: gunicorn config.wsgi:application --bind 0.0.0.0:$PORT
```

### 📝 Desglose:

#### buildCommand (Se ejecuta UNA VEZ al desplegar):
```bash
pip install -r requirements.txt      # Instala todas las dependencias Python
python manage.py migrate --noinput   # Aplica migraciones de BD sin preguntar
```

#### startCommand (Se ejecuta cada vez que inicia):
```bash
gunicorn config.wsgi:application --bind 0.0.0.0:$PORT
```
- **gunicorn** → Servidor web WSGI (necesario para producción)
- **config.wsgi:application** → Dónde está la app WSGI de Django
- **--bind 0.0.0.0** → Escucha en TODAS las interfaces de red
- **$PORT** → Variable de entorno que asigna Render (dinámicamente)

---

## 🔑 Conceptos Clave

| Concepto | Significado |
|----------|------------|
| **runtime** | Entorno de ejecución (node, python, docker, etc.) |
| **rootDir** | Carpeta donde está el código del proyecto |
| **buildCommand** | Se ejecuta UNA VEZ al desplegar (instalar dependencias, migraciones) |
| **startCommand** | Se ejecuta CADA VEZ que la app inicia |
| **envVars** | Variables de entorno disponibles para la app |
| **$PORT** | Variable que Render proporciona automáticamente |

---

## 🚀 Flujo de Despliegue

```
1. Render lee render.yaml
   ↓
2. Para cada servicio:
   ├─ Configura el entorno (node, python, docker)
   ├─ Ejecuta buildCommand (una sola vez)
   └─ Ejecuta startCommand (cada reinicio)
   ↓
3. Las apps quedan disponibles en URLs públicas
```

---

## 📊 Comparación de Runtimes

| API | Runtime | Ventaja | Desventaja |
|-----|---------|---------|-----------|
| Node | `node` | Ligero, rápido | Requiere Node instalado |
| Django | `python` | Flexible, amplio ecosistema | Más lento, necesita Gunicorn |
| Spring Boot | `docker` | Máxima portabilidad | Más pesado, imagen Docker grande |

---

## 💡 Notas Importantes

1. **$PORT es dinámico**: Render asigna un puerto diferente cada vez. Por eso usamos `$PORT` en variables
2. **buildCommand vs startCommand**: El build es una sola vez (instalar deps), el start es cada reinicio
3. **Orden importa**: Django ejecuta migraciones en el build, no en el start
4. **Gunicorn es necesario**: Django en desarrollo usa `runserver`, pero en producción necesita Gunicorn o similar
