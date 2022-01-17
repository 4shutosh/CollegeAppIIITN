package com.college.app.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build

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