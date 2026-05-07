# Vinilos - Equipo 10

Repositorio para el proyecto móvil de la materia Ingeniería de Software para Aplicaciones Móviles. Esta aplicación permite a los usuarios explorar álbumes, artistas y coleccionistas de vinilos, con una experiencia curada para amantes de la música.

## Requisitos Previos (Local)

Para ejecutar este proyecto en tu máquina local sin problemas, asegúrate de contar con lo siguiente:

1.  **Android Studio**: Versión **Ladybug (2024.2.1)** o superior.
2.  **JDK**: Versión **17** (requerida por el plugin de Gradle).
3.  **Android SDK**:
    *   **Compile SDK**: 35
    *   **Target SDK**: 35
    *   **Min SDK**: 21 (Soporta desde Android 5.0 Lollipop)
4.  **Gradle**: Versión **9.3.1** o superior.
5.  **Internet**: Necesario para la descarga de dependencias y el consumo de la API en Heroku (https://back-vynils-equipo10-c7f5ef54eafe.herokuapp.com/)

## Especificaciones Técnicas y Versiones

| Componente | Versión |
| :--- | :--- |
| **Android Gradle Plugin (AGP)** | 9.1.1 |
| **Kotlin** | 2.0.21 |
| **Jetpack Compose (BOM)** | 2024.12.01 |
| **Retrofit** | 2.11.0 |
| **Glide (Compose)** | 1.0.0-beta01 |

## Arquitectura del Proyecto

El proyecto sigue una arquitectura moderna de Android basada en **Clean Architecture** y el patrón **MVVM (Model-View-ViewModel)**.

### Capas:

1.  **Domain (`domain/`)**: Contiene los modelos de datos puros y lógica de negocio.
2.  **Data (`data/`)**: Implementa el patrón **Repository** y el **Service Adapter** (Retrofit) para gestionar la fuente de verdad de los datos (Remota en Heroku).
3.  **UI (`ui/`)**: Desarrollada íntegramente en **Jetpack Compose**.
    *   **ViewModels**: Gestionan el estado de la UI y se comunican con el repositorio.
    *   **Screens**: Componibles que representan las diferentes pantallas (Álbumes, Detalle de Álbum, Artistas, Detalle de Artista/Banda, Coleccionistas).

## Instalación y Ejecución

1.  Clona el repositorio:
    ```bash
    git clone https://github.com/[usuario]/VynilsApp-Equipo10.git
    ```
2.  Abre el proyecto en Android Studio.
3.  Deja que Gradle sincronice las dependencias (Sync Project with Gradle Files).
4.  Ejecuta la aplicación en un emulador o dispositivo físico (API 21+).

## API Base URL
Actualmente el proyecto consume los datos de:
`https://back-vynils-equipo10-c7f5ef54eafe.herokuapp.com/`

## Pruebas E2E (End-to-End)

El proyecto cuenta con escenarios de prueba automatizados utilizando **Espresso** y **Compose Test Library**. Estas pruebas validan los flujos críticos de la aplicación.

### Escenarios de Prueba:

1.  **Flujo de Coleccionista**: 
    *   **Escenario**: El usuario entra como coleccionista, visualiza la lista de álbumes, selecciona uno y ve su detalle.
    *   **Validación**: Presencia del botón "Agregar (+)", carga de datos desde el API y navegación correcta al detalle y retorno.
2.  **Modo Visitante**:
    *   **Escenario**: El usuario entra como visitante para explorar.
    *   **Validación**: El botón "Agregar (+)" debe estar oculto (modo lectura).
3.  **Navegación de Pestañas**:
    *   **Escenario**: El usuario navega a la pestaña de "Artistas" y "Coleccionistas".
    *   **Validación**: En Artistas se muestra la lista real de artistas con el encabezado "BIBLIOTECA CURADA". En Coleccionistas se muestra la lista real con el encabezado "SOCIEDAD DE BUSCADORES DE JOYAS".
4.  **Detalle de Artista (HU04)**:
    *   **Escenario**: El usuario selecciona un artista del listado y visualiza su detalle.
    *   **Validación**: Se muestra nombre, fecha de nacimiento y descripción del artista, así como la lista de álbumes asociados. Al tocar un álbum navega al detalle del álbum. El botón de regreso vuelve al listado de artistas.
5.  **Listado de Coleccionistas (HU05)**:
    *   **Escenario**: El usuario navega a la pestaña "Coleccionistas".
    *   **Validación**: Se muestra el encabezado "SOCIEDAD DE BUSCADORES DE JOYAS", la lista de coleccionistas con nombre y cantidad de álbumes, y una barra de búsqueda funcional. Si no hay coleccionistas, se muestra un mensaje informativo.
    *   **Archivos**: `CollectorsListTest.kt` (6 tests HU05)
6.  **Flujo de Salida (Logout)**:
    *   **Escenario**: El usuario está en la pantalla principal y decide volver a la selección de rol.
    *   **Validación**: Al pulsar el botón "Home" en la cabecera, debe regresar a la pantalla de bienvenida.

### Ejecución de Pruebas:

Para ejecutar las pruebas instrumentadas (E2E), asegúrate de tener un emulador o dispositivo físico conectado y desbloqueado.

**Desde la terminal:**
```bash
./gradlew connectedAndroidTest
```

**Desde Android Studio:**
Haz clic derecho en la carpeta `app/src/androidTest` y selecciona la opción **Run 'Tests in com.example...'**.

### Resultados de las Pruebas:

Una vez finalizada la ejecución desde la terminal, Gradle genera un reporte detallado en formato HTML. Puedes encontrarlo en la siguiente ruta:

`app/build/reports/androidTests/connected/index.html`

Para visualizarlo, abre el archivo `index.html` en cualquier navegador web. Allí verás el resumen de éxitos, fallos y el tiempo de ejecución de cada escenario.

## Análisis de Calidad y Cobertura (SonarCloud)

El proyecto está integrado con **SonarCloud** para garantizar la calidad del código y monitorear la cobertura de las pruebas unitarias.

### Herramientas y Reportes:
*   **JaCoCo**: Se utiliza para medir la cobertura del código. Genera reportes en formato XML que son consumidos por SonarCloud.
*   **SonarScanner**: Analiza el código en busca de bugs, vulnerabilidades y code smells.

### Generación de Cobertura Local:
Si deseas generar el reporte de cobertura manualmente en tu máquina, ejecuta:
```bash
./gradlew jacocoTestReport
```
Al finalizar, podrás encontrar el reporte detallado en:
`app/build/reports/jacoco/jacocoTestReport/html/index.html`

### Integración Continua (CI):
El análisis se realiza automáticamente mediante GitHub Actions (`sonar.yml`) bajo las siguientes condiciones:
1.  Cada vez que se realiza un **Push** a la rama `main`.
2.  Al abrir o actualizar un **Pull Request** hacia `main`.

El pipeline se encarga de:
1.  Ejecutar las pruebas unitarias.
2.  Generar el reporte de cobertura con JaCoCo.
3.  Subir los resultados a SonarCloud usando las credenciales seguras (`SONAR_TOKEN`, `SONAR_PROJECT_KEY`, `SONAR_ORGANIZATION`) configuradas en el repositorio.

## Generación del APK

Si necesitas generar o actualizar el instalador (APK) de la aplicación para pruebas en dispositivos reales, sigue estos pasos:

### 1. Generar el APK
Ejecuta el siguiente comando en la terminal desde la raíz del proyecto:
```bash
./gradlew assembleDebug
```

### 2. Localización del instalador
Una vez finalizado el build, el archivo APK generado se encontrará en:
`app/build/outputs/apk/debug/app-debug.apk`

### 3. Carpeta de Distribución
Para facilitar el acceso, hemos habilitado la carpeta llamada `/release` en la raíz del repositorio donde se encuentran las versiones listas para instalar. La versión más reciente es:
`release/vynils-app-v1.0.1.apk`
