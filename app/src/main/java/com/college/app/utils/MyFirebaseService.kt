package com.college.app.utils
//
//import android.app.NotificationChannel
//import android.app.NotificationManager
//import android.app.PendingIntent
//import android.content.Intent
//import android.media.RingtoneManager
//import android.os.Build
//import android.util.Log
//import androidx.core.app.NotificationCompat
//import com.college.app.MainActivity
//import com.college.app.Notification.NotificationAdapter
//import com.college.app.R
//import com.google.android.gms.tasks.Task
//import com.google.firebase.iid.FirebaseInstanceId
//import com.google.firebase.iid.InstanceIdResult
//import com.google.firebase.messaging.FirebaseMessagingService
//import com.google.firebase.messaging.RemoteMessage
//
//class MyFirebaseService : FirebaseMessagingService() {
//    /*notifications: foreground and background working fine and the notification item is added as well
//     *                but when app is killed, notification is received but is not getting added
//     *                 it will be received some times when app is reopened maybe the onMessage is triggered*/
//    private val notificationAdapter: NotificationAdapter? = null
//    var title: String? = null
//    var body: String? = null
//    override fun onNewToken(s: String) {
//        FirebaseInstanceId.getInstance().instanceId.addOnCompleteListener { task: Task<InstanceIdResult> ->
//            if (!task.isSuccessful) {
//                Log.w(TAG, "failed: ", task.exception)
//                return@addOnCompleteListener
//            }
//            val token = task.result!!.token
//            Log.d(TAG, token)
//        }
//    }
//
//    override fun onMessageReceived(remoteMessage: RemoteMessage) {
//        try {
//            sendNotification(remoteMessage.data["Title"], remoteMessage.data["Message"])
//            title = remoteMessage.data["Title"]
//            body = remoteMessage.data["Message"]
//            //            addNotification(title, body);
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//    }
//
//    private fun sendNotification(title: String?, messageBody: String?) {
//        val intent = Intent(this, MainActivity::class.java)
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//        val pendingIntent = PendingIntent.getActivity(
//            this, 0 /* Request code */, intent,
//            PendingIntent.FLAG_ONE_SHOT
//        )
//        val channelId = getString(R.string.default_notification_channel_id)
//        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
//        val notificationBuilder = NotificationCompat.Builder(this, channelId)
//            .setSmallIcon(R.drawable.ic_launcher_foreground)
//            .setContentTitle(title)
//            .setContentText(messageBody)
//            .setAutoCancel(true)
//            .setSound(defaultSoundUri)
//            .setContentIntent(pendingIntent)
//        val notificationManager = this.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
//
//        // For Android Oreo ++
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val channel = NotificationChannel(
//                channelId,
//                "Channel human readable title",
//                NotificationManager.IMPORTANCE_DEFAULT
//            )
//            notificationManager?.createNotificationChannel(channel)
//        }
//        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build())
//    }
//
//    companion object {
//        private const val TAG = "1234"
//    }
//}