# Análisis de Perfilamiento: HU06 - Detalle de Coleccionista

## 1. Descripción del Flujo
El usuario realiza el siguiente flujo de navegación:
1. Desde el **Listado de Coleccionistas**, el usuario selecciona un coleccionista específico.
2. Se carga la pantalla de **Detalle del Coleccionista**, la cual incluye:
   - Información del coleccionista (nombre, email, teléfono).
   - Estadísticas (álbumes, artistas favoritos, reseñas, rating promedio).
   - Colección de álbumes asociados.
3. El usuario visualiza la información y realiza scroll para ver la colección completa.
4. El usuario toca un álbum de la colección y navega al detalle del álbum.
5. El usuario regresa al detalle del coleccionista y luego al listado.

## 2. Resultados del Profiler (Hallazgos Observados)

### CPU
- **Uso:** Pico de actividad durante la carga del detalle del coleccionista, que incluye múltiples llamadas paralelas al backend para obtener álbumes y artistas favoritos. La actividad se normaliza una vez completada la carga.
- **Trace:** El archivo `.trace` captura la ejecución de las corrutinas encargadas de las peticiones paralelas y el renderizado de la grilla de álbumes.

### Memoria
- **Comportamiento:** Incremento moderado durante la carga de las imágenes de portada de los álbumes de la colección. Glide gestiona correctamente el caché, manteniendo la memoria estable durante el scroll.

### Red (Network)
- **Peticiones:** Se observan peticiones GET a `/collectors/{id}`, `/collectors/{id}/albums` y `/collectors/{id}/favoritePerformers` al cargar el detalle. Las imágenes de portada se descargan bajo demanda durante el scroll.

### Energía
- **Impacto:** Bajo.

## 3. Evidencia
[Gráfica Profiler](evidencia_grafica.png)

[Archivo .trace de sesión](https://uniandes-my.sharepoint.com/:u:/g/personal/oj_gomezo1_uniandes_edu_co/IQBwy3wk2ZBjQ5D2nwsP8on0AQHpWF_iHybECAAo-9rZ1jw?e=2v6tPf)
