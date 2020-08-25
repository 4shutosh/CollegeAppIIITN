package com.college.app.todo;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.college.app.MainActivity;
import com.college.app.R;
import com.college.app.utils.NotificationAction;

public class TodoBroadcast extends BroadcastReceiver {

    private static final String TAG = "sdfsd";
    public String todoTitle;

    @Override
    public void onReceive(Context context, Intent intent) {
        String channelId = "todoChannel";
        todoTitle = intent.getExtras().getString("MyMessage");
        long id = intent.getExtras().getLong("todoId");
        Log.d(TAG, "onReceive: " + todoTitle);
        Intent markAsDoneIntent = new Intent(context, NotificationAction.class);
        markAsDoneIntent.putExtra("todoId", id);
        markAsDoneIntent.putExtra("action", "markAsDone");
        PendingIntent markAsDonePending = PendingIntent.getBroadcast(context, 1, markAsDoneIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        Intent main = new Intent(context, MainActivity.class);
        PendingIntent m2 = PendingIntent.getActivity(context, 0,
                main, 0);


        Intent snoozeIntent = new Intent(context, NotificationAction.class);
        snoozeIntent.putExtra("todoId", id);
        snoozeIntent.putExtra("action", "snooze");
        PendingIntent snoozePendingIntent = PendingIntent.getBroadcast(context, 1, snoozeIntent, PendingIntent.FLAG_UPDATE_CURRENT);


        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.ic_access_time_black_24dp)
                .setContentTitle("Todo Reminder")
                .setContentText(todoTitle)
                .setAutoCancel(true)
                .setContentIntent(m2)
                .addAction(R.drawable.ic_baseline_check_24, "Mark as done", markAsDonePending)
                .addAction(R.drawable.ic_access_time_black_24dp, "Snooze", snoozePendingIntent)
                .setPriority(NotificationCompat.PRIORITY_MAX);


        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Todo Reminder",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0, builder.build());

//        try{
//            Uri som = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//            Ringtone toque = RingtoneManager.getRingtone(context, som);
//            toque.play();
//        }
//        catch(Exception e){
//
//        }
    }
}
