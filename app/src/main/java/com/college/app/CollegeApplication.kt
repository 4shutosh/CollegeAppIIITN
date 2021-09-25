package com.college.app

import android.app.Application
import io.objectbox.BoxStore

class CollegeApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Companion.boxStore = MyObjectBox.builder()
                .androidContext(this@CollegeApplication)
                .build()
    }

    val boxStore: BoxStore?
        get() = Companion.boxStore

    companion object {
        private var boxStore: BoxStore? = null
    }
}