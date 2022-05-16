package com.college.app.notifications

import android.content.Intent
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.college.app.notifications.AnnouncementBroadcastManager.Companion.ANNOUNCEMENT_NOTIFICATION_KEY
import com.college.app.notifications.AnnouncementBroadcastManager.Companion.KEY_ID
import com.college.app.notifications.AnnouncementBroadcastManager.Companion.KEY_MESSAGE
import com.college.app.notifications.AnnouncementBroadcastManager.Companion.KEY_TITLE
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AnnouncementsNotificationService : FirebaseMessagingService() {

    private var broadcaster: LocalBroadcastManager? = null

    override fun onCreate() {
        super.onCreate()
        broadcaster = LocalBroadcastManager.getInstance(this)
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        handleMessage(message)
    }

    private fun handleMessage(message: RemoteMessage) {
        print("message found $message")
        val intent = Intent(ANNOUNCEMENT_NOTIFICATION_KEY)
        intent.putExtra(KEY_TITLE, message.data[KEY_TITLE])
        intent.putExtra(KEY_MESSAGE, message.data[KEY_MESSAGE])
        intent.putExtra(KEY_ID, message.data[KEY_ID])

        broadcaster?.sendBroadcast(intent)

    }

}