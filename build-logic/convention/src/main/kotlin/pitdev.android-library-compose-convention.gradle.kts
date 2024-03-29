import fr.pitdev.config.libs

private val libs = project.libs

plugins {
    id("pitdev.android-library-convention")
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

    buildFeatures {
        buildConfig = true
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
