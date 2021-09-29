plugins {
    id("com.android.application")
    id("dagger.hilt.android.plugin")

    kotlin("android")
    kotlin("android.extensions")
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
        viewBinding = true
    }

}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)

    //recycler View
    implementation(libs.androidx.recyclerview)
    implementation(libs.androidx.recyclerview.selection)

    // material
    implementation(libs.google.material)

    // firebase
    implementation(libs.firebase.auth)
    implementation(libs.firebase.gmsauth)
    implementation(libs.firebase.database)
    implementation(libs.firebase.storage)
    implementation(libs.firebase.messaging)

    //picasso
    implementation(libs.picasso)

    implementation(libs.androidx.core)
    implementation(libs.kotlin.stdlib)

    implementation(libs.shimmer)
    implementation(libs.swipeRefresh)

    //circular view
    implementation(libs.circularview)

    // hilt
    implementation(libs.hilt.library)
    kapt(libs.hilt.compiler)
}

if (file("google-services.json").exists()) {
    apply { plugin("com.google.gms.google-services") }
//    apply { plugin("com.google.firebase.crashlytics") } todo setup crashlytics
}
