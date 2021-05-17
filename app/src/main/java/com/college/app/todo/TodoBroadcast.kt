package com.college.app.todo

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.college.app.MainActivity
import com.college.app.R
import com.college.app.utils.NotificationAction

class TodoBroadcast : BroadcastReceiver() {
    var todoTitle: String? = null
    override fun onReceive(context: Context, intent: Intent) {
        val channelId = "todoChannel"
        todoTitle = intent.extras!!.getString("MyMessage")
        val id = intent.extras!!.getLong("todoId")
        Log.d(TAG, "onReceive: $todoTitle")
        val markAsDoneIntent = Intent(context, NotificationAction::class.java)
        markAsDoneIntent.putExtra("todoId", id)
        markAsDoneIntent.putExtra("action", "markAsDone")
        val markAsDonePending = PendingIntent.getBroadcast(
            context,
            1,
            markAsDoneIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val main = Intent(context, MainActivity::class.java)
        val m2 = PendingIntent.getActivity(
            context, 0,
            main, 0
        )
        val snoozeIntent = Intent(context, NotificationAction::class.java)
        snoozeIntent.putExtra("todoId", id)
        snoozeIntent.putExtra("action", "snooze")
        val snoozePendingIntent =
            PendingIntent.getBroadcast(context, 1, snoozeIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_access_time_black_24dp)
            .setContentTitle("Todo Reminder")
            .setContentText(todoTitle)
            .setAutoCancel(true)
            .setContentIntent(m2)
            .addAction(R.drawable.ic_baseline_check_24, "Mark as done", markAsDonePending)
            .addAction(R.drawable.ic_access_time_black_24dp, "Snooze", snoozePendingIntent)
            .setPriority(NotificationCompat.PRIORITY_MAX)
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Todo Reminder",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(0, builder.build())

//        try{
//            Uri som = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//            Ringtone toque = RingtoneManager.getRingtone(context, som);
//            toque.play();
//        }
//        catch(Exception e){
//
//        }
    }

    companion object {
        private const val TAG = "sdfsd"
    }
}