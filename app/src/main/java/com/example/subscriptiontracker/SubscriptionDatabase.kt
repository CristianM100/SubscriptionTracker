package com.example.subscriptiontracker

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase


@Database(entities = [SubscriptionItem::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class SubscriptionDatabase : RoomDatabase() {

    abstract fun subscriptionDao(): SubscriptionDao

    companion object {
        @Volatile
        private var INSTANCE: SubscriptionDatabase? = null

        fun getDatabase(context: Context): SubscriptionDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SubscriptionDatabase::class.java,
                    "subscription_database"
                )
                    .fallbackToDestructiveMigration() // Forces deletion of the database if schema changes
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}


