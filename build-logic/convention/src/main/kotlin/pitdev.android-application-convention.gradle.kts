import fr.pitdev.config.ProjectConfig
import fr.pitdev.config.libs
private val libs = project.libs

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("pitdev.android-jacoco-convention")
}

android {
    compileSdk = libs.findVersion("compileSdk").get().toString().toInt()

    defaultConfig {
        minSdk = libs.findVersion("minSdk").get().toString().toInt()
        targetSdk = libs.findVersion("targetSdk").get().toString().toInt()
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        testInstrumentationRunnerArguments["runnerBuilder"] = "de.mannodermaus.junit5.AndroidJUnit5Builder" // Use JUnit 5 for local unit tests

        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {

        debug {
            enableUnitTestCoverage = true
            enableAndroidTestCoverage = true
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

    testOptions {
        animationsDisabled = true
        unitTests {
            isReturnDefaultValues = true
            isIncludeAndroidResources = true

        }
    }
    buildFeatures {
        buildConfig = true
    }

    packaging {
        resources {
            excludes.addAll(
                listOf(
                    "META-INF/LICENSE.md",
                    "META-INF/LICENSE-notice.md"
                )
            )
        }
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
    }
}





dependencies {

    implementation(libs.findLibrary("androidx.core.ktx").get())
    implementation(libs.findBundle("androidx.lifecycle").get())
    implementation(libs.findLibrary("timber").get())

    testImplementation(libs.findLibrary("jetbrains.kotlinx.coroutines.test").get())

    testImplementation(libs.findLibrary("junit").get())
    testImplementation(platform(libs.findLibrary("junit5Bom").get()))

    testImplementation(libs.findLibrary("junit5").get())
    testRuntimeOnly(libs.findLibrary("junit.platform.launcher").get())
    testRuntimeOnly(libs.findLibrary("junit5.vintage").get())

    testImplementation(libs.findLibrary("mockk.android").get())
    testImplementation(libs.findLibrary("mockk.agent").get())

    testImplementation(libs.findLibrary("robolectric").get())

    androidTestImplementation(libs.findLibrary("androidx.test.ext.junit").get())
    androidTestImplementation(libs.findLibrary("androidx.test.espresso.core").get())

    androidTestImplementation(libs.findLibrary("androidx.test.ext.junit").get())
    androidTestImplementation(libs.findLibrary("androidx.test.espresso.core").get())

    androidTestImplementation(libs.findLibrary("mockk.android").get())
    androidTestImplementation(libs.findLibrary("mockk.agent").get())



}
