# Análisis de Perfilamiento: HU09 - Comentar Álbum

## 1. Descripción del Flujo
El usuario realiza el proceso de registro de una reseña para un álbum:
1. El usuario navega al **Detalle de un Álbum** específico.
2. Como **Coleccionista**, presiona el botón de acción para agregar un comentario.
3. Se despliega un formulario donde se ingresa: Calificación (Rating), Descripción y Correo electrónico.
4. Al presionar "Enviar", la aplicación gestiona la identificación del coleccionista y realiza la petición POST.
5. Se visualiza un mensaje de éxito y el formulario se cierra automáticamente, refrescando la vista del detalle.

## 2. Resultados del Profiler (Hallazgos Observados)

### CPU
- **Uso:** Se detecta actividad en el hilo principal durante la apertura del `ModalBottomSheet`. El procesamiento de la lógica de validación en el `CommentViewModel` es eficiente y no bloquea la interfaz.
- **Trace:** El archivo de traza adjunto muestra la ejecución de las corrutinas responsables de la comunicación con el API de comentarios.

### Memoria
- **Comportamiento:** El uso de memoria es óptimo. La creación del `CommentViewModel` mediante una Factory personalizada no genera fugas de memoria, y el estado se reinicia correctamente tras completar la operación.

### Red (Network)
- **Peticiones:** Se registra una transferencia de datos (POST) dirigida al endpoint `albums/{id}/comments`. El monitoreo muestra que la carga de red es mínima y el tiempo de respuesta del servidor es el factor determinante en la duración del estado de carga.

### Energía
- **Impacto:** Bajo. El uso de recursos de red es puntual y breve.

## 3. Evidencia
[Gráfica Profiler](evidencia_grafica.png)

[Archivo.trace](https://uniandes-my.sharepoint.com/:u:/g/personal/oj_gomezo1_uniandes_edu_co/IQBFe23J9D5eQZyM1WksghqxAbQoD3Io-UWwAd4Va2EEexk?e=CpbYyW)
