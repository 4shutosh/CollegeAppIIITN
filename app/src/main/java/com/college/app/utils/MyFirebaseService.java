package com.college.app.utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.college.app.MainActivity;
import com.college.app.Notification.Notification;
import com.college.app.Notification.NotificationAdapter;
import com.college.app.Notification.NotificationsFragment;
import com.college.app.R;
import com.college.app.databinding.FragmentNotificationsBinding;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.Set;

public class MyFirebaseService extends FirebaseMessagingService {

    /*notifications: foreground and background working fine and the notification item is added as well
     *                but when app is killed, notification is received but is not getting added
     *                 it will be received some times when app is reopened maybe the onMessage is triggered*/

    private NotificationAdapter notificationAdapter;
    private static final String TAG = "1234";
    String title;
    String body;


    @Override
    public void onNewToken(@NonNull String s) {
        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.w(TAG, "failed: ", task.getException());
                return;
            }
            String token = task.getResult().getToken();
            Log.d(TAG, token);
        });
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        try {
            sendNotification(remoteMessage.getData().get("Title"), remoteMessage.getData().get("Message"));
            title = remoteMessage.getData().get("Title");
            body = remoteMessage.getData().get("Message");
//            addNotification(title, body);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void sendNotification(String title, String messageBody) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        String channelId = getString(R.string.default_notification_channel_id);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.ic_launcher_foreground)
                        .setContentTitle(title)
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        // For Android Oreo ++
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

}
