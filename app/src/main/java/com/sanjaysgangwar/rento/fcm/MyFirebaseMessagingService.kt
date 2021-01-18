package com.sanjaysgangwar.rento.fcm

import android.app.ActivityManager
import android.app.ActivityManager.RunningAppProcessInfo
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.sanjaysgangwar.rento.activity.MainActivity
import org.json.JSONObject


class MyFirebaseMessagingService : FirebaseMessagingService() {

    companion object {
        private const val TAG = "MyFirebaseService"
        private const val NOTIFICATION_TYPE = "notificationType"
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        remoteMessage.data.isNotEmpty().let {
            remoteMessage.data.let { data ->
                val jsonObject = JSONObject(data as Map<*, *>)
                if (jsonObject.has(NOTIFICATION_TYPE)) {
                    if (applicationInForeground()) {
                        createOrderNotification(jsonObject.toString())
                    } else {
                        createOrderNotification(jsonObject.toString())
                    }
                }
            }
        }

        // Check if message contains a notification payload.
        remoteMessage.notification?.let { notification ->
            Log.d(TAG, "Message Notification Body: ${notification.body}")
        }
    }
    // [END receive_message]

    private fun createOrderNotification(notificationData: String) {
        val pendingIntent = PendingIntent.getActivity(
            this,
            System.currentTimeMillis().toInt(),
            Intent(this, MainActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            }, PendingIntent.FLAG_ONE_SHOT
        )

        val channelId = "general"
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setContentTitle("sanjay")
            .setContentText("Body")
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)

        notificationBuilder.color = ContextCompat.getColor(this, android.R.color.holo_blue_dark)

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Channel human readable title",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(System.currentTimeMillis().toInt(), notificationBuilder.build())
    }

    private fun applicationInForeground(): Boolean {
        val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val services = activityManager.runningAppProcesses
        var isActivityFound = false
        if (!services.isNullOrEmpty() && (services[0].processName.equals(
                packageName,
                ignoreCase = true
            )
                    && services[0].importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND)
        ) {
            isActivityFound = true
        }
        return isActivityFound
    }
}