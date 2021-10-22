import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask

buildscript {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven("https://jitpack.io")
    }
    dependencies {
        classpath(libs.android.pluginGradle)
        classpath(libs.kotlin.pluginGradle)
        classpath(libs.hilt.pluginGradle)
        classpath(libs.google.gmsGoogleServices)

        classpath(libs.firebase.crashlyticsGradle)
    }
}

plugins {
    alias(libs.plugins.gradleDependencyUpdate)
}

allprojects {
    repositories {
        google()
        mavenCentral()
        maven("https://jitpack.io")
    }
}

subprojects {
    // todo setup spotless here
}

tasks.create<Delete>("clean") {
    delete = setOf(
        rootProject.buildDir,
        project(":app").buildDir,
        project(":base").buildDir
    )
}

tasks.withType<DependencyUpdatesTask> {
    rejectVersionIf {
        // todo check for the resolution strategy and the rejection case here
        currentVersion.isNonStable()
    }
    checkForGradleUpdate = true
    outputFormatter = "json"
    outputDir = "build/dependencyUpdates"
    reportfileName = "dependencyReport"
}

fun String.isNonStable(): Boolean {
    val stableKeyword = listOf("RELEASE", "FINAL", "GA").any { toUpperCase().contains(it) }
    val regex = "^[0-9,.v-]+(-r)?$".toRegex()
    val isStable = stableKeyword || regex.matches(this)
    return isStable.not()
}