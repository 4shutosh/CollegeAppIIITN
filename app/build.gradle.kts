plugins {
    id("com.android.application")
    id("dagger.hilt.android.plugin")

    kotlin("android")
    id("kotlin-parcelize")
    kotlin("kapt")
}

android {
    compileSdkVersion(Versions.COMPILE_SDK)

    defaultConfig {
        applicationId = "com.college.app"
        minSdkVersion(Versions.MIN_SDK)
        targetSdkVersion(Versions.TARGET_SDK)
        versionCode = 1
        versionName = "1.0"

//        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
            minifyEnabled(true)
//            proguardFiles getDefaultProguardFile ('proguard-android-optimize.txt'), 'proguard-rules.pro'
        }
        getByName("debug") {
            debuggable(true)
            minifyEnabled(false)
        }
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildFeatures {
        dataBinding = true
    }

}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    implementation(project(":base"))

    implementation(libs.hilt.library)
    kapt(libs.hilt.compiler)

    implementation(libs.timber)
    implementation(libs.firebase.crashlytics)

    implementation(libs.kotlin.stdlib)

    implementation(libs.androidx.core)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.recyclerview)
    implementation(libs.androidx.recyclerview.selection)
    implementation(libs.androidx.startup)
//    implementation(libs.androidx.core.splash)

    implementation(libs.google.material)

    implementation(libs.firebase.auth)
    implementation(libs.firebase.gmsauth)
    implementation(libs.firebase.database)
    implementation(libs.firebase.storage)
    implementation(libs.firebase.messaging)

    implementation(libs.picasso)

    implementation("com.facebook.shimmer:shimmer:0.1.0@aar")
    implementation(libs.swipeRefresh)

    implementation(libs.circularview)
}

if (file("google-services.json").exists()) {
    apply { plugin("com.google.gms.google-services") }
    apply { plugin("com.google.firebase.crashlytics") }
}
