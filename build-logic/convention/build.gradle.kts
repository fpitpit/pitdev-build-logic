plugins {
    `kotlin-dsl`
    `maven-publish`
    `version-catalog`
}

publishing {
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/fpitpit/pitdev-build-logic")
            credentials {
                username = project.findProperty("gpr.user") as String? ?: System.getProperty("github.username")
                password = project.findProperty("gpr.key") as String? ?: System.getProperty("github.token")
            }
        }
    }
    publications {
        register<MavenPublication>(project.name) {
            artifactId = project.name
            logger.quiet("group = $group")
            logger.quiet("artifactId = $artifactId")
            logger.quiet("version = $version")

            from(components["java"])
        }
    }
    publications {
        register<MavenPublication>("PitDevVersionCatalog") {
            artifactId = "version-catalog"
            logger.quiet("group = $group")
            logger.quiet("artifactId = $artifactId")
            logger.quiet("version = $version")

            artifact("../../gradle/pitdev.versions.toml")
        }
    }
}


dependencies {
    api(libs.jetbrains.kotlin.gradle.plugin)
    api(libs.android.tools.build.gradle)
    gradleApi()
}

gradlePlugin {
    // register JacocoReportsPlugin as a plugin
    plugins {
        register("jacoco-reports") {
            id = "jacoco-reports"
            implementationClass = "fr.pitdev.plugins.JacocoReportsPlugin"
        }
    }
}


