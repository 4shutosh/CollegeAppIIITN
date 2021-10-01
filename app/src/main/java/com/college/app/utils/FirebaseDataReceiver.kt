package com.college.app.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.college.app.Notification.Notification

class FirebaseDataReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val bundle = intent.extras
        if (bundle != null) {
            val extras = intent.extras
            val title = extras!!.getString("Title")
            val body = extras.getString("Message")
            notification = Notification(0, title, body)
//            EventBus.getDefault().post(Notification(0, title, body))
        }
    }

    companion object {
        var notification: Notification? = null
    }
}