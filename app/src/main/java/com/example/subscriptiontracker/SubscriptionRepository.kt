package com.example.subscriptiontracker

import android.app.Application
import androidx.lifecycle.LiveData


class SubscriptionRepository(private val dao: SubscriptionDao) {

    fun getAllSubscriptions(): LiveData<List<SubscriptionItem>> = dao.getAllSubscriptions()

    fun getSubscriptionsByCategory(): LiveData<List<SubscriptionItem>> = dao.getSubscriptionsByCategory()

    fun getSubscriptionsByPaymentMethod(): LiveData<List<SubscriptionItem>> = dao.getSubscriptionsByPaymentMethod()

    suspend fun insert(item: SubscriptionItem) {
        dao.insert(item)
    }

    suspend fun update(item: SubscriptionItem) {
        dao.update(item)
    }

    suspend fun delete(item: SubscriptionItem) {
        dao.delete(item)
    }
}
