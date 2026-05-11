# Análisis de Perfilamiento: HU07 - Crear Álbum

## 1. Descripción del Flujo
Se realizó el seguimiento del flujo de creación de un nuevo recurso:
1. El usuario ingresa con el rol de **Coleccionista**.
2. Desde la pantalla de álbumes, se presiona el **Botón Flotante (FAB)** para acceder al formulario de creación.
3. Se diligencian los campos del formulario (Nombre, Portada, Fecha, Descripción, Género y Sello).
4. Se presiona el botón **"Crear Álbum"**.
5. Se visualiza la pantalla de éxito y se espera el redireccionamiento automático al listado.

## 2. Resultados del Profiler (Hallazgos Observados)

### CPU
- **Uso:** Se observa un pico de actividad al momento de realizar el POST al servidor y al procesar el `DatePickerDialog`. El hilo principal se mantiene fluido durante la navegación.
- **Trace:** El archivo de traza capturado refleja el ciclo de vida del `CreateAlbumViewModel` y las corrutinas de red.

### Memoria
- **Comportamiento:** La memoria aumenta levemente durante la apertura del formulario debido a la carga de componentes de Compose. Al completar la creación, el estado del formulario se limpia, liberando las referencias a los strings temporales en el `AlbumFormState`.

### Red (Network)
- **Peticiones:** Se registra una petición de salida (POST) al endpoint `/albums`. El tamaño del payload es pequeño y la respuesta se recibe en tiempos óptimos.

### Energía
- **Impacto:** Bajo. No se detectan fugas de energía ni procesos pesados en segundo plano tras cerrar el formulario.

## 3. Evidencia
[Gráfica Profiler](evidencia_grafica.png)

##
[Archivo.trace](https://uniandes-my.sharepoint.com/:u:/g/personal/oj_gomezo1_uniandes_edu_co/IQAhBU1YhJfuT5b9sa16qbIFAXVXEL6_AQXo_2dXBbb5MUU?e=Cd4XXx)
