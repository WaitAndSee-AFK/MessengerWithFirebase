package com.example.messenger

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.io.Closeable

class ChatViewModel(
    private val currentUserID: String,
    private val otherUserID: String
) : ViewModel() {

    private val firebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val referenceUsers: DatabaseReference = firebaseDatabase.getReference("Users")
    private val referenceMessages: DatabaseReference = firebaseDatabase.getReference("Messages")

    private val _messages = MutableLiveData<List<Message>>()
    val messages: LiveData<List<Message>>
        get() = _messages

    private val _otherUser = MutableLiveData<User>()
    val otherUser: LiveData<User>
        get() = _otherUser

    private val _messageSent = MutableLiveData<Boolean>()
    val messageSent: LiveData<Boolean>
        get() = _messageSent

    private val _error = MutableLiveData<String>()
    val error: LiveData<String>
        get() = _error

    fun setUserOnline(isOnline: Boolean) {
        referenceUsers
            .child(currentUserID)
            .child("online")
            .setValue(isOnline)
    }

    fun sendMessage(message: Message) {
        referenceMessages
            .child(message.senderID)
            .child(message.receiverID)
            .push().setValue(message)
            .addOnSuccessListener {
                referenceMessages
                    .child(message.receiverID)
                    .child(message.senderID)
                    .push().setValue(message)
                    .addOnSuccessListener {
                        _messageSent.value = true
                    }
                    .addOnFailureListener { err ->
                        _error.value = err.toString()
                    }
            }
            .addOnFailureListener { err ->
                _error.value = err.toString()
            }
    }

    private fun loadOtherUser() {
        referenceUsers.child(otherUserID)
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    _error.value = "Failed to load user ${error}r"
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    val user: User? = snapshot.getValue(User::class.java)
                    if (user != null) {
                        _otherUser.value = user
                    } else {
                        _error.value = "User not found"
                    }
                }
            })
    }

    private fun loadMessages() {
        referenceMessages.child(currentUserID).child(otherUserID)
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    _error.value = "Failed to load message $error"
                }


                override fun onDataChange(snapshot: DataSnapshot) {
                    val messageList = mutableListOf<Message>()
                    for (dataSnapshot in snapshot.children) {
                        val message: Message? = dataSnapshot.getValue(Message::class.java)
                        if (message != null) {
                            messageList.add(message)
                        } else {
                            _error.value = "Message is null"
                        }
                    }
                    _messages.value = messageList
                }

            })
    }

    init {
        loadOtherUser()
        loadMessages()
    }
}