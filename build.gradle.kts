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
    }
}

project(":app") {
    sonar {
        properties {
            val buildDir = project.layout.buildDirectory.get().asFile

            // Binarios para que JaCoCo funcione (crucial para el 0%)
            property("sonar.java.binaries", "$buildDir/tmp/kotlin-classes/debug")
            property("sonar.kotlin.binaries", "$buildDir/tmp/kotlin-classes/debug")

            // Rutas de reportes (Rutas absolutas para evitar confusiones)
            property("sonar.coverage.jacoco.xmlReportPaths", "$buildDir/reports/jacoco/jacocoTestReport/jacocoTestReport.xml")
            property("sonar.androidLint.reportPaths", "$buildDir/reports/lint-results-debug.xml")
            property("sonar.junit.reportPaths", "$buildDir/test-results/testDebugUnitTest")
        }
    }
}
