package com.college.app.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import java.util.concurrent.atomic.AtomicInteger
import javax.inject.Inject

class NotificationHelper @Inject constructor() {
    private val notifyId: AtomicInteger = AtomicInteger(123)

    fun getNotificationID() = notifyId.incrementAndGet()

    fun isChannelExists(context: Context, channelID: String?): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return true
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        return manager.getNotificationChannel(channelID) != null
    }

    fun createNotificationChannel(
        notificationManager: NotificationManager,
        channelId: String, channelDesc: String, importance: Int,
    ) {
        runWithMinSdk(Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, channelDesc, importance)
            notificationManager.createNotificationChannel(channel)
        }
    }
}