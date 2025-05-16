package com.example.subscriptiontracker.data.repository

import androidx.lifecycle.LiveData
import com.example.subscriptiontracker.data.database.SubscriptionDao
import com.example.subscriptiontracker.data.model.SubscriptionItem


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
