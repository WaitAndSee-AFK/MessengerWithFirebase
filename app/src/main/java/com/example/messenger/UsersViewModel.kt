package com.example.messenger

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class UsersViewModel() : ViewModel() {
    private val database: FirebaseDatabase
    private val usersReference: DatabaseReference
    private val auth: FirebaseAuth
    private val _users: MutableLiveData<List<User>> = MutableLiveData()
    val users: LiveData<List<User>>
        get() = _users

    private val _user: MutableLiveData<FirebaseUser> = MutableLiveData()
    val user: LiveData<FirebaseUser>
        get() = _user

    fun setUserOnline(isOnline: Boolean) {
        val currentUserID = auth.currentUser
        if (currentUserID == null) {
            return
        }
        usersReference
            .child(currentUserID.uid)
            .child("online")
            .setValue(isOnline)
    }

    private val valueEventListener = object : ValueEventListener {
        override fun onCancelled(error: DatabaseError) {
            Log.d("UsersViewModel", error.toString())
        }

        override fun onDataChange(snapshot: DataSnapshot) {
            val currentUser: FirebaseUser? = auth.currentUser
            if (currentUser == null) return
            val usersFromDB = mutableListOf<User>()
            snapshot.children.forEach {
                val user = it.getValue(User::class.java)
                if (user == null) return
                if (!user.id.equals(currentUser.uid)) {
                    usersFromDB.add(user)
                }
            }
            _users.value = usersFromDB
        }
    }

    fun logout() {
        setUserOnline(false)
        auth.signOut()
    }

    fun newIntent(context: Context): Intent {
        return Intent(context, MainActivity::class.java)
    }

    init {
        auth = FirebaseAuth.getInstance()
        auth.addAuthStateListener { firebaseAuth -> _user.value = firebaseAuth.currentUser }
        database = FirebaseDatabase.getInstance()
        usersReference = database.getReference("Users")
        usersReference.addValueEventListener(valueEventListener)
    }
}