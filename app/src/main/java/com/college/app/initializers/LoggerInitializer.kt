package com.college.app.initializers

import android.content.Context
import androidx.startup.Initializer
import com.college.app.BuildConfig
import com.college.app.di.InitializerEntryPoint
import com.college.base.logger.CollegeLogger
import javax.inject.Inject

/*
constructor injection won't work here
*/

class LoggerInitializer : Initializer<Unit> {

    @Inject
    lateinit var collegeLogger: CollegeLogger

    override fun create(context: Context) {
        InitializerEntryPoint.resolve(context).inject(this)
        collegeLogger.init(BuildConfig.DEBUG)
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        return listOf(DependencyGraphInitializer::class.java)
    }
}