package com.example.subscriptiontracker.utils

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.example.subscriptiontracker.data.model.SubscriptionItem
import java.util.Calendar
import java.util.Locale


class AlarmHelper(private val context: Context) {

    companion object {
        const val REMINDER_ACTION = "com.example.subscriptiontracker.REMINDER_ACTION"
    }

    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    fun scheduleReminder(subscriptionItem: SubscriptionItem) {
        val reminderTime = calculateReminderTime(subscriptionItem)

        val intent = Intent(context, ReminderReceiver::class.java).apply {
            action = REMINDER_ACTION
            putExtra("subscriptionId", subscriptionItem.subscriptionId)
            putExtra("subscriptionName", subscriptionItem.name)
            putExtra("subscriptionAmount", String.format(Locale.US, "%.2f", subscriptionItem.amount))
            putExtra("subscriptionCurrency", subscriptionItem.currency)
        }

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
    }

    private fun calculateReminderTime(subscriptionItem: SubscriptionItem): Calendar {
        val calendar = Calendar.getInstance()
        calendar.time = subscriptionItem.pay

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
                calendar.add(Calendar.DAY_OF_MONTH, -1)
            }
        }
        return calendar
    }
} 