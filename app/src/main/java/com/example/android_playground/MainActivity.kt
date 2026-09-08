package com.example.android_playground

import android.os.Bundle                        // Bundle: container for storing key-value data, often used for saving/restoring Activity state
import androidx.activity.enableEdgeToEdge       // Function that allows the Activity UI to draw behind system bars
import androidx.appcompat.app.AppCompatActivity // Base Activity class that MainActivity inherits from
import androidx.core.view.ViewCompat            // Utility/helper for working with Android Views with compatibility across Android versions [IDK]
import androidx.core.view.WindowInsetsCompat    // Represents information about system UI areas, such as status/navigation bars
import com.google.android.material.bottomnavigation.BottomNavigationView // Material Design bottom navigation UI component
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.navigation.fragment.NavHostFragment

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        // @description: set and configure UI connection

        // Set the parent state
        super.onCreate(savedInstanceState)

        // Set application boundaries
        enableEdgeToEdge()

        // User interface connection
        setContentView(R.layout.activity_main)

        // Get BottomNavigationView
        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottomNavigation)

        // Add navigation system
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainer) as NavHostFragment
        val navController = navHostFragment.navController
        bottomNavigation.setupWithNavController(navController)


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
