plugins {
    `kotlin-dsl`
}

repositories {
    google()
    mavenCentral()
    gradlePluginPortal() // so that external plugins can be resolved in dependencies section
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

dependencies {
    implementation(libs.android.tools.build.gradle.api)
    gradleApi()
}

