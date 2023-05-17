package fr.pitdev.config

import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.getByType

val Project.libs: VersionCatalog
    get() = extensions.getByType(VersionCatalogsExtension::class.java).named("libs")

object ProjectConfig {

    object AndroidConfig {
        const val compileSdk = 33
        const val minSdk = 24
        const val targetSdk = 33
        val javaVersion: JavaVersion = JavaVersion.VERSION_17
        val kotlinJvmVersion = javaVersion.toString()

    }
}