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


