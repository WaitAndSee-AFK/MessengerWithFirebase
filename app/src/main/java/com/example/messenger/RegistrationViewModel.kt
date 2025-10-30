package com.example.messenger

import android.content.Context
import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class RegistrationViewModel() : ViewModel() {

    private val _error: MutableLiveData<String> = MutableLiveData()
    val error: LiveData<String>
        get() = _error
    private val _user: MutableLiveData<FirebaseUser> = MutableLiveData()
    val user: LiveData<FirebaseUser>
        get() = _user

    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val usersReference: DatabaseReference = database.getReference("Users")
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    fun signUp(
        email: String,
        password: String,
        name: String,
        surname: String,
        age: Int
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener { authResult ->
                val firebaseUser: FirebaseUser? = authResult.user
                if (firebaseUser == null) {
                    return@addOnSuccessListener
                }
                val user: User = User(
                    firebaseUser.uid,
                    name,
                    surname,
                    age,
                    false
                )
                usersReference.child(user.id).setValue(user)
            }
            .addOnFailureListener { exception -> _error.value = exception.toString() }
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