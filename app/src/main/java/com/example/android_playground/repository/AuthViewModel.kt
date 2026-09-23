package com.example.android_playground.repository

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel




class AuthViewModel(
    private val repository: AuthRepository
) : ViewModel(){

    // Livedatas are values that notify their thread whenever their values get changed
    private val _authResult = MutableLiveData<Pair<Boolean, String?>>() // The pair boolean string? is for exception handling
    // the underline is a kotlin convension for naming mutable private datas


    val authResult: LiveData<Pair<Boolean, String?>> get() = _authResult


    // AUTHENTICATION
    /////////////////////////////
    fun registerUser(
        email : String,
        password : String
    ){
        repository.registerUser(email, password){ success, error ->
            _authResult.value = Pair(success, error)
        }
    }

    fun loginUser(email: String, password: String) {
        repository.loginUser(email, password) { success, error ->
            _authResult.value = Pair(success, error)
        }
    }

    fun logoutUser(){
        repository.logoutUser()
    }



    // STATE MANAGEMENT
    ////////////////////////////////
    fun isUserLoggedIn() : Boolean{
        return repository.getCurrentUser() != null
    }

    fun getCurrentUser() = repository.getCurrentUser()

    // STORAGE
    ////////////////////////////////
    fun uploadProfilePicture(
        imageUri: Uri,
        onResult: (Boolean, String?) -> Unit
    ) {
        repository.uploadProfilePicture(imageUri, onResult)
    }


}
