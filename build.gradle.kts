plugins {
    id("com.android.application") version "8.13.2" apply false
    id("org.jetbrains.kotlin.android") version "1.9.22" apply false
    id("org.jetbrains.kotlin.kapt") version "1.9.22" apply false
// File: app/build.gradle.kts
configurations.configureEach {
    exclude(group = "com.android.support")
}
}
