import fr.pitdev.plugins.JacocoReportsPluginExtension
import fr.pitdev.plugins.LimitType
import fr.pitdev.plugins.LimitRule

plugins {
    alias(projectLibs.plugins.android.application)
    id("org.jetbrains.kotlin.android")
    id("jacoco-reports")
}


/**
 *  set the required unit test coverage in percent,
 *  see [buildSrc/src/main/kotlin/jacoco-reports-precompiled.gradle.kts]
 */
configure<JacocoReportsPluginExtension> {
    limitRules.set(
        listOf(
            LimitRule(LimitType.INSTRUCTION, minimum = 0.8),
            LimitRule(LimitType.BRANCH, minimum = 0.5),
            LimitRule(LimitType.CLASS, minimum = 0.5),
            LimitRule(LimitType.COMPLEXITY, maximum = 0.5),
            LimitRule(LimitType.LINE, minimum = 0.8),
            LimitRule(LimitType.METHOD, minimum = 0.8)
        )
    )
    includes.set(listOf("com.example.tutojacocokts.*"))
}



android {
    namespace = "com.example.tutojacocokts"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.example.tutojacocokts"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.2"
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

    implementation("androidx.core:core-ktx:1.10.0")
    implementation("androidx.activity:activity-compose:1.7.1")
    implementation(platform("androidx.compose:compose-bom:2023.05.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.foundation:foundation")
    implementation("androidx.compose.runtime:runtime")
    implementation("androidx.compose.material:material-icons-extended")
    implementation("androidx.compose.material3:material3-window-size-class")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.1")
    implementation(projectLibs.bundles.androidx.lifecycle)
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.05.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}

tasks.withType<Test> {
    configure<JacocoTaskExtension> {
        isIncludeNoLocationClasses = true
        excludes = listOf("jdk.internal.*")
    }
}
