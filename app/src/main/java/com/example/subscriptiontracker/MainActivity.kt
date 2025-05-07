package com.example.subscriptiontracker

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.room.Room


class MainActivity : AppCompatActivity() {

    lateinit var viewModel: SubscriptionViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Set up the toolbar
   /*     val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

// IMPORTANT: Wait until the view is fully inflated before getting the NavController
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

// Define top-level destinations for AppBarConfiguration
        val appBarConfig = AppBarConfiguration(setOf(R.id.mainFragment))

// Connect the NavController with the Toolbar
        setupActionBarWithNavController(navController, appBarConfig)

// Observe NavController destination changes to update Toolbar title dynamically
        navController.addOnDestinationChangedListener { _, destination, _ ->
            // Update Toolbar title based on fragment's label
            supportActionBar?.title = destination.label
        }
*/
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

  /*  override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }*/
}







/* setSupportActionBar(findViewById(R.id.toolbar))
       val navController = findNavController(R.id.nav_host_fragment)
       val config = AppBarConfiguration(navController.graph)

       findViewById<Toolbar>(R.id.toolbar).setupWithNavController(navController, config);
*/
/*  send_button = findViewById(R.id.send_button_id)

  val intent = Intent(this, SubscriptionCategoryActivity::class.java)
  startActivity(intent)*/