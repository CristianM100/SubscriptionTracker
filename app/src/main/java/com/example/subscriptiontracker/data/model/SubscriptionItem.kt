package com.example.subscriptiontracker.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.util.Date


@Parcelize
@Entity(tableName = "subscription_items")
data class SubscriptionItem(
 @PrimaryKey(autoGenerate = true)
 var subscriptionId: Int = 0,

 @ColumnInfo(name = "name")
 val name: String,

 @ColumnInfo(name = "desc")
 val desc: String,

 @ColumnInfo(name = "cat")
 val cat: String,

 @ColumnInfo(name = "pay")
 val pay: Date,

 @ColumnInfo(name = "cycle")
 val cycle: String,

 @ColumnInfo(name = "amount")
 val amount: Double,

 @ColumnInfo(name = "currency")
 val currency: String,

 @ColumnInfo(name = "pay_met")
 val payMet: String,

 @ColumnInfo(name = "remind")
 val remind: String
) : Parcelable
