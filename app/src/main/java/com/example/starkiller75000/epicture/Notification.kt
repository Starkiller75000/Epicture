package com.example.starkiller75000.epicture

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.support.v4.app.NotificationCompat.*
import android.support.v4.content.ContextCompat

class Notification {
    companion object {
        private val NOTIFICATION_ID = 1998
        private val NOTIFICATION_CHANNEL_ID = "upload_notification_channel"
        private val NEW_COMMENT_CHANNEL_ID = "new_comment_notification_channel"
        private val NEW_COMMENT_ID = 1997
        private val ACTION_COPY_PENDING_INTENT_ID = 98
        private val PENDING_INTENT_ID = 38

        fun clearAllNotifications(context: Context) {
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.cancelAll()
        }

        @SuppressLint("ObsoleteSdkInt")
        @TargetApi(Build.VERSION_CODES.O)
        fun uploadedNotification(context: Context, url: String) {

            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val mChannel = NotificationChannel(
                    NOTIFICATION_CHANNEL_ID,
                    "Primary",
                    NotificationManager.IMPORTANCE_HIGH)

                notificationManager.createNotificationChannel(mChannel)
            }

            val notificationBuilder = Builder(context, NOTIFICATION_CHANNEL_ID)
            notificationBuilder
                .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(largeIcon(context))
                .setContentTitle(context.getString(R.string.image_uploaded_success))
                .setContentText(context.getString(R.string.image_uploaded))
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setContentIntent(contentIntent(context))
                .setAutoCancel(true)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN && Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
                notificationBuilder.priority = PRIORITY_HIGH
            }

            notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())
        }

        private fun contentIntent(context: Context): PendingIntent {
            val startActivityIntent = Intent(context, ImageFragment::class.java)
            return PendingIntent.getActivity(
                context,
                PENDING_INTENT_ID,
                startActivityIntent,
                PendingIntent.FLAG_UPDATE_CURRENT)
        }

        private fun largeIcon(context: Context): Bitmap {
            val res = context.resources
            return BitmapFactory.decodeResource(res, R.mipmap.ic_launcher)
        }
    }
}

