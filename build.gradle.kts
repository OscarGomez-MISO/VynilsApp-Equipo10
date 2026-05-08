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
            // Binarios para que JaCoCo funcione
            property("sonar.java.binaries", "build/intermediates/javac/debug/classes,build/tmp/kotlin-classes/debug")

            // Reportes
            property("sonar.coverage.jacoco.xmlReportPaths", "build/reports/jacoco/jacocoTestReport/jacocoTestReport.xml")
            property("sonar.junit.reportPaths", "build/test-results/testDebugUnitTest")
            
            // Forzar inclusiones para debugging si es necesario
            property("sonar.sources", "src/main/java")
            property("sonar.tests", "src/test/java")
            property("sonar.sourceEncoding", "UTF-8")
        }
    }
}
