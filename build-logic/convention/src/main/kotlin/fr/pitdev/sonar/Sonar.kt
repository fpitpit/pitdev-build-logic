
import gradle.kotlin.dsl.accessors._f94fc6c8215be330ed97c169cddc9497.sonar
import gradle.kotlin.dsl.accessors._f94fc6c8215be330ed97c169cddc9497.sonarqube
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension

internal fun Project.configureSonar() {
    val libs = extensions.getByType(VersionCatalogsExtension::class.java).named("libs")

    sonar {
        properties {
            //val coverageExcludeFiles = rootProject.extra.get("coverageExcludeFiles") as List<*>
            property("sonar.projectKey", "")
            property("sonar.host.url", "https://sonarcloud.io")
            property("sonar.login", System.getenv("SONAR_TOKEN"))
            property("sonar.core.codeCoveragePlugin", "jacoco")
            property(
                "sonar.coverage.jacoco.xmlReportPaths",
                "${project.buildDir}/reports/jacoco/jacocoTestReport/jacocoTestReport.xml"
            )
            //property(
            //    "sonar.coverage.exclusions",
            //    coverageExcludeFiles.joinToString(separator = ",")
            //)
        }
    }
    subprojects {
        sonarqube {
            properties {
                property("sonar.sources", "src")
            }
        }
    }
}