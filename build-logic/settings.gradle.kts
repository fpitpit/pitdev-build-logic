
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
    versionCatalogs {
        create("libs") {
            from(files("../gradle/pitdev.versions.toml"))
        }
    }
}
rootProject.name = "build-logic"
include(":convention")
