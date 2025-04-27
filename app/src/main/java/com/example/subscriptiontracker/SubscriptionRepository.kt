package com.example.subscriptiontracker

import androidx.lifecycle.LiveData


class SubscriptionRepository(private val dao: SubscriptionDao) {

    val allSubscriptions: LiveData<List<SubscriptionItem>> = dao.getAllSubscriptions()

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
