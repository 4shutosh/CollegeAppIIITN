package com.college.app.initializers

import android.content.Context
import androidx.startup.Initializer
import com.college.app.di.InitializerEntryPoint
import com.college.base.utils.logger.CollegeLogger
import com.google.firebase.BuildConfig
import javax.inject.Inject

class LoggerInitializer @Inject constructor(
    private val collegeLogger: CollegeLogger
) : Initializer<Unit> {

    override fun create(context: Context) {
        InitializerEntryPoint.resolve(context).inject(this)
        collegeLogger.init(BuildConfig.DEBUG)
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        return listOf(DependencyGraphInitializer::class.java)
    }
}