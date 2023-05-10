plugins {
    `kotlin-dsl`
    //`kotlin-dsl-precompiled-script-plugins`
}

repositories {
    google()
    mavenCentral()
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
    implementation("com.android.tools.build:gradle-api:8.0.1")
    implementation(kotlin("stdlib"))
    gradleApi()
}

