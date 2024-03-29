plugins {
    id("pitdev.android-library-compose-convention")
}

android {
    namespace = "fr.pitdev.log"

    flavorDimensions += "environment"
    productFlavors {
        create("dev") {
            dimension = "environment"
        }

        create("preprod") {
            dimension = "environment"
        }

        create("production") {
            dimension = "environment"
        }
    }

}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.google.android.material)

}
