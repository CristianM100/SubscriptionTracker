package com.example.subscriptiontracker

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.room.Room
import com.google.android.material.navigation.NavigationView
import androidx.appcompat.widget.Toolbar  // Import Toolbar for setting it as ActionBar

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: SubscriptionViewModel
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private val NOTIFICATION_PERMISSION_CODE = 123

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Request notification permission for Android 13 and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    NOTIFICATION_PERMISSION_CODE
                )
            }
        }

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        // Set up NavController
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        // Initial AppBarConfiguration for non-home fragments
        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.nav_home), // Home is the only top-level fragment
            null // No drawer layout for home fragment
        )

        // Set up the action bar with NavController
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration)

        // Listen for destination changes
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.nav_home) {
                // Hide the back arrow (ensure it's only hidden for the home page)
                supportActionBar?.setDisplayHomeAsUpEnabled(false)
                supportActionBar?.setHomeButtonEnabled(false)
            } else {
                // Show the back arrow for other fragments
                supportActionBar?.setDisplayHomeAsUpEnabled(true)
                supportActionBar?.setHomeButtonEnabled(true)
            }
        }

        // Initialize Room database
        val database = Room.databaseBuilder(
            applicationContext,
            SubscriptionDatabase::class.java,
            "subscription_database"
        ).allowMainThreadQueries().build()

        val repository = SubscriptionRepository(database.subscriptionDao())
        val factory = SubscriptionViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[SubscriptionViewModel::class.java]
        viewModel.initialize(application)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            NOTIFICATION_PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Notification permission granted", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Notification permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        // Get the current destination id from the NavController
        val currentFragmentId = navController.currentDestination?.id

        return when {
            // If we're on MainFragment, navigate to SubscriptionCategoryActivity
            currentFragmentId == R.id.mainFragment -> {
                // Start SubscriptionCategoryActivity when the back arrow is pressed from MainFragment
                val intent = Intent(this, SubscriptionCategoryActivity::class.java)
                startActivity(intent)
                true // Indicating that the navigation action was handled
            }
            // If we're on another fragment, navigate back to the Main Fragment (R.id.mainFragment)
            else -> {
                // Go back to the previous fragment using NavController
                navController.navigateUp()
                true
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_help -> {
                Toast.makeText(this, "Help clicked", Toast.LENGTH_SHORT).show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}


