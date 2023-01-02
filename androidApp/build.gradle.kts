plugins {
    id("com.android.application")
    kotlin("android")
}

android {
    namespace = "com.sterndu.pingonvolcano.android"
    compileSdk = 32
    defaultConfig {
        applicationId = "com.sterndu.pingonvolcano.android"
        minSdk = 21
        targetSdk = 32
        versionCode = 1
        versionName = "1.0"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.3.0"
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
}

dependencies {

    val nav_version = "2.5.3"

    implementation(project(":shared"))
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.5.1")
    implementation("androidx.compose.ui:ui:1.2.1")
    implementation("androidx.compose.ui:ui-tooling:1.2.1")
    implementation("androidx.compose.ui:ui-tooling-preview:1.2.1")
    implementation("androidx.compose.foundation:foundation:1.2.1")
    implementation("androidx.compose.material:material:1.2.1")
    implementation("androidx.activity:activity-compose:1.5.1")
    implementation("androidx.navigation:navigation-compose:$nav_version")
    implementation("androidx.emoji:emoji:1.0.0")
    implementation("com.google.firebase:protolite-well-known-types:18.0.0")
    implementation("androidx.appcompat:appcompat:1.4.1")
    implementation("com.google.android.material:material:1.4.+")
    implementation("androidx.constraintlayout:constraintlayout:2.1.3")

}