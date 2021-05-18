package com.college.app.utils

import android.content.Context
import com.college.app.MyObjectBox
import io.objectbox.BoxStore

object ObjectBox {
    var boxStore: BoxStore? = null
        private set

    fun init(context: Context) {
        boxStore = MyObjectBox.builder()
            .androidContext(context.applicationContext)
            .build()
    }
}