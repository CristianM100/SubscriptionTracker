package com.example.subscriptiontracker.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import androidx.lifecycle.*
import com.example.subscriptiontracker.data.model.SubscriptionItem
import com.example.subscriptiontracker.data.repository.SubscriptionRepository
import com.example.subscriptiontracker.utils.AlarmHelper


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
        alarmHelper.cancelReminder(item.subscriptionId)
        scheduleReminder(item)
    }

    fun deleteItem(item: SubscriptionItem) = viewModelScope.launch {
        repository.delete(item)
        alarmHelper.cancelReminder(item.subscriptionId)
    }

    private fun scheduleReminder(item: SubscriptionItem) {
        if (item.remind.isNotEmpty()) {
            alarmHelper.scheduleReminder(item)
        }
    }
}




