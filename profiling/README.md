# Reporte de Perfilamiento - Vynils App

Este directorio contiene los resultados del perfilamiento de recursos realizado para las Historias de Usuario (HU) principales. El objetivo es demostrar el uso eficiente de CPU, Memoria, Red y Energía.

## Estructura por HU
Cada carpeta contiene:
1. `evidencia_grafica.png`: Captura de pantalla del Android Profiler durante el flujo.
2. `session_data.asprof`: Archivo de sesión exportado desde Android Studio (opcional pero recomendado).
3. `analisis.md`: Documento con los hallazgos técnicos.

## HUs Perfiladas
- [HU01 - Lista de Álbumes](./HU01_ListaAlbumes/analisis.md)
- [HU02 - Detalle de Álbum](./HU02_DetalleAlbum/analisis.md)
- [HU03 - Crear Comentario](./HU03_CrearComentario/analisis.md)
- [HU04 - Detalle de Artista](./HU04_DetalleArtista/analisis.md)
- [HU05 - Listado de Coleccionistas](./HU05_ListadoColeccionistas/analisis.md)
- [HU06 - Detalle de Coleccionista](./HU06_DetalleColeccionista/analisis.md)

## Cómo verificar
1. Los archivos `.asprof` pueden abrirse en Android Studio arrastrándolos a la ventana principal.
2. Los reportes de `analisis.md` detallan los picos de consumo observados.
