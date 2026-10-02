package com.example.android_playground.authenticate


import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.android_playground.R
import com.example.android_playground.repository.AuthRepository
import com.example.android_playground.repository.AuthViewModel
import com.example.android_playground.repository.AuthViewModelFactory
import androidx.navigation.fragment.findNavController

class LoginFragment : Fragment(R.layout.sub_frag_login) {

    private lateinit var viewModel : AuthViewModel


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // Call every child's onViewCreated function
        super.onViewCreated(view, savedInstanceState)

        // Add Firebase repository
        val repository = AuthRepository()

        // Add AuthViewModelFactory
        val factory = AuthViewModelFactory(repository)

        // Create the viewModel
        viewModel = ViewModelProvider(
            this,
            factory
        )[AuthViewModel::class.java]


        // Get the input fields
        val emailEditText = view.findViewById<EditText>(R.id.emailEditText)
        val passwordEditText = view.findViewById<EditText>(R.id.passwordEditText)
        val loginButton = view.findViewById<Button>(R.id.loginButton)

        // Add login button event
        loginButton.setOnClickListener {
            // Extract the data
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString()

            // Check for empty inputs
            if(email.isEmpty() || password.isEmpty()){
                Toast.makeText(
                    requireContext(),
                    "Please enter email and password",
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }
            // Login the user using the viewModel
            viewModel.loginUser(email, password)
        }

        // Add the observer for the authResult livedata for debugging info
        viewModel.authResult.observe(viewLifecycleOwner) { result ->

            val success = result.first
            val error = result.second

            if (success) {

                Toast.makeText(
                    requireContext(),
                    "Login successful :)",
                    Toast.LENGTH_SHORT
                ).show()

                findNavController().navigate(
                    R.id.action_loginFragment_to_nav_workout
                )



                // We will navigate to the main application here later.

            } else {

                Toast.makeText(
                    requireContext(),
                    error ?: "Login failed",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }



}