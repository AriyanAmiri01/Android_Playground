package com.example.android_playground

import android.os.Bundle                        // Bundle: container for storing key-value data, often used for saving/restoring Activity state
import androidx.activity.enableEdgeToEdge       // Function that allows the Activity UI to draw behind system bars
import androidx.appcompat.app.AppCompatActivity // Base Activity class that MainActivity inherits from
import androidx.core.view.ViewCompat            // Utility/helper for working with Android Views with compatibility across Android versions [IDK]
import androidx.core.view.WindowInsetsCompat    // Represents information about system UI areas, such as status/navigation bars

import androidx.core.view.GravityCompat
import com.google.android.material.navigation.NavigationView
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.appbar.MaterialToolbar

import androidx.navigation.fragment.NavHostFragment
import com.example.android_playground.repository.AuthRepository
import com.example.android_playground.repository.AuthViewModelFactory

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        // @description: set and configure UI connection

        // Set the parent state
        super.onCreate(savedInstanceState)

        // Set activity content view
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)


        // Get drawer instances
        val drawerLayout = findViewById<DrawerLayout>(R.id.drawerLayout)
        val toolbar = findViewById<MaterialToolbar>(R.id.topAppBar)
        val navigationView = findViewById<NavigationView>(R.id.navigationView)

        // Get the NavController
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainer) as NavHostFragment
        val navController = navHostFragment.navController


        // Open drawer when hamburger icon is clicked
        toolbar.setNavigationOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        // Handle drawer menu selections
        navigationView.setNavigationItemSelectedListener { item ->

            when (item.itemId) {

                R.id.nav_workout -> {
                    navController.navigate(R.id.nav_workout)
                }

                R.id.nav_exercises -> {
                    navController.navigate(R.id.nav_exercises)
                }

                R.id.nav_history -> {
                    navController.navigate(R.id.nav_history)
                }

                R.id.nav_timer -> {
                    navController.navigate(R.id.nav_timer)
                }

                R.id.nav_profile -> {
                    navController.navigate(R.id.nav_profile)
                }
            }

            // Close drawer after selecting an item
            drawerLayout.closeDrawer(GravityCompat.START)

            true
        }




        // State check
        val repository = AuthRepository()
        if (repository.getCurrentUser() != null) {
            navController.navigate(R.id.nav_workout)
        }else{
            navController.navigate(R.id.loginFragment)
        }

        // Make sure no content goes under hte system bar
        ViewCompat.setOnApplyWindowInsetsListener(
            // Get the view
            findViewById(R.id.main))
            { v, insets -> // insets is the extra padding to consider to avoid object to go on top of another
                val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
                insets
            }
    }

}
