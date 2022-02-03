package com.college.app.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.ResolveInfo
import android.net.Uri
import android.os.Build
import androidx.browser.customtabs.CustomTabsIntent
import androidx.browser.customtabs.CustomTabsService.ACTION_CUSTOM_TABS_CONNECTION


fun getBroadCastReceiveListener(action: (context: Context, intent: Intent) -> Unit): BroadcastReceiver {
    return object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            action(context, intent)
        }
    }
}

fun <T> runWithMinSdk(minVersion: Int, fallback: (() -> T)? = null, func: () -> T) {
    if (Build.VERSION.SDK_INT >= minVersion) {
        func()
    } else fallback?.invoke()
}

fun openURL(context: Context, url: String) {
    val listOfCustomTabsSupportedBrowsers = getCustomTabsPackages(context)
    val chromeAvailable = listOfCustomTabsSupportedBrowsers.find {
        it.activityInfo.packageName == "com.android.chrome"
    }
    val tabsIntent = CustomTabsIntent.Builder().build()
    if (chromeAvailable != null) {
        tabsIntent.intent.setPackage(chromeAvailable.activityInfo.packageName)
    } else if (listOfCustomTabsSupportedBrowsers.isNotEmpty()) {
        tabsIntent.intent.setPackage(listOfCustomTabsSupportedBrowsers[0].activityInfo.packageName)
    }
    tabsIntent.launchUrl(context, Uri.parse(url))
}

fun getCustomTabsPackages(context: Context): ArrayList<ResolveInfo> {
    val packageManager = context.packageManager
    // Get default VIEW intent handler.
    val activityIntent = Intent()
        .setAction(Intent.ACTION_VIEW)
        .addCategory(Intent.CATEGORY_BROWSABLE)
        .setData(Uri.fromParts("http", "", null))

    // Get all apps that can handle VIEW intents.
    val resolvedActivityList = packageManager.queryIntentActivities(activityIntent, 0)
    val packagesSupportingCustomTabs: ArrayList<ResolveInfo> = ArrayList()
    for (info in resolvedActivityList) {
        val serviceIntent = Intent()
        serviceIntent.action = ACTION_CUSTOM_TABS_CONNECTION
        serviceIntent.setPackage(info.activityInfo.packageName)
        // Check if this package also resolves the Custom Tabs service.
        if (packageManager.resolveService(serviceIntent, 0) != null) {
            packagesSupportingCustomTabs.add(info)
        }
    }
    return packagesSupportingCustomTabs
}