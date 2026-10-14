package com.example.android_playground.mainFrags


import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.PickVisualMediaRequest
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.android_playground.R
import com.example.android_playground.repository.Auth_Repository
import com.example.android_playground.repository.Auth_ViewModel
import com.example.android_playground.repository.Auth_ViewModelFactory
import android.app.Activity
import android.net.Uri
import androidx.activity.result.contract.ActivityResultContracts
import com.yalantis.ucrop.UCrop
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.example.android_playground.data.WorkoutDatabase


class K06_UserProfileFragment : Fragment(R.layout.x06_main_frag_user_profile){

    // Variables
    private lateinit var viewModel : Auth_ViewModel
    private lateinit var profileImage : ImageView
    private val cropImageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            if (data != null) {
                val resultUri: Uri? = UCrop.getOutput(data)
                if (resultUri != null) { uploadProfileImage(resultUri) }
            }
        }
    }
    private val profilePicturePicker = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri: Uri? ->
        if (uri != null) { startImageCrop(uri) }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        // Default onViewCreated constructor
        super.onViewCreated(view, savedInstanceState)


        // Get Views
        profileImage = view.findViewById(R.id.profileImage)
        val profileName = view.findViewById<TextView>(R.id.profileName)
        val profileUsername = view.findViewById<TextView>(R.id.profileUsername)
        val logoutButton = view.findViewById<Button>(R.id.logoutButton)
        val loginButton = view.findViewById<Button>(R.id.loginButton)
        val notLoggedInText = view.findViewById<TextView>(R.id.notLoggedInText)


        // Firebase communication
        val repository = Auth_Repository();
        val factory = Auth_ViewModelFactory(repository);
        viewModel = ViewModelProvider(this, factory)[Auth_ViewModel::class.java]


        updateProfile(profileName, profileUsername, logoutButton, loginButton, notLoggedInText)

        // Observe Firestore user profile
        viewModel.userProfile.observe(viewLifecycleOwner) { userProfile ->

            if (userProfile != null) {

                profileName.text =
                    "${userProfile.firstName} ${userProfile.lastName}"

                profileUsername.text =
                    "@${userProfile.username}"
            }
        }
        // Load user profile from Firestore
        if (viewModel.isUserLoggedIn()) {
            viewModel.loadUserProfile()
        }


        // Event Listeners
        profileImage.setOnClickListener {
            if(viewModel.isUserLoggedIn()){
                profilePicturePicker.launch(
                    PickVisualMediaRequest(
                        ActivityResultContracts.PickVisualMedia.ImageOnly
                    )
                )
            }
        }
        logoutButton.setOnClickListener {

            viewLifecycleOwner.lifecycleScope.launch {

                // Get the Room database
                val database =
                    WorkoutDatabase.getDatabase(requireContext())


                // Clear all local Room data
                withContext(Dispatchers.IO) {
                    database.clearAllTables()
                }


                // Logout from Firebase
                viewModel.logoutUser()


                // Navigate to login
                findNavController().navigate(
                    R.id.loginFragment,
                    null,
                    NavOptions.Builder()
                        .setPopUpTo(R.id.nav_workout, true)
                        .build()
                )
            }
        }
        loginButton.setOnClickListener {
            // Update UI
            findNavController().navigate(
                R.id.loginFragment
            )
        }
    }

    // Helper methods
    private fun updateProfile(profileName : TextView, profileUsername : TextView, logoutButton : Button, loginButton : Button, notLoggedInText : TextView){

        // 1. Query user data
        val user = viewModel.getCurrentUser()

        // 2. Update data based on state
        if(user != null){
            profileName.visibility = View.VISIBLE
            profileUsername.visibility = View.VISIBLE
            logoutButton.visibility = View.VISIBLE
            loginButton.visibility = View.GONE
            notLoggedInText.visibility = View.GONE
            if(user.photoUrl != null){
                Glide.with(this).load(user.photoUrl).into(profileImage)
            }

        } else{
            // User is not authenticated
            profileName.visibility = View.GONE
            profileUsername.visibility = View.GONE
            logoutButton.visibility = View.GONE
            loginButton.visibility = View.VISIBLE
            notLoggedInText.visibility = View.VISIBLE
        }
    }

    // Image Picker stuffs
    private fun startImageCrop(sourceUri: Uri) {
        // Function that tells uCrop where the original iamge is located

        // 1. Set target location and uCrop options
        val destinationUri = Uri.fromFile(java.io.File(requireContext().cacheDir, "cropped_profile_${System.currentTimeMillis()}.jpg"))
        val options = com.yalantis.ucrop.UCrop.Options().apply {
            setCompressionFormat(android.graphics.Bitmap.CompressFormat.JPEG)
            setCompressionQuality(90)
        }

        // 2. Create uCrop cropIntent
        val cropIntent = com.yalantis.ucrop.UCrop.of(sourceUri, destinationUri)
            .withAspectRatio(1f, 1f)
            .withMaxResultSize(512, 512)
            .withOptions(options)
            .getIntent(requireContext())

        // 3. Launch uCrop launcher
        cropImageLauncher.launch(cropIntent)
    }
    private fun uploadProfileImage(uri: Uri) {
        viewModel.uploadProfilePicture(uri) { success, error ->
            if (success) {
                Glide.with(this)
                    .load(uri)
                    .into(profileImage)
                (requireActivity() as K00_MainActivity).updateDrawerProfileImage(uri)
            } else {
                // Handle upload error later
            }
        }
    }

}