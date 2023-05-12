import fr.pitdev.config.ProjectConfig
import fr.pitdev.config.libs

private val libs = project.libs

plugins {
    id("android-application-convention")
}

android {

    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.findVersion("composeKotlinCompilerExtensionVersion").get().toString()
    }
    packaging {
        resources {
            excludes += listOf("/META-INF/{AL2.0,LGPL2.1}")
        }
    }
}

dependencies {
    implementation(platform(libs.findLibrary("androidx.compose.bom").get()))
    implementation(libs.findBundle("androidx.compose").get())

    testImplementation(libs.findLibrary("androidx.compose.ui.test.junit4").get())

    androidTestImplementation(platform(libs.findLibrary("androidx.compose.bom").get()))
    androidTestImplementation(libs.findLibrary("androidx.compose.ui.test.junit4").get())

    debugImplementation(libs.findLibrary("androidx.compose.ui.tooling").get())
    debugImplementation(libs.findLibrary("androidx.compose.ui.test.manifest").get())

}
