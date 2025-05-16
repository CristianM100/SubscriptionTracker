package com.example.subscriptiontracker

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import java.util.Locale

class ReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        // Log the received action for debugging
        android.util.Log.d("ReminderReceiver", "Received action: ${intent.action}")

        // Handle both our custom action and boot completed
        if (intent.action != "com.example.subscriptiontracker.REMINDER_ACTION" && 
            intent.action != "android.intent.action.BOOT_COMPLETED") {
            android.util.Log.e("ReminderReceiver", "Received unknown action: ${intent.action}")
            return
        }

        // If it's a boot completed action, we don't need to show a notification
        if (intent.action == "android.intent.action.BOOT_COMPLETED") {
            android.util.Log.d("ReminderReceiver", "Device booted, no notification needed")
            return
        }

        val subscriptionId = intent.getIntExtra("subscriptionId", -1)
        val subscriptionName = intent.getStringExtra("subscriptionName")
        val amountString = intent.getStringExtra("subscriptionAmount")
        val subscriptionCurrency = intent.getStringExtra("subscriptionCurrency")

        android.util.Log.d("ReminderReceiver", """
            Received reminder data:
            - ID: $subscriptionId
            - Name: $subscriptionName
            - Amount string: $amountString
            - Currency: $subscriptionCurrency
        """.trimIndent())

        // Validate the received data
        if (subscriptionName.isNullOrEmpty() || amountString.isNullOrEmpty()) {
            android.util.Log.e("ReminderReceiver", "Missing required data - Name or amount is null/empty")
            return
        }

        val subscriptionAmount = amountString.toDoubleOrNull()
        if (subscriptionAmount == null || subscriptionAmount == 0.0) {
            android.util.Log.e("ReminderReceiver", "Invalid amount value: $amountString")
            return
        }

        // Create a temporary SubscriptionItem to pass to NotificationHelper
        val subscriptionItem = SubscriptionItem(
            subscriptionId = subscriptionId,
            name = subscriptionName,
            desc = "",
            cat = "",
            pay = java.util.Date(),
            cycle = "",
            amount = subscriptionAmount,
            currency = subscriptionCurrency ?: "",
            payMet = "",
            remind = ""
        )

        android.util.Log.d("ReminderReceiver", "Created subscription item - Name: ${subscriptionItem.name}, Amount: ${subscriptionItem.amount}")

        // Show the notification
        NotificationHelper(context).showNotification(subscriptionItem)
    }
} 