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
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch



class K00_LoginFragment : Fragment(R.layout.x01_main_frag_login) {

    private lateinit var viewModel : AuthViewModel
    private lateinit var credentialManager: CredentialManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // Call every child's onViewCreated function
        super.onViewCreated(view, savedInstanceState)

        // Add Firebase repository
        val repository = AuthRepository()
        val factory = AuthViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[AuthViewModel::class.java]


        // Google
        credentialManager = CredentialManager.create(requireContext())

        // Get the input fields
        val emailEditText = view.findViewById<EditText>(R.id.emailEditText)
        val passwordEditText = view.findViewById<EditText>(R.id.passwordEditText)
        val loginButton = view.findViewById<Button>(R.id.loginButton)
        val googleSignInButton = view.findViewById<Button>(R.id.googleSignInButton)
        val registerTextView = view.findViewById<TextView>(R.id.registerTextView)

        // Add button event
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
        googleSignInButton.setOnClickListener {
            signInWithGoogle()
        }
        registerTextView.setOnClickListener {
            findNavController().navigate(
                R.id.action_loginFragment_to_registerFragment
            )
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


    private fun signInWithGoogle() {

        val googleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(getString(R.string.default_web_client_id))
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        viewLifecycleOwner.lifecycleScope.launch {

            try {

                val result = credentialManager.getCredential(
                    context = requireActivity(),
                    request = request
                )

                val credential = result.credential

                if (
                    credential.type ==
                    GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
                ) {

                    val googleIdTokenCredential =
                        GoogleIdTokenCredential.createFrom(
                            credential.data
                        )

                    val idToken = googleIdTokenCredential.idToken

                    // Send Google's token to Firebase
                    viewModel.loginWithGoogle(idToken)
                }

            } catch (e: GetCredentialException) {

                Toast.makeText(
                    requireContext(),
                    "Google Sign-In failed: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }


}