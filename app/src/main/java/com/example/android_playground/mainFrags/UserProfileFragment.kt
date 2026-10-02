package com.example.android_playground.mainFrags

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.android_playground.R
import com.example.android_playground.repository.AuthRepository
import com.example.android_playground.repository.AuthViewModel
import com.example.android_playground.repository.AuthViewModelFactory

class UserProfileFragment : Fragment(R.layout.main_frag_user_profile){

    // For Firebase connection
    private lateinit var viewModel : AuthViewModel

    private lateinit var profileImage : ImageView

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
            factory
        )[AuthViewModel::class.java]

        // Get Views
        profileImage = view.findViewById(R.id.profileImage)
        val profileName = view.findViewById<TextView>(R.id.profileName)
        val profileUsername = view.findViewById<TextView>(R.id.profileUsername)
        val logoutButton = view.findViewById<Button>(R.id.logoutButton)
        val loginButton = view.findViewById<Button>(R.id.loginButton)
        val notLoggedInText = view.findViewById<TextView>(R.id.notLoggedInText)

        // Finish the GUI based on authentication state
        updateProfile(
            profileName,
            profileUsername,
            logoutButton,
            loginButton,
            notLoggedInText
        )
        // Profile  image update
        profileImage.setOnClickListener {
            if(viewModel.isUserLoggedIn()){
                profilePicturePicker.launch(
                    PickVisualMediaRequest(
                        ActivityResultContracts.PickVisualMedia.ImageOnly
                    )
                )
            }
        }

        // Implement logout button
        logoutButton.setOnClickListener {
            // Logout from the firebase
            viewModel.logoutUser()

            // Update the UI
            findNavController().navigate(
                R.id.loginFragment,
                null,
                NavOptions.Builder()
                    .setPopUpTo(R.id.nav_workout, true)
                    .build()
            )
        }

        // Implement login
        loginButton.setOnClickListener {
            // Update UI
            findNavController().navigate(
                R.id.loginFragment
            )
        }
    }


    private fun updateProfile(
        profileName : TextView,
        profileUsername : TextView,
        logoutButton : Button,
        loginButton : Button,
        notLoggedInText : TextView
    ){
        // Query the user datas

        val user = viewModel.getCurrentUser()

        if(user != null){
            profileName.visibility = View.VISIBLE
            profileUsername.visibility = View.VISIBLE
            logoutButton.visibility = View.VISIBLE
            loginButton.visibility = View.GONE
            notLoggedInText.visibility = View.GONE

            //profileName.text = user.displayName ?: "User"
            //profileUsername.text = user.email ?: "No email"

            if(user.photoUrl != null){
                Glide.with(this)
                    .load(user.photoUrl)
                    .into(profileImage)
            }
        }else{
            // User is not authenticated
            profileName.visibility = View.GONE
            profileUsername.visibility = View.GONE
            logoutButton.visibility = View.GONE
            loginButton.visibility = View.VISIBLE
            notLoggedInText.visibility = View.VISIBLE
        }
    }


    // PICTURE PICKER
    private val profilePicturePicker =
        registerForActivityResult(  // Launch another activity and later give me its result
            ActivityResultContracts.PickVisualMedia()) { uri: Uri? ->   // Let the user select the media
            if (uri != null) {

                viewModel.uploadProfilePicture(uri) { success, error ->

                    if (success) {
                        // Show the selected image immediately
                        profileImage.setImageURI(uri)
                    } else {
                        // Upload failed
                        // We will handle the error properly later
                    }
                }
            }
        }
}