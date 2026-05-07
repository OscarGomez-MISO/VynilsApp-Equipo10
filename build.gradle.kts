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
        
        // Configuración para el módulo app
        property("sonar.sources", "app/src/main/java")
        property("sonar.tests", "app/src/test/java")
        property("sonar.java.binaries", "app/build/tmp/kotlin-classes/debug")
        property("sonar.kotlin.binaries", "app/build/tmp/kotlin-classes/debug")
        property("sonar.coverage.jacoco.xmlReportPaths", "app/build/reports/jacoco/jacocoTestReport/jacocoTestReport.xml")
        property("sonar.androidLint.reportPaths", "app/build/reports/lint-results-debug.xml")
    }
}
