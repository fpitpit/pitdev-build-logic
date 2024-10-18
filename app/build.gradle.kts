import fr.pitdev.config.ProjectConfig

val buildVersionCode: String by project
val version: String by project

plugins {
    id("pitdev.android-compose-convention")
}

android {
    namespace = "com.example.tutojacocokts"

    defaultConfig {
        versionCode = buildVersionCode.toInt()
        versionName = version.toString()
        applicationId = "com.example.tutojacocokts"
        testApplicationId = "$applicationId.test"
    }

    flavorDimensions += "environment"
    productFlavors {
        create("dev") {
            dimension = "environment"
            applicationIdSuffix = ".dev"
        }

        create("preprod") {
            dimension = "environment"
            applicationIdSuffix = ".preprod"
        }

        create("production") {
            dimension = "environment"
        }
    }




}

dependencies {
    implementation(project(":log"))
}
