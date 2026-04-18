# VynilsApp-Equipo10
Repositorio para el proyecto móvil de la materia ingeniería de software para aplicaciones móviles

## Arquitectura del Proyecto

Este proyecto sigue el patrón de arquitectura **MVVM (Model-View-ViewModel)** junto con el patrón **Repository** y un **Service Adapter** para la comunicación con el API REST.

### Componentes:

1.  **Models (`models/`)**: Contiene las clases de datos (Data Classes) que representan las entidades del dominio (ej. Album, Artist, Collector).
2.  **Service Adapter (`network/`)**: Encargado de la comunicación con el API REST alojada en Heroku. Utiliza Retrofit para realizar las peticiones HTTP.
3.  **Repository (`repositories/`)**: Actúa como una capa de abstracción sobre las fuentes de datos. Su responsabilidad es decidir de dónde obtener los datos (Red o Caché Local) y proveerlos al ViewModel.
4.  **ViewModel (`viewmodels/`)**: Contiene la lógica de negocio relacionada con la UI y mantiene el estado de la vista. Se comunica con el repositorio para obtener datos y los expone mediante `LiveData` o `Flow`.
5.  **UI (`ui/`)**: Contiene las Activities y Fragments. Su única responsabilidad es observar los datos del ViewModel y pintarlos en la pantalla.

### Tecnologías Principales:
- **Retrofit**: Para el consumo de servicios REST.
- **Kotlin Coroutines**: Para manejo de tareas asíncronas.
- **Lifecycle (ViewModel & LiveData)**: Para la gestión del ciclo de vida y reactividad de la UI.
- **Material Design**: Para los componentes de interfaz de usuario.

### API Base URL:
`https://back-vynils-equipo10-c7f5ef54eafe.herokuapp.com/`
