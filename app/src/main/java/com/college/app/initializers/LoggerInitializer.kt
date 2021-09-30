package com.college.app.initializers

import android.content.Context
import androidx.startup.Initializer
import com.curieo.base.utils.logger.CollegeLogger
import com.google.firebase.BuildConfig
import javax.inject.Inject

class LoggerInitializer @Inject constructor(
    private val collegeLogger: CollegeLogger
) : Initializer<Unit> {
    override fun create(context: Context) {
        collegeLogger.init(BuildConfig.DEBUG)
    }

    override fun dependencies(): List<Class<out Initializer<*>>> = emptyList()
}