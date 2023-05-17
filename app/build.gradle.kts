import fr.pitdev.config.ProjectConfig
val buildVersionCode: String by project
val version: String by project

plugins {
    id("pitdev.android-application-convention")
    id("pitdev.android-compose-convention")
    id("pitdev.android-jacoco-convention")
}

android {
    namespace = "com.example.tutojacocokts"

    defaultConfig {
        versionCode = buildVersionCode.toInt()
        versionName = version
        applicationId = "com.example.tutojacocokts"
    }
}
