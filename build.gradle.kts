// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.sonarqube)
}

sonar {
    properties {
        property("sonar.projectKey", System.getenv("SONAR_PROJECT_KEY") ?: "OscarGomez-MISO_VynilsApp-Equipo10")
        property("sonar.organization", System.getenv("SONAR_ORGANIZATION") ?: "oscargomez-miso")
        property("sonar.host.url", "https://sonarcloud.io")
        // Evitar doble indexación deshabilitando fuentes en la raíz
        property("sonar.sources", "")
    }
}

project(":app") {
    sonar {
        properties {
            val buildDir = project.layout.buildDirectory.get().asFile
            // Ruta absoluta al reporte XML de JaCoCo
            property("sonar.coverage.jacoco.xmlReportPaths", "$buildDir/reports/jacoco/test/jacocoTestReport.xml")
            property("sonar.junit.reportPaths", "$buildDir/test-results/testDebugUnitTest")
            // Indicar dónde están las fuentes del módulo
            property("sonar.sources", "src/main/java")
        }
    }
}
