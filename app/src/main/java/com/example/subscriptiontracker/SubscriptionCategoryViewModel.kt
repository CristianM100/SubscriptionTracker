package com.example.subscriptiontracker

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.map


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

    // Grouped data to use in a pie chart or summary view
    val groupedByCategoryAmount = repository.getAllSubscriptions().map { list ->
        list.groupBy { it.cat }
            .mapValues { entry -> entry.value.sumOf { it.amount } }
    }

}



