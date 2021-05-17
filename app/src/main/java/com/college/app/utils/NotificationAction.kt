package com.college.app.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.college.app.MainActivity

class NotificationAction : BroadcastReceiver() {
    private val TODO_FRAGMENT = "3"
    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.getStringExtra("action")
        val id = intent.extras!!.getLong("todoId")
        if (action == "markAsDone") {
            val i = Intent(context, MainActivity::class.java)
            i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            i.putExtra("fragmentInformation", TODO_FRAGMENT)
            context.startActivity(i)
        } else if (action == "snooze") {
            val i = Intent(context, MainActivity::class.java)
            i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            i.putExtra("fragmentInformation", TODO_FRAGMENT)
            context.startActivity(i)
            //            adapter.snoozeNotificationAction(id);
        }
        val it = Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)
        context.sendBroadcast(it)
    }
}