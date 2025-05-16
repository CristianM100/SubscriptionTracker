package com.example.subscriptiontracker.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.subscriptiontracker.data.model.SubscriptionItem
import java.util.Date


class ReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {

        if (intent.action != "com.example.subscriptiontracker.REMINDER_ACTION" &&
            intent.action != "android.intent.action.BOOT_COMPLETED") {
            return
        }

        if (intent.action == "android.intent.action.BOOT_COMPLETED") {
            android.util.Log.d("ReminderReceiver", "Device booted, no notification needed")
            return
        }

        val subscriptionId = intent.getIntExtra("subscriptionId", -1)
        val subscriptionName = intent.getStringExtra("subscriptionName")
        val amountString = intent.getStringExtra("subscriptionAmount")
        val subscriptionCurrency = intent.getStringExtra("subscriptionCurrency")

        if (subscriptionName.isNullOrEmpty() || amountString.isNullOrEmpty()) {
            return
        }

        val subscriptionAmount = amountString.toDoubleOrNull()
        if (subscriptionAmount == null || subscriptionAmount == 0.0) {
            return
        }

        val subscriptionItem = SubscriptionItem(
            subscriptionId = subscriptionId,
            name = subscriptionName,
            desc = "",
            cat = "",
            pay = Date(),
            cycle = "",
            amount = subscriptionAmount,
            currency = subscriptionCurrency ?: "",
            payMet = "",
            remind = ""
        )
        NotificationHelper(context).showNotification(subscriptionItem)
    }
} 