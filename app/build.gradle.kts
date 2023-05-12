import fr.pitdev.config.ProjectConfig

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("jacoco-reports")
}

android {
    namespace = "com.example.tutojacocokts"
    compileSdk = ProjectConfig.AndroidConfig.compileSdk

    defaultConfig {
        applicationId = "com.example.tutojacocokts"
        minSdk = ProjectConfig.AndroidConfig.minSdk
        targetSdk = ProjectConfig.AndroidConfig.targetSdk
        versionCode = ProjectConfig.AndroidConfig.versionCode
        versionName = ProjectConfig.AndroidConfig.versionName

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        debug {
            enableUnitTestCoverage = true

        }
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = ProjectConfig.AndroidConfig.javaVersion
        targetCompatibility = ProjectConfig.AndroidConfig.javaVersion
    }
    kotlinOptions {
        jvmTarget = ProjectConfig.AndroidConfig.kotlinJvmVersion
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.composeKotlinCompilerExtensionVersion.get()
    }
    packaging {
        resources {
            excludes += listOf("/META-INF/{AL2.0,LGPL2.1}")
        }
    }

    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }

}

dependencies {

    implementation(libs.androidx.core.ktx)

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.bundles.androidx.compose)

    implementation(libs.bundles.androidx.lifecycle)

    testImplementation(libs.junit)

    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.androidx.test.espresso.core)

    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)

    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}

