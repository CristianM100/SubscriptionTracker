package com.example.subscriptiontracker


data class SubscriptionItem(
    val name: String,
    val desc: String,
    val cat: String,
    val pay: String,
    val cycle: String,
    val amount: String,
    val currency: String,
    val payMet: String,
    val remind: String
)
