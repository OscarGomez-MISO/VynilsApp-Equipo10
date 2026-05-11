# Análisis de Perfilamiento: HU05 - Listado de Coleccionistas

## 1. Descripción del Flujo
El usuario realiza el siguiente flujo de navegación:
1. El usuario navega a la pestaña de **Coleccionistas**.
2. Se carga la lista completa de coleccionistas desde el backend.
3. El usuario realiza **scroll** para visualizar todos los coleccionistas disponibles.
4. El usuario utiliza la **barra de búsqueda** para filtrar coleccionistas por nombre.
5. El usuario limpia la búsqueda y regresa al Home.

## 2. Resultados del Profiler (Hallazgos Observados)

### CPU
- **Uso:** Pico moderado durante la carga inicial de la lista y el parseo del JSON de coleccionistas. Durante el filtrado por búsqueda se observa actividad leve relacionada con el procesamiento del texto ingresado.
- **Trace:** El archivo `.trace` muestra la actividad de los hilos durante la carga y el renderizado de la lista.

### Memoria
- **Comportamiento:** Incremento controlado al cargar la lista completa de coleccionistas. La memoria se mantiene estable durante el scroll, lo que indica una correcta gestión del reciclaje de vistas en el listado.

### Red (Network)
- **Peticiones:** Se observa una única petición GET al endpoint `/collectors` al ingresar a la pestaña. No se realizan peticiones adicionales durante el scroll ni el filtrado (filtrado local).

### Energía
- **Impacto:** Bajo.

## 3. Evidencia
[Gráfica Profiler](evidencia_grafica.png)

[Archivo .trace de sesión](https://uniandes-my.sharepoint.com/:u:/g/personal/oj_gomezo1_uniandes_edu_co/IQAExGBsElrQRIDiKLHvvLIoAQIkWb-QLvBKh6zAj7BjtfE?e=vtmP6q)
