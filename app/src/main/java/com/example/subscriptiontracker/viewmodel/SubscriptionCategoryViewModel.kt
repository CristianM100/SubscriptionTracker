package com.example.subscriptiontracker.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.example.subscriptiontracker.data.database.SubscriptionDatabase
import com.example.subscriptiontracker.data.model.SubscriptionItem
import com.example.subscriptiontracker.data.repository.SubscriptionRepository


class SubscriptionCategoryViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: SubscriptionRepository

    val subscriptionsByCategory: LiveData<List<SubscriptionItem>>
    val subscriptionsByPaymentMethod: LiveData<List<SubscriptionItem>>

    init {
        val dao = SubscriptionDatabase.getDatabase(application).subscriptionDao()
        repository = SubscriptionRepository(dao)

        subscriptionsByCategory = repository.getSubscriptionsByCategory()
        subscriptionsByPaymentMethod = repository.getSubscriptionsByPaymentMethod()
    }

    val groupedByCategoryAmount = repository.getAllSubscriptions().map { list ->
        list.groupBy { it.cat }
            .mapValues { entry -> entry.value.sumOf { it.amount } }
    }
}



