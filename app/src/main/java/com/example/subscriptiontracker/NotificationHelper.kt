package com.example.subscriptiontracker

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import java.util.Locale

class NotificationHelper(private val context: Context) {

    companion object {
        const val CHANNEL_ID = "subscription_reminders"
        const val CHANNEL_NAME = "Subscription Reminders"
        const val CHANNEL_DESCRIPTION = "Notifications for subscription payment reminders"
    }

    init {
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance).apply {
            description = CHANNEL_DESCRIPTION
        }
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    fun showNotification(subscriptionItem: SubscriptionItem) {
        android.util.Log.d("NotificationHelper", "Showing notification for ${subscriptionItem.name} with amount: ${subscriptionItem.amount}")

        if (subscriptionItem.name.isEmpty() || subscriptionItem.amount == 0.0) {
            android.util.Log.e("NotificationHelper", "Invalid subscription data - Name or amount is missing/zero")
            return
        }

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            subscriptionItem.subscriptionId,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val formattedAmount = String.format(Locale.US, "%.2f", subscriptionItem.amount)
        val notificationText = "${subscriptionItem.name} payment of $formattedAmount ${subscriptionItem.currency} is due soon"
        
        android.util.Log.d("NotificationHelper", "Notification text: $notificationText")

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("Subscription Payment Due")
            .setContentText(notificationText)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        NotificationManagerCompat.from(context).apply {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return
            }
            notify(subscriptionItem.subscriptionId, notification)
        }
    }
} 