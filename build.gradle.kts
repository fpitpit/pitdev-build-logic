// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
}

buildscript {
    repositories {
        mavenCentral()
        google()
    }
    dependencies {
        classpath(libs.android.tools.build.gradle)
        classpath(libs.jetbrains.kotlin.gradle.plugin)
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}



