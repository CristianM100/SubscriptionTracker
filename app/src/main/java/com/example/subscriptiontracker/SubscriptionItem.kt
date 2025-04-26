package com.example.subscriptiontracker

import java.util.Date


data class SubscriptionItem (
    val name: String,
    val desc: String,
    val cat: String,
    val pay: Date,
    val cycle: String,
    val amount: Double,
    val currency: String,
    val payMet: String,
    val remind: String
)
