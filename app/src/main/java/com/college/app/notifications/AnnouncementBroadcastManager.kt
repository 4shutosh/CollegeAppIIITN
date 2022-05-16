package com.college.app.notifications

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.college.app.R
import com.college.app.ui.todo.broadcast.TodoBroadcastReceiver
import com.college.app.utils.NotificationHelper
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AnnouncementBroadcastManager : BroadcastReceiver() {

    @Inject
    lateinit var notificationHelper: NotificationHelper

    override fun onReceive(context: Context, intent: Intent) {
        val bundle = intent.extras

        when (intent.action) {
            ANNOUNCEMENT_NOTIFICATION_KEY -> {
                if (bundle != null) {
                    val title = bundle.getString(KEY_TITLE)
                    val message = bundle.getString(KEY_MESSAGE)
                    val requestCode = bundle.getString(KEY_ID).orEmpty().toInt()

                    val contentIntent =
                        PendingIntent.getActivity(
                            context,
                            requestCode,
                            intent,
                            PendingIntent.FLAG_IMMUTABLE
                        )


                    val notificationBuilder =
                        NotificationCompat.Builder(context,
                            TodoBroadcastReceiver.TODO_NOTIFICATION_ID)
                    val notificationManager =
                        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

                    notificationBuilder.setContentTitle(title)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                        .setContentIntent(contentIntent)
                        .setAutoCancel(true)

                    if (!message.isNullOrEmpty()) notificationBuilder.setContentText(
                        message
                    )

                    if (notificationHelper.isChannelExists(context,
                            TodoBroadcastReceiver.TODO_NOTIFICATION_ID).not()
                    ) {
                        notificationHelper.createNotificationChannel(
                            notificationManager,
                            TodoBroadcastReceiver.TODO_NOTIFICATION_ID,
                            TodoBroadcastReceiver.TODO_NOTIFICATION_ID,
                            NotificationManagerCompat.IMPORTANCE_HIGH // check importance
                        )
                        notificationBuilder.setChannelId(TodoBroadcastReceiver.TODO_NOTIFICATION_ID)
                    }

                    val notification = notificationBuilder.build()
                    notificationManager.notify(requestCode, notification)
                }
            }
        }

    }

    companion object {
        const val ANNOUNCEMENT_NOTIFICATION_KEY = "ANNOUNCEMENT_NOTIFICATION_KEY"

        const val KEY_ID = "KEY_ID" // this is the request code for pending intent
        const val KEY_TITLE = "KEY_TITLE"
        const val KEY_MESSAGE = "KEY_MESSAGE"
    }

}