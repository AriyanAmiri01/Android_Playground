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
import com.example.android_playground.repository.Auth_Repository
import com.example.android_playground.repository.Auth_ViewModel
import com.example.android_playground.repository.Auth_ViewModelFactory

class K01_RegisterFragment : Fragment(R.layout.x07_main_frag_register){
    private lateinit var viewModel : Auth_ViewModel
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // Calling the child's onViewCreated functions
        super.onViewCreated(view, savedInstanceState)

        // Firebase communication
        val repository = Auth_Repository()
        val factory = Auth_ViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[Auth_ViewModel::class.java]


        // Get the input fields
        val firstNameEditText = view.findViewById<EditText>(R.id.firstNameEditText)
        val lastNameEditText = view.findViewById<EditText>(R.id.lastNameEditText)
        val usernameEditText = view.findViewById<EditText>(R.id.usernameEditText)
        val emailEditText = view.findViewById<EditText>(R.id.emailEditText)
        val passwordEditText = view.findViewById<EditText>(R.id.passwordEditText)
        val registerButton = view.findViewById<Button>(R.id.registerButton)
        val loginTextView = view.findViewById<TextView>(R.id.loginTextView)


        //  Add the register button event listener
        registerButton.setOnClickListener {
            val firstName = firstNameEditText.text.toString().trim()
            val lastName = lastNameEditText.text.toString().trim()
            val username = usernameEditText.text.toString().trim()
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString()

            if (firstName.isEmpty() || lastName.isEmpty() || username.isEmpty() || email.isEmpty() || password.isEmpty()) {

                Toast.makeText(
                    requireContext(),
                    "Please fill in all fields",
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }

            // Register user
            viewModel.registerUser(
                firstName,
                lastName,
                username,
                email,
                password
            )
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