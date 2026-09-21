package com.example.android_playground.repository

import com.google.firebase.auth.FirebaseAuth



class AuthRepository {

    private val firebaseAuth = FirebaseAuth.getInstance()

    fun registerUser(
        email : String,
        password : String,
        onResult : (Boolean, String?) -> Unit
    ){
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if( task.isSuccessful){
                    onResult(true, null)
                }else{
                    onResult(false,task.exception?.message)
                }
            }
    }

    fun loginUser(
        email: String,
        password: String,
        onResult: (Boolean, String?) -> Unit
    ) {
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

    fun getCurrentUser(){
        firebaseAuth.currentUser
    }



}