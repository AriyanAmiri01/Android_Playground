package com.example.android_playground

import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.GravityCompat
import com.google.android.material.navigation.NavigationView
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.appbar.MaterialToolbar
import androidx.navigation.fragment.NavHostFragment
import com.bumptech.glide.Glide
import com.example.android_playground.repository.AuthRepository


class MainActivity : AppCompatActivity() {

    private lateinit var navProfileImage: ImageView
    private lateinit var navUsername: TextView
    private lateinit var navEmail: TextView

    override fun onCreate(savedInstanceState: Bundle?) {

        // Set the parent state
        super.onCreate(savedInstanceState)


        // Set content view
        enableEdgeToEdge()
        setContentView(R.layout.x00_main_activity)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        // Get drawer instances
        val drawerLayout = findViewById<DrawerLayout>(R.id.drawerLayout)
        val toolbar = findViewById<MaterialToolbar>(R.id.topAppBar)
        val navigationView = findViewById<NavigationView>(R.id.navigationView)
        val headerView = navigationView.getHeaderView(0)
        navProfileImage = headerView.findViewById<ImageView>(R.id.navProfileImage)
        navUsername = headerView.findViewById<TextView>(R.id.navUsername)
        navEmail = headerView.findViewById<TextView>(R.id.navEmail)


        // Get current user, display username with profile
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


        //  Get navigation instances
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainer) as NavHostFragment

        val navController = navHostFragment.navController

        if (savedInstanceState == null) {

            val navGraph = navController.navInflater.inflate(R.navigation.nav_graph)

            if (user != null) {
                navGraph.setStartDestination(R.id.nav_workout)
            } else {
                navGraph.setStartDestination(R.id.loginFragment)
            }

            navController.graph = navGraph
        }

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

    }

    // Update only the drawer profile image
    fun updateDrawerProfileImage(uri: Uri) {

        Glide.with(this)
            .load(uri)
            .into(navProfileImage)
    }
}