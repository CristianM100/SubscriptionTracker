package com.example.subscriptiontracker

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import androidx.lifecycle.*


class SubscriptionViewModel(private val repository: SubscriptionRepository) : ViewModel() {

    val items: LiveData<List<SubscriptionItem>> = repository.getAllSubscriptions()
    private lateinit var alarmHelper: AlarmHelper

    fun initialize(application: Application) {
        alarmHelper = AlarmHelper(application)
    }

    fun addItem(item: SubscriptionItem) = viewModelScope.launch {
        repository.insert(item)
        scheduleReminder(item)
    }

    fun updateItem(item: SubscriptionItem) = viewModelScope.launch {
        repository.update(item)
        // Cancel existing reminder and schedule new one
        alarmHelper.cancelReminder(item.subscriptionId)
        scheduleReminder(item)
    }

    fun deleteItem(item: SubscriptionItem) = viewModelScope.launch {
        repository.delete(item)
        alarmHelper.cancelReminder(item.subscriptionId)
    }

    private fun scheduleReminder(item: SubscriptionItem) {
        if (item.remind.isNotEmpty()) {
            android.util.Log.d("SubscriptionViewModel", "Scheduling reminder for ${item.name} with amount: ${item.amount}")
            alarmHelper.scheduleReminder(item)
        }
    }
}




