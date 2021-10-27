plugins {
    id("com.android.application")
    id("dagger.hilt.android.plugin")

    kotlin("android")
    id("kotlin-parcelize")
    kotlin("kapt")
}

android {
    compileSdk = Versions.COMPILE_SDK

    defaultConfig {
        applicationId = "com.college.app"
        minSdk = Versions.MIN_SDK
        targetSdk = Versions.TARGET_SDK
        versionCode = 1
        versionName = "1.0"

//        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("debug") {
            isDebuggable = true
            isMinifyEnabled = false

            resValue("string", "app_name", "College App Debug")
            resValue("string", "college_endpoint", "http://192.168.0.103:8090/")
        }
        getByName("release") {
            isDebuggable = false
            isMinifyEnabled = true
//            applicationIdSuffix(".debug")
//            proguardFiles getDefaultProguardFile ('proguard-android-optimize.txt'), 'proguard-rules.pro'
            resValue("string", "app_name", "College App")
            resValue("string", "college_endpoint", "http://192.168.0.103:8090/")
            // todo add db options of product flavours
        }
    }
    flavorDimensions("type")
    productFlavors {
        create("community") {
            dimension = "type"
        }
        create("college") {
            dimension = "type"
        }
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.get()
    }
    buildFeatures {
        dataBinding = true
        compose = true
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
    implementation(libs.kotlin.coroutines.core)
    implementation(libs.kotlin.coroutines.android)
    implementation(libs.kotlin.coroutines.play.services)

    implementation(libs.androidx.core)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)
//    implementation(libs.androidx.datastore)
    implementation(libs.androidx.recyclerview)
    implementation(libs.androidx.recyclerview.selection)
    implementation(libs.androidx.startup)
    implementation(libs.androidx.lifecycle.runtime)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.core.splash)
    implementation(libs.androidx.datastore.preferences)

    implementation(libs.compose.ui.ui)
    implementation(libs.compose.ui.tooling)
    implementation(libs.compose.compiler)
    implementation(libs.compose.foundation.foundation)
    implementation(libs.androidx.activity.compose)
    implementation(libs.compose.foundation.layout)
    implementation(libs.compose.material.material)
    implementation(libs.compose.material.iconsext)
    implementation(libs.androidx.hilt.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.compose.livedata)

    implementation(libs.accompanist.swiperefresh)
    implementation(libs.accompanist.insets)
    implementation(libs.accompanist.insetsui)
    implementation(libs.accompanist.navigation.animation)
    implementation(libs.accompanist.systemuicontroller)

    implementation(libs.lottie)

    implementation(libs.retrofit.retrofit)
    implementation(libs.okhttp.okhttp)
    implementation(libs.okhttp.loggingInterceptor)
    implementation(libs.moshi)
    implementation(libs.moshi.converter)
    implementation(libs.moshi.compiler)

    implementation(libs.coil.coil)

    implementation(libs.google.material)

    implementation(libs.firebase.auth)
    implementation(libs.firebase.gmsauth)
    implementation(libs.firebase.database)
    implementation(libs.firebase.storage)
    implementation(libs.firebase.messaging)

    implementation(libs.picasso)
}

if (file("google-services.json").exists()) {
    apply { plugin("com.google.gms.google-services") }
    apply { plugin("com.google.firebase.crashlytics") }
}
