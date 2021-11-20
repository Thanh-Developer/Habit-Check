package com.demo.habitcheck.utils

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.graphics.Color
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.demo.habitcheck.R

class NotificationUtil {

    fun showNotification(
        context: Context,
        title: String?,
        text: String?,
        pendingIntent: PendingIntent?
    ) {
        //only used on 26+
        createNotificationChannel(context)

        val builder: Notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle(title)
            .setContentText(text)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setOnlyAlertOnce(true)
            .setColor(Color.GREEN)
            .build()


        NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, builder)
    }

    private fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            channel.description = CHANNEL_DES
            val notificationManager = context.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    companion object {
        private const val CHANNEL_ID = "1"
        private const val NOTIFICATION_ID = 1
        private const val CHANNEL_NAME = "channel name"
        private const val CHANNEL_DES = "channel desc"
    }
}