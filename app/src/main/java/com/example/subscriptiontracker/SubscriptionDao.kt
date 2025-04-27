package com.example.subscriptiontracker

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface SubscriptionDao {
    @Query("SELECT * FROM subscription_items")
    fun getAllSubscriptions(): LiveData<List<SubscriptionItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: SubscriptionItem)

    @Update
    suspend fun update(item: SubscriptionItem)

    @Delete
    suspend fun delete(item: SubscriptionItem)
}

