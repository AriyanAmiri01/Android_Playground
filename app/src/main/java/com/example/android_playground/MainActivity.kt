package com.example.android_playground

import android.os.Bundle                        // Bundle: container for storing key-value data, often used for saving/restoring Activity state
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge       // Function that allows the Activity UI to draw behind system bars
import androidx.appcompat.app.AppCompatActivity // Base Activity class that MainActivity inherits from
import androidx.core.view.ViewCompat            // Utility/helper for working with Android Views with compatibility across Android versions [IDK]
import androidx.core.view.WindowInsetsCompat    // Represents information about system UI areas, such as status/navigation bars

import androidx.core.view.GravityCompat
import com.google.android.material.navigation.NavigationView
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.appbar.MaterialToolbar

import androidx.navigation.fragment.NavHostFragment
import com.bumptech.glide.Glide
import com.example.android_playground.repository.AuthRepository

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        // @description: set and configure UI connection

        // Set the parent state
        super.onCreate(savedInstanceState)

        // Set activity content view
        enableEdgeToEdge()
        setContentView(R.layout.x00_main_activity)


        // Get drawer instances
        val drawerLayout = findViewById<DrawerLayout>(R.id.drawerLayout)
        val toolbar = findViewById<MaterialToolbar>(R.id.topAppBar)
        val navigationView = findViewById<NavigationView>(R.id.navigationView)
        val headerView = navigationView.getHeaderView(0)
        val navProfileImage = headerView.findViewById<ImageView>(R.id.navProfileImage)
        val navUsername = headerView.findViewById<TextView>(R.id.navUsername)
        val navEmail = headerView.findViewById<TextView>(R.id.navEmail)


        // Get current authenticated user
        val repository = AuthRepository()
        val user = repository.getCurrentUser()
        if (user != null) {
            navUsername.text = user.displayName ?: "User"
            navEmail.text = user.email ?: ""
            if (user.photoUrl != null) {
                Glide.with(this)
                    .load(user.photoUrl)
                    .into(navProfileImage)
            }
        }


        // Get the NavController and navigate to the correct page
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainer) as NavHostFragment
        val navController = navHostFragment.navController
        if (savedInstanceState == null && user != null) { navController.navigate(R.id.nav_workout) }


        // Hamburger icon and menu drawer
        toolbar.setNavigationOnClickListener { drawerLayout.openDrawer(GravityCompat.START) }
        navigationView.setNavigationItemSelectedListener { item ->
            // Navigate
            when (item.itemId) {
                R.id.nav_workout -> { navController.navigate(R.id.nav_workout) }
                R.id.nav_exercises -> { navController.navigate(R.id.nav_exercises) }
                R.id.nav_history -> { navController.navigate(R.id.nav_history) }
                R.id.nav_active_workout -> { navController.navigate(R.id.nav_active_workout) }
                R.id.nav_profile -> { navController.navigate(R.id.nav_profile) }
            }

            // Close drawer after selecting an item
            drawerLayout.closeDrawer(GravityCompat.START)

            true
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
