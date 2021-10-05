package com.college.base.logger

import android.util.Log
import com.google.firebase.crashlytics.FirebaseCrashlytics
import timber.log.Timber
import javax.inject.Inject

class CrashlyticsTree @Inject constructor(
    private val firebaseCrashlytics: FirebaseCrashlytics
) : Timber.Tree() {

    fun setUserId(id: String) {
        firebaseCrashlytics.setUserId(id)
    }

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        if (priority == Log.VERBOSE || priority == Log.DEBUG || priority == Log.INFO) {
            return
        }
        firebaseCrashlytics.setCustomKey(CRASHLYTICS_KEY_PRIORITY, priority)
        firebaseCrashlytics.setCustomKey(CRASHLYTICS_KEY_TAG, tag ?: "")
        firebaseCrashlytics.setCustomKey(CRASHLYTICS_KEY_MESSAGE, message)

        if (t == null) {
            firebaseCrashlytics.recordException(Exception(message))
        } else {
            firebaseCrashlytics.recordException(t)
        }
    }

    companion object {
        private const val CRASHLYTICS_KEY_PRIORITY = "priority"
        private const val CRASHLYTICS_KEY_TAG = "tag"
        private const val CRASHLYTICS_KEY_MESSAGE = "message"
    }
}
