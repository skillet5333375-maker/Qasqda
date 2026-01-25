plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.kapt")
}

android {
    namespace = "com.example.medalgorithms"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.medalgorithms"
        minSdk = 21
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.10"
    }

    
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
kotlinOptions {
        jvmTarget = "17"
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    // AndroidX replacement for old support-v4 (helps legacy libs)
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    // Compose
    implementation(platform("androidx.compose:compose-bom:2024.02.02"))
    implementation("androidx.activity:activity-compose:1.8.2")
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3:1.2.1")
    debugImplementation("androidx.compose.ui:ui-tooling")

    // Navigation
    implementation("androidx.navigation:navigation-compose:2.7.7")

    // Lifecycle / ViewModel
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")

    // Room
    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    kapt("androidx.room:room-compiler:2.6.1")

    // PDF Viewer (View-based)
    implementation("com.github.mhiew:android-pdf-viewer:3.2.0-beta.1")


    // PDF text extraction for search
    implementation("com.tom-roush:pdfbox-android:2.0.27.0")
}

kapt {
    correctErrorTypes = true
}

/**
 * Drop old com.android.support:* artifacts. They frequently break manifest merging in AndroidX projects.
 */
configurations.configureEach {
    exclude(group = "com.android.support")
dependencies {
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
}

configurations.configureEach {
    exclude(group = "com.android.support")
}

}
