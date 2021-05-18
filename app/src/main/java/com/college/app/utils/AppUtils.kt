package com.college.app.utils

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Build

object AppUtils {
    fun checkAppInstalled(context: Context, packageName: String?): Boolean {
        return try {
            context.packageManager.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }

    fun getVersionName(context: Context): String {
        return try {
            val manager = context.packageManager
            val info = manager.getPackageInfo(context.packageName, 0)
            val version = info.versionName
            "V: $version"
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }

    fun getScreenWidthDp(context: Context?): Int {
        val displayMetrics = context!!.resources.displayMetrics
        return (displayMetrics.widthPixels / displayMetrics.density).toInt()
    }

    fun isGame(context: Context): Boolean {
        return try {
            val pm = context.packageManager
            val applicationInfo = pm.getApplicationInfo(context.packageName, 0)
            if (applicationInfo.flags and ApplicationInfo.FLAG_IS_GAME == ApplicationInfo.FLAG_IS_GAME) {
                return true
            }
            if (Build.VERSION.SDK_INT >= 26 && applicationInfo.category == ApplicationInfo.CATEGORY_GAME) {
                true
            } else false
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}