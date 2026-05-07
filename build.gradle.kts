// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.sonarqube) apply false
}

sonar {
    properties {
        property("sonar.projectKey", System.getenv("SONAR_PROJECT_KEY") ?: "OscarGomez-MISO_VynilsApp-Equipo10")
        property("sonar.organization", System.getenv("SONAR_ORGANIZATION") ?: "oscargomez-miso")
        property("sonar.host.url", "https://sonarcloud.io")
        property("sonar.coverage.jacoco.xmlReportPaths", "${project.rootDir}/app/build/reports/jacoco/jacocoTestReport/jacocoTestReport.xml")
        property("sonar.kotlin.binaries", "${project.rootDir}/app/build/tmp/kotlin-classes/debug")
    }
}
