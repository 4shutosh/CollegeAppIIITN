package com.college.app.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.college.app.MainActivity;

public class NotificationAction extends BroadcastReceiver {


    private final String TODO_FRAGMENT = "3";

    @Override
    public void onReceive(Context context, Intent intent) {

        String action = intent.getStringExtra("action");
        long id = intent.getExtras().getLong("todoId");

        if (action.equals("markAsDone")) {
            Intent i = new Intent(context, MainActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.putExtra("fragmentInformation", TODO_FRAGMENT);
            context.startActivity(i);
        } else if (action.equals("snooze")) {
            Intent i = new Intent(context, MainActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.putExtra("fragmentInformation", TODO_FRAGMENT);
            context.startActivity(i);
//            adapter.snoozeNotificationAction(id);
        }

        Intent it = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        context.sendBroadcast(it);

    }
}