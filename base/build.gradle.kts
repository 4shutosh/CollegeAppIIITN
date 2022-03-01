plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
}

android {
    compileSdkVersion(Versions.COMPILE_SDK)

    kotlinOptions {
        jvmTarget = "1.8"
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    implementation(libs.androidx.core)
    implementation(libs.kotlin.stdlib)
    implementation(libs.kotlin.coroutines.core)
    implementation(libs.kotlin.coroutines.android)
    implementation(libs.google.material)

    implementation(libs.timber)
    implementation(libs.firebase.crashlytics)

    implementation(libs.moshi)
    implementation(libs.moshi.converter)
    kapt(libs.moshi.compiler)
    implementation(libs.moshi.kotlin)
    implementation(libs.kotlin.reflect)
}