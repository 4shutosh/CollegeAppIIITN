package com.college.app

import android.app.Application
import com.college.app.AppClass
import io.objectbox.BoxStore

class AppClass : Application() {
    override fun onCreate() {
        super.onCreate()
        Companion.boxStore = MyObjectBox.builder()
                .androidContext(this@AppClass)
                .build()
    }

    val boxStore: BoxStore?
        get() = Companion.boxStore

    companion object {
        private var boxStore: BoxStore? = null
    }
}