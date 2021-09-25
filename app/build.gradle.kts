plugins {
    id("com.android.application")
    kotlin("android")
    id("io.objectbox") // here the order of plugin MATTERS
//    id("kotlin-parcelize")
    kotlin("android.extensions")
    kotlin("kapt")
    id("com.google.gms.google-services")
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
    implementation (fileTree(mapOf("dir" to "libs","include" to listOf("*.jar"))))

    implementation("androidx.appcompat:appcompat:1.3.1")
    implementation("androidx.constraintlayout:constraintlayout:2.1.0")

    //recycler View
    implementation("androidx.recyclerview:recyclerview:1.2.1")
    implementation("androidx.recyclerview:recyclerview-selection:1.2.0-alpha01")

    // material
    implementation("com.google.android.material:material:1.4.0")

    //EventBus
    implementation("org.greenrobot:eventbus:3.2.0")

    // firebase
    implementation("com.google.firebase:firebase-auth:21.0.1")
    implementation("com.google.android.gms:play-services-auth:19.2.0")
    implementation("com.google.firebase:firebase-database:20.0.2")
    implementation("com.google.firebase:firebase-storage:20.0.0")
    implementation("com.google.firebase:firebase-messaging:20.2.4")

    //picasso
    implementation("com.squareup.picasso:picasso:2.71828")

    //parse
    implementation("com.github.parse-community.Parse-SDK-Android:parse:1.23.1")

    implementation("androidx.core:core-ktx:1.6.0")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:1.5.30")

    implementation("com.facebook.shimmer:shimmer:0.1.0@aar")
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")

    //circular view
    implementation("de.hdodenhof:circleimageview:3.1.0")
}
