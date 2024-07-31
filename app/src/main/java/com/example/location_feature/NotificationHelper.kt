package com.example.location_feature

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import kotlin.random.Random


class NotificationHelper(base: Context?) : ContextWrapper(base) {
    private val CHANNEL_NAME = "High priority channel"
    private val CHANNEL_ID = "com.example.location_feature $CHANNEL_NAME"
    companion object {
        private const val TAG = "NotificationHelper"
    }
    init {
        createChannels()
    }

    private fun createChannels() {
        val notificationChannel =
            NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH)
        notificationChannel.enableLights(true)
        notificationChannel.enableVibration(true)
        notificationChannel.description = "this is the description of the channel."
        notificationChannel.lightColor = Color.RED
        notificationChannel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
        val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(notificationChannel)
    }

    fun sendHighPriorityNotification(title: String?, body: String?, activityName: Class<*>?) {
        val intent = Intent(this, activityName)

        val pendingIntent =
            PendingIntent.getActivity(this,
                267,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT)

        val notification: Notification =
            NotificationCompat.Builder(this, CHANNEL_ID)
                 .setContentTitle(title)
                 .setContentText(body)
                .setSmallIcon(R.drawable.icon41)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setStyle(
                    NotificationCompat.BigTextStyle().setSummaryText("summary")
                        .setBigContentTitle(title).bigText(body)
                )
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build()
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            return
        }
        NotificationManagerCompat.from(this).notify(Random.nextInt(), notification)
    }

}