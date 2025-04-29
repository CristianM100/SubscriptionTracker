package com.example.subscriptiontracker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


class SubscriptionViewModelFactory(private val repository: SubscriptionRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SubscriptionViewModel::class.java)) {
            return SubscriptionViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}





