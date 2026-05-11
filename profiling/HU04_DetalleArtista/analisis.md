# Análisis de Perfilamiento: HU04 - Detalle de Artista

## 1. Descripción del Flujo
El usuario realiza el siguiente flujo de navegación:
1. Desde el **Listado de Artistas**, el usuario selecciona un artista específico.
2. Se carga la pantalla de **Detalle del Artista**, la cual incluye:
   - Imagen del artista.
   - Información biográfica (nombre, fecha de nacimiento, descripción).
   - Lista de álbumes asociados al artista.
3. El usuario visualiza la información y realiza scroll para ver el contenido completo.
4. El usuario toca un álbum asociado y navega al detalle del álbum.
5. El usuario regresa al listado de artistas.

## 2. Resultados del Profiler (Hallazgos Observados)

### CPU
- **Uso:** Pico de actividad al momento de la transición entre pantallas y durante el renderizado de la imagen del artista y la lista de álbumes asociados.
- **Trace:** El archivo `.trace` captura la ejecución de los hilos encargados del parseo de la información del artista y sus álbumes.

### Memoria
- **Comportamiento:** Incremento controlado durante la carga de la imagen del artista. El uso de memoria se mantiene estable una vez finalizada la carga, sin mostrar fugas evidentes.

### Red (Network)
- **Peticiones:** Se observan peticiones GET al endpoint `/musicians/{id}` o `/bands/{id}` para obtener la información del artista y sus álbumes asociados.

### Energía
- **Impacto:** Bajo.

## 3. Evidencia
[Gráfica Profiler](evidencia_grafica.png)

[Archivo .trace de sesión](https://uniandes-my.sharepoint.com/:u:/g/personal/oj_gomezo1_uniandes_edu_co/IQB1qF01t7_DQYZIh92HCJtXAUUQeH2W41AxbqAIwLXU-Cc?e=hSvD0B)
