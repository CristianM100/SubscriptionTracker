package com.example.subscriptiontracker

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import java.util.Calendar
import java.util.Date
import java.util.Locale

class AlarmHelper(private val context: Context) {

    companion object {
        const val REMINDER_ACTION = "com.example.subscriptiontracker.REMINDER_ACTION"
    }

    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    fun scheduleReminder(subscriptionItem: SubscriptionItem) {
        val reminderTime = calculateReminderTime(subscriptionItem)
        
        android.util.Log.d("AlarmHelper", "Scheduling reminder for ${subscriptionItem.name} with amount: ${subscriptionItem.amount}")
        
        // Create a new intent with all required data
        val intent = Intent(context, ReminderReceiver::class.java).apply {
            action = REMINDER_ACTION
            putExtra("subscriptionId", subscriptionItem.subscriptionId)
            putExtra("subscriptionName", subscriptionItem.name)
            putExtra("subscriptionAmount", String.format(Locale.US, "%.2f", subscriptionItem.amount))
            putExtra("subscriptionCurrency", subscriptionItem.currency)
        }

        // Verify the intent data before creating the PendingIntent
        android.util.Log.d("AlarmHelper", """
            Intent data verification:
            - Action: ${intent.action}
            - ID: ${intent.getIntExtra("subscriptionId", -1)}
            - Name: ${intent.getStringExtra("subscriptionName")}
            - Amount: ${intent.getStringExtra("subscriptionAmount")}
            - Currency: ${intent.getStringExtra("subscriptionCurrency")}
        """.trimIndent())

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            subscriptionItem.subscriptionId,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            reminderTime.timeInMillis,
            pendingIntent
        )

        android.util.Log.d("AlarmHelper", "Reminder scheduled for: ${reminderTime.time}")
    }

    fun cancelReminder(subscriptionId: Int) {
        val intent = Intent(context, ReminderReceiver::class.java).apply {
            action = REMINDER_ACTION
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            subscriptionId,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.cancel(pendingIntent)
        android.util.Log.d("AlarmHelper", "Reminder cancelled for subscription ID: $subscriptionId")
    }

    private fun calculateReminderTime(subscriptionItem: SubscriptionItem): Calendar {
        val calendar = Calendar.getInstance()
        calendar.time = subscriptionItem.pay

        // Parse the reminder preference (e.g., "1 day before", "1 hour before")
        val reminderText = subscriptionItem.remind.lowercase()
        when {
            reminderText.contains("day") -> {
                val days = reminderText.filter { it.isDigit() }.toIntOrNull() ?: 1
                calendar.add(Calendar.DAY_OF_MONTH, -days)
            }
            reminderText.contains("hour") -> {
                val hours = reminderText.filter { it.isDigit() }.toIntOrNull() ?: 1
                calendar.add(Calendar.HOUR_OF_DAY, -hours)
            }
            else -> {
                // Default to 1 day before if format is not recognized
                calendar.add(Calendar.DAY_OF_MONTH, -1)
            }
        }

        return calendar
    }
} 