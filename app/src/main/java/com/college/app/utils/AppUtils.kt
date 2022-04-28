package com.college.app.utils

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Build
import androidx.fragment.app.Fragment
import com.college.app.ui.books.LibraryFragment
import com.college.app.ui.books.LibraryFragment.Companion.OFFLINE_LIBRARY_FRAGMENT_ID
import com.college.app.ui.courses.CoursesFragment
import com.college.app.ui.courses.CoursesFragment.Companion.COURSES_FRAGMENT
import com.college.app.ui.main.home.HomeFragment
import com.college.app.ui.main.home.HomeFragment.Companion.HOME_FRAGMENT_ID
import com.college.app.ui.profile.ProfileSettingsFragment
import com.college.app.ui.profile.ProfileSettingsFragment.Companion.FRAGMENT_PROFILE_SETTINGS_ID
import com.college.app.ui.todo.TodoFragment
import com.college.app.ui.todo.TodoFragment.Companion.TODO_FRAGMENT_ID

object AppUtils {
    fun checkAppInstalled(context: Context, packageName: String?): Boolean {
        return try {
//            context.packageManager.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES) todo fix this
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


    fun fragmentFromId(id: Int): Fragment? {
        return when (id) {
            HOME_FRAGMENT_ID -> HomeFragment()
            TODO_FRAGMENT_ID -> TodoFragment()
            OFFLINE_LIBRARY_FRAGMENT_ID -> LibraryFragment()
            COURSES_FRAGMENT -> CoursesFragment()
            FRAGMENT_PROFILE_SETTINGS_ID -> ProfileSettingsFragment()
            else -> {
                null
            }
        }
    }

}