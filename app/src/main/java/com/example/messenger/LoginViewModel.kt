package com.example.messenger

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class LoginViewModel() : ViewModel() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val _user: MutableLiveData<FirebaseUser> = MutableLiveData()
    val user: LiveData<FirebaseUser>
        get() = _user
    private val _error: MutableLiveData<String> = MutableLiveData()
    val error: LiveData<String>
        get() = _error

    fun login(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener { authResult ->
                _user.value = authResult.user
            }
            .addOnFailureListener { err ->
                _error.value = err.toString()
            }
    }

    init {
        auth.addAuthStateListener { firebaseAuth ->
            if (firebaseAuth.currentUser != null) {
                _user.value = firebaseAuth.currentUser
            }
        }
    }
}