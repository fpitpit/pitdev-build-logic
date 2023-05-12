import fr.pitdev.config.ProjectConfig

plugins {
    id("android-application-convention")
    id("android-application-jacoco-convention")
    id("android-compose-convention")
}

android {
    namespace = "com.example.tutojacocokts"

    defaultConfig {
        applicationId = "com.example.tutojacocokts"
    }
}
