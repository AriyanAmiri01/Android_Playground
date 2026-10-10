package com.example.android_playground.repository

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.tasks.await
import com.google.firebase.auth.GoogleAuthProvider



class AuthRepository {
    /// FIREBASE INSTANCES
    /////////////////////////////
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val firebaseStorage = FirebaseStorage.getInstance()


    /// AUTHENTICATION
    /////////////////////////////
    fun registerUser(email : String, password : String, onResult : (Boolean, String?) -> Unit){
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if( task.isSuccessful){
                    onResult(true, null)
                }else{
                    onResult(false,task.exception?.message)
                }
            }
    }
    fun loginUser(email: String, password: String, onResult: (Boolean, String?) -> Unit) {
        firebaseAuth
            .signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->

                if (task.isSuccessful) {
                    onResult(true, null)
                } else {
                    onResult(false, task.exception?.message)
                }
            }
    }
    fun logoutUser(){
        firebaseAuth.signOut()
    }
    fun getCurrentUser() : FirebaseUser?{
        return firebaseAuth.currentUser
    }


    /// STORAGE
    ////////////////////////////////
    fun uploadProfilePicture(imageUri : Uri, onResult : (Boolean, String?) -> Unit) {
        // Get the currently logged-in user
        val user = firebaseAuth.currentUser

        // Make sure a user is logged in
        if (user == null) {
            onResult(false, "No user is currently logged in")
            return
        }

        // Create a reference to:
        // Firebase Storage
        //      └── profilePictures
        //              └── USER_UID
        //                      └── profile.jpg
        val imageReference = firebaseStorage
            .reference
            .child("profilePictures/${user.uid}/profile.jpg")

        // Upload the image
        imageReference.putFile(imageUri)
            .addOnSuccessListener {

                // Upload finished successfully.
                // Now get the download URL.
                imageReference.downloadUrl
                    .addOnSuccessListener { downloadUri ->

                        // Create the Firebase profile update
                        val profileUpdates = UserProfileChangeRequest.Builder()
                            .setPhotoUri(downloadUri)
                            .build()

                        // Update the Firebase Authentication profile
                        user.updateProfile(profileUpdates)
                            .addOnSuccessListener {
                                onResult(true, null)
                            }
                            .addOnFailureListener { exception ->
                                onResult(false, exception.message)
                            }
                    }
                    .addOnFailureListener { exception ->
                        onResult(false, exception.message)
                    }
            }
            .addOnFailureListener { exception ->
                onResult(false, exception.message)
            }
    }

    suspend fun getDownloadUrl(storagePath : String) : String{
        val storageReference = firebaseStorage.getReferenceFromUrl(storagePath)
        return storageReference.downloadUrl.await().toString()
    }

    fun loginWithGoogle(
        idToken: String,
        onResult: (Boolean, String?) -> Unit
    ) {
        // Convert the Google ID token into a Firebase credential
        val credential = GoogleAuthProvider.getCredential(idToken, null)

        // Authenticate the Google account with Firebase
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener { task ->

                if (task.isSuccessful) {
                    onResult(true, null)
                } else {
                    onResult(false, task.exception?.message)
                }
            }
    }



    // GOOGLE STUFFS


}