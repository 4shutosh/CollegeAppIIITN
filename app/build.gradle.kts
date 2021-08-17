plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-kapt")
    id("kotlin-parcelize")
    id("com.google.gms.google-services")
    id("io.objectbox")
    id("androidx.navigation.safeargs.kotlin")
}


android {
    compileSdkVersion(29)
//    buildToolsVersion "2.0.2"

    defaultConfig {
        applicationId = "com.college.app"
        minSdkVersion(21)
        targetSdkVersion(29)
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

//    buildTypes {
//        getByName("release") {
////            minifyEnabled false
////            proguardFiles getDefaultProguardFile ('proguard-android-optimize.txt'), 'proguard-rules.pro'
//        }
//    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    viewBinding {
        enabled = true
    }

}

dependencies {
//    implementation fileTree (dir: 'libs', include: ['*.jar'])

    implementation("androidx.appcompat:appcompat:1.2.0")
    implementation("androidx.constraintlayout:constraintlayout:2.0.0")

    //recycler View
    implementation("androidx.recyclerview:recyclerview:1.1.0")
    implementation("androidx.recyclerview:recyclerview-selection:1.1.0-rc01")

    // material
    implementation("com.google.android.material:material:1.2.0")

    //EventBus
    implementation("org.greenrobot:eventbus:3.2.0")

    // firebase
    implementation("com.google.firebase:firebase-auth:19.3.2")
    implementation("com.google.android.gms:play-services-auth:18.1.0")
    implementation("com.google.firebase:firebase-database:19.3.1")
    implementation("com.google.firebase:firebase-storage:19.1.1")
    implementation("com.google.firebase:firebase-messaging:20.2.4")

    //picasso
    implementation("com.squareup.picasso:picasso:2.71828")

    //parse
    implementation("com.github.parse-community.Parse-SDK-Android:parse:1.23.1")

    testImplementation("junit:junit:4.13")
    androidTestImplementation("androidx.test.ext:junit:1.1.2")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.3.0")
    implementation("androidx.core:core-ktx:1.3.1")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:1.4.0")

    implementation("com.facebook.shimmer:shimmer:0.1.0@aar")
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")

    //circular view
    implementation("de.hdodenhof:circleimageview:3.1.0")

}
repositories {
    mavenCentral()
}
