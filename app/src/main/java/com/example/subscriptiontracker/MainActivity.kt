package com.example.subscriptiontracker

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room


class MainActivity : AppCompatActivity() {

    lateinit var viewModel: SubscriptionViewModel

  //  lateinit var send_button: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

      /*  send_button = findViewById(R.id.send_button_id)

        val intent = Intent(this, SubscriptionCategoryActivity::class.java)
        startActivity(intent)*/


        val database = Room.databaseBuilder(
            applicationContext,
            SubscriptionDatabase::class.java,
            "subscription_database"
        ).allowMainThreadQueries() // Only for quick tests!
            .build()

        val repository = SubscriptionRepository(database.subscriptionDao())
        val factory = SubscriptionViewModelFactory(repository)

        viewModel = ViewModelProvider(this, factory)[SubscriptionViewModel::class.java]
    }
}

