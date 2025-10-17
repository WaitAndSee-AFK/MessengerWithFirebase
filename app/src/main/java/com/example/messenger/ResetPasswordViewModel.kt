package com.example.messenger

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class ResetPasswordViewModel() : ViewModel() {
    private val _error = MutableLiveData<String>()
    val error: LiveData<String>
        get() = _error
    private val _isSuccess = MutableLiveData<Boolean>()
    val isSuccess: LiveData<Boolean>
        get() = _isSuccess
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    fun resetPassword(email: String) {
        auth.sendPasswordResetEmail(email)
            .addOnSuccessListener { _isSuccess.value = true }
            .addOnFailureListener { exception ->  _error.value = exception.toString()}
    }
}