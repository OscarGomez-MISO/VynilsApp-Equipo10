# Análisis de Perfilamiento: HU03 - Crear Comentario

## 1. Descripción del Flujo
El usuario realiza el proceso de registro de una reseña para un álbum:
1. Desde el **Detalle del Álbum**, el usuario abre el formulario para agregar un comentario.
2. Ingresa los datos: Calificación (Rating), Descripción y su Correo electrónico.
3. Al presionar "Enviar", la aplicación valida la sesión del coleccionista (o lo registra si es necesario) y realiza una petición POST al servidor.
4. El usuario recibe una confirmación visual de éxito y la interfaz se actualiza.

## 2. Resultados del Profiler (Hallazgos Observados)

### CPU
- **Uso:** Actividad concentrada en el manejo de la validación del formulario y la actualización del estado de la UI (CommentUiState). Se observa actividad mínima durante la espera de la respuesta del servidor (I/O Wait).
- **Trace:** El archivo `.trace` documenta el ciclo de vida desde el clic en enviar hasta el cierre del BottomSheet de comentarios.

### Memoria
- **Comportamiento:** Se mantiene estable. No hay picos significativos de memoria, lo que indica que los objetos de solicitud (Request objects) se liberan correctamente después de la ejecución del ViewModelScope.

### Red (Network)
- **Peticiones:** Es el punto crítico de esta HU. Se observa actividad de "Sending" (subida) al enviar el cuerpo del comentario en formato JSON al endpoint `/albums/{id}/comments`. El tiempo de respuesta del backend se refleja en la duración del estado de "Loading" en la gráfica.

### Energía
- **Impacto:** Bajo.

## 3. Evidencia
[Gráfica Profiler](evidencia_grafica.png)

[Archivo .trace de sesión](https://uniandes-my.sharepoint.com/:u:/g/personal/oj_gomezo1_uniandes_edu_co/IQAU82AJa-wDRqULXkI4WhciAdcVMD_ZEie7AMxi6GMH3d4?e=1fNG8x)
