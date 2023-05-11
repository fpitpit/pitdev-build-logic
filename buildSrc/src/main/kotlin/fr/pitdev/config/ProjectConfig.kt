package fr.pitdev.config

import org.gradle.api.JavaVersion

object ProjectConfig {

    object AndroidConfig {
        const val compileSdk = 33
        const val minSdk = 24
        const val targetSdk = 33
        const val versionCode = 1
        const val versionName = "1.0.0"
        val javaVersion: JavaVersion = JavaVersion.VERSION_17
        val kotlinJvmVersion = "17"

    }
}