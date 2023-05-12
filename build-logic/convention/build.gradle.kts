plugins {
    `kotlin-dsl`
}

group = "fr.pitdev.tutojacocokts.buildlogic"

repositories {
    google()
    mavenCentral()
    gradlePluginPortal() // so that external plugins can be resolved in dependencies section
}

dependencies {
    api(libs.android.tools.build.gradle.api)
    api(libs.jetbrains.kotlin.gradle.plugin)
    api("com.android.tools.build:gradle:8.0.1")
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


