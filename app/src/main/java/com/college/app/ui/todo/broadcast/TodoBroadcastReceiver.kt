package com.college.app.ui.todo.broadcast

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.college.app.R
import com.college.app.utils.NotificationHelper
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class TodoBroadcastReceiver : BroadcastReceiver() {

    @Inject
    lateinit var notificationHelper: NotificationHelper

    override fun onReceive(context: Context, intent: Intent) {
        val bundle = intent.extras

        when (intent.action) {
            TODO_ACTION_SEND_NOTIFICATION -> {
                if (bundle != null) {
                    val title = bundle.getString(KEY_TODO_TITLE)
                    val description = bundle.getString(KEY_TODO_DESCRIPTION)
                    val requestCode = bundle.getInt(KEY_TODO_ID)

                    val contentIntent =
                        PendingIntent.getActivity(
                            context,
                            requestCode,
                            intent,
                            PendingIntent.FLAG_IMMUTABLE
                        )


                    val notificationBuilder =
                        NotificationCompat.Builder(context, TODO_NOTIFICATION_ID)
                    val notificationManager =
                        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

                    notificationBuilder.setContentTitle(title)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setCategory(NotificationCompat.CATEGORY_REMINDER)
                        .addAction(
                            R.drawable.todo_alarm_checkbox,
                            context.getString(R.string.todo_notification_mark_as_done),
                            contentIntent
                        )
                        .setContentIntent(contentIntent)
                        .setAutoCancel(true)

                    if (!description.isNullOrEmpty()) notificationBuilder.setContentText(
                        description
                    )

                    if (notificationHelper.isChannelExists(context, TODO_NOTIFICATION_ID).not()) {
                        notificationHelper.createNotificationChannel(
                            notificationManager, TODO_NOTIFICATION_ID, TODO_NOTIFICATION_ID,
                            NotificationManagerCompat.IMPORTANCE_HIGH // check importance
                        )
                        notificationBuilder.setChannelId(TODO_NOTIFICATION_ID)
                    }

                    val notification = notificationBuilder.build()
                    notificationManager.notify(requestCode, notification)
                }
            }
            TODO_ACTION_MARK_AS_DONE -> {

            }
        }
    }

    companion object {
        const val TODO_ACTION_SEND_NOTIFICATION = "TODO_ACTION_SEND_NOTIFICATION"
        const val TODO_ACTION_MARK_AS_DONE = "TODO_ACTION_MARK_AS_DONE"

        const val KEY_TODO_ID = "KEY_TODO_ID" // this is the request code for pending intent
        const val KEY_TODO_TITLE = "KEY_TODO_TITLE"
        const val KEY_TODO_DESCRIPTION = "KEY_TODO_DESCRIPTION"

        const val TODO_NOTIFICATION_ID = "TODO Notifications"
    }
}