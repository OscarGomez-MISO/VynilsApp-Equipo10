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
            // Ruta absoluta al reporte XML de JaCoCo
            property("sonar.coverage.jacoco.xmlReportPaths", "${rootDir}/app/build/reports/jacoco/test/jacocoTestReport.xml")
            property("sonar.junit.reportPaths", "${rootDir}/app/build/test-results/testDebugUnitTest")
            
            // Forzar binarios para el mapeo
            property("sonar.java.binaries", "build/intermediates/javac/debug/classes,build/tmp/kotlin-classes/debug")
        }
    }
}
