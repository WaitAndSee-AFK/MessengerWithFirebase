package com.example.messenger

import android.content.Context
import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class UsersViewModel() : ViewModel() {
    private val _user: MutableLiveData<FirebaseUser> = MutableLiveData()
    val user: LiveData<FirebaseUser>
        get() = _user

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    fun logout() {
        auth.signOut()
    }

    fun newIntent(context: Context): Intent {
        return Intent(context, MainActivity::class.java)
    }

    init {
        auth.addAuthStateListener { firebaseAuth ->
            _user.value = firebaseAuth.currentUser
        }
    }
}