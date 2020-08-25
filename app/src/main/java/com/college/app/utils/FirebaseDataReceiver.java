package com.college.app.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.college.app.Notification.Notification;

import org.greenrobot.eventbus.EventBus;

public class FirebaseDataReceiver extends BroadcastReceiver {
    public static Notification notification;

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            Bundle extras = intent.getExtras();
            String title = extras.getString("Title");
            String body = extras.getString("Message");
            notification = new Notification(0, title, body);
            EventBus.getDefault().post(new Notification(0, title, body));
        }
    }

    public static Notification getNotification() {
        return notification;
    }
}
