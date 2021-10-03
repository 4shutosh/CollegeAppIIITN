package com.college.app.initializers

import android.content.Context
import androidx.startup.Initializer
import com.college.app.di.InitializerEntryPoint

// this will lazily initialize ApplicationComponent before Application's `onCreate`
class DependencyGraphInitializer : Initializer<Unit> {

    override fun create(context: Context) {
        InitializerEntryPoint.resolve(context)
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        return emptyList()
    }
}