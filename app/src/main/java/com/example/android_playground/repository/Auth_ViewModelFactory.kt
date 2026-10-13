package com.example.android_playground.repository



import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


class Auth_ViewModelFactory (
    private val repository : Auth_Repository
): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(Auth_ViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return Auth_ViewModel(repository) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }

}