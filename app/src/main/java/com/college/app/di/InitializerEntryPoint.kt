package com.college.app.di

import android.content.Context
import com.college.app.initializers.LoggerInitializer
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent

/*
* reference https://proandroiddev.com/app-startup-hilt-7d253d60772f
* Class to make hilt functioning inside the app-startup initializers
*
* ApplicationComponent is initialized by Hilt library lazily in the Application class.
* It happens on the first access to the component or in the onCreate callback.
* */

@EntryPoint
@InstallIn(SingletonComponent::class)
interface InitializerEntryPoint {

    fun inject(loggerInitializer: LoggerInitializer)

    companion object {

        fun resolve(context: Context): InitializerEntryPoint {
            val appContext = context.applicationContext ?: throw IllegalStateException()

            return EntryPointAccessors.fromApplication(
                appContext,
                InitializerEntryPoint::class.java
            )
        }

    }
}