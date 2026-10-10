package com.example.android_playground.mainFrags

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.android_playground.R
import com.example.android_playground.repository.AuthRepository
import com.example.android_playground.repository.AuthViewModel
import com.example.android_playground.repository.AuthViewModelFactory

class K01_RegisterFragment : Fragment(R.layout.x07_main_frag_register){
    private lateinit var viewModel : AuthViewModel
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // Calling the child's onViewCreated functions
        super.onViewCreated(view, savedInstanceState)

        // Add the repository
        val repository = AuthRepository()

        // Add the AuthViewModelFactory
        val factory = AuthViewModelFactory(repository)

        // Create the viewModel
        viewModel = ViewModelProvider(
            this,
            factory
        )[AuthViewModel::class.java]

        // Get the input fields
        val emailEditText = view.findViewById<EditText>(R.id.emailEditText)
        val passwordEditText = view.findViewById<EditText>(R.id.passwordEditText)
        val registerButton = view.findViewById<Button>(R.id.registerButton)
        val loginTextView = view.findViewById<TextView>(R.id.loginTextView)

        //  Add the register button event listener
        registerButton.setOnClickListener {

            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(
                    requireContext(),
                    "Please enter email and password",
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }

            viewModel.registerUser(email, password)
        }
        loginTextView.setOnClickListener {
            findNavController().navigate(
                R.id.action_registerFragment_to_loginFragment
            )
        }


        // Get debug information from the authResult livedata
        viewModel.authResult.observe(viewLifecycleOwner) { result ->

            val success = result.first
            val error = result.second

            if (success) {

                Toast.makeText(
                    requireContext(),
                    "Account created successfully",
                    Toast.LENGTH_SHORT
                ).show()

                findNavController().navigate(
                    R.id.action_registerFragment_to_nav_workout
                )

            } else {

                Toast.makeText(
                    requireContext(),
                    error ?: "Registration failed",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }



}