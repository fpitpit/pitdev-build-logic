pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
    versionCatalogs {
        create("projectLibs") {
            from(files("gradle/pitdev.versions.toml"))
        }
    }
}

rootProject.name = "tutoJacocoKts"
include(":app")
