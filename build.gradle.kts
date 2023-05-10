// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(projectLibs.plugins.kotlin.android) apply false
    alias(projectLibs.plugins.android.application) apply false
    alias(projectLibs.plugins.android.library) apply false
}

buildscript {
    repositories {
        mavenCentral()
        google()
    }
    dependencies {
        //classpath(Config.Dependencies.androidTools)
        classpath(projectLibs.android.tools.build.gradle)
        classpath(kotlin("gradle-plugin", version = "1.4.32"))
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}



