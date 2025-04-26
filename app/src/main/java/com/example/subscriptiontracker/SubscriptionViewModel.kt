package com.example.subscriptiontracker

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SubscriptionViewModel : ViewModel() {

    private val _items = MutableLiveData<List<SubscriptionItem>>()
    val items: LiveData<List<SubscriptionItem>> get() = _items

    private val itemList = mutableListOf<SubscriptionItem>()

    init {
        _items.value = itemList
    }

    fun addItem(item: SubscriptionItem) {
        itemList.add(item)
        _items.value = itemList
    }

    fun updateItem(position: Int, item: SubscriptionItem) {
        itemList[position] = item
        _items.value = itemList
    }

    fun deleteItem(position: Int) {
        itemList.removeAt(position)
        _items.value = itemList
    }

    fun getItem(position: Int): SubscriptionItem {
        return itemList[position]
    }
}

