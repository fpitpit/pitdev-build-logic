import fr.pitdev.config.ProjectConfig

plugins {
    id("pitdev.android-application-convention")
    id("pitdev.android-compose-convention")
    id("pitdev.android-jacoco-convention")
}

android {
    namespace = "com.example.tutojacocokts"

    defaultConfig {
        applicationId = "com.example.tutojacocokts"
    }
}
