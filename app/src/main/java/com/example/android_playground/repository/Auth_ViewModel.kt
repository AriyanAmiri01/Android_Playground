package com.example.android_playground.repository

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.android_playground.data.UserProfile



class Auth_ViewModel(
    private val repository: Auth_Repository
) : ViewModel(){

    // Livedatas are values that notify their thread whenever their values get changed
    private val _authResult = MutableLiveData<Pair<Boolean, String?>>() // The pair boolean string? is for exception handling
    // the underline is a kotlin convension for naming mutable private datas
    private val _userProfile =
        MutableLiveData<UserProfile?>()

    val userProfile: LiveData<UserProfile?>
        get() = _userProfile

    val authResult: LiveData<Pair<Boolean, String?>> get() = _authResult


    // AUTHENTICATION
    /////////////////////////////
    fun registerUser(
        firstName: String,
        lastName: String,
        username: String,
        email: String,
        password: String
    ) {

        repository.registerUser(
            firstName,
            lastName,
            username,
            email,
            password
        ) { success, error ->

            _authResult.value =
                Pair(success, error)
        }
    }
    fun loginUser(email: String, password: String) {
        repository.loginUser(email, password) { success, error ->
            _authResult.value = Pair(success, error)
        }
    }
    fun loginWithGoogle(idToken: String) {
        repository.loginWithGoogle(idToken) { success, error ->
            _authResult.value = Pair(success, error)
        }
    }
    fun loadUserProfile() {

        repository.getUserProfile { profile, error ->

            if (profile != null) {
                _userProfile.value = profile
            }
        }
    }

    fun logoutUser(){
        repository.logoutUser()
    }
    fun isUserLoggedIn() : Boolean{
        return repository.getCurrentUser() != null
    }
    fun getCurrentUser() = repository.getCurrentUser()

    
    // STORAGE
    ////////////////////////////////
    fun uploadProfilePicture(imageUri: Uri, onResult: (Boolean, String?) -> Unit) {
        repository.uploadProfilePicture(imageUri, onResult)
    }


}
