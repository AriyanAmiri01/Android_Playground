package com.example.android_playground.Others

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.android_playground.R
import com.example.android_playground.repository.AuthRepository
import com.example.android_playground.repository.AuthViewModel
import com.example.android_playground.repository.AuthViewModelFactory

class UserProfileFragment : Fragment(R.layout.fragment_user_profile){
    // For Firebase connection
    private lateinit var viewModel : AuthViewModel;

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // Call the childern proccesses
        super.onViewCreated(view, savedInstanceState)

        // Create the repository
        val repository = AuthRepository();

        // Create the factory
        val factory = AuthViewModelFactory(repository);

        // Get the viewModel
        viewModel = ViewModelProvider(
            this,
            factory)[AuthViewModel::class.java]

        // Get the logout button
        val logoutButton = view.findViewById<Button>(R.id.logoutButton)

        logoutButton.setOnClickListener {
            // Log the user out
            viewModel.logoutUser()

            // Navigating back to the login fragment and clearing the backstack
            findNavController().navigate(
                R.id.loginFragment,
                null,
                androidx.navigation.NavOptions.Builder()
                    .setPopUpTo(R.id.nav_library, true)
                    .build()
            )
        }
    }
}