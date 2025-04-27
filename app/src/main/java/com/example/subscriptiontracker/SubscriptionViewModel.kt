package com.example.subscriptiontracker

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import androidx.lifecycle.*


class SubscriptionViewModel(private val repository: SubscriptionRepository) : ViewModel() {

    val items: LiveData<List<SubscriptionItem>> = repository.allSubscriptions

    fun addItem(item: SubscriptionItem) = viewModelScope.launch {
        repository.insert(item)
    }

    fun updateItem(item: SubscriptionItem) = viewModelScope.launch {
        repository.update(item)
    }

    fun deleteItem(item: SubscriptionItem) = viewModelScope.launch {
        repository.delete(item)
    }
}




