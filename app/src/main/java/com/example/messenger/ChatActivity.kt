package com.example.messenger

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView

private const val EXTRA_CURRENT_USER_ID = "current_id"
private const val EXTRA_OTHER_USER_ID = "other_id"

class ChatActivity : AppCompatActivity() {
    private lateinit var textViewTitle: TextView
    private lateinit var viewOnlineStatus: View
    private lateinit var recyclerViewMessages: RecyclerView
    private lateinit var editTextMessage: EditText
    private lateinit var imageViewSendMessage: ImageView

    private lateinit var messagesAdapter: MessagesAdapter

    private lateinit var currentUserID: String
    private lateinit var otherUserID: String

    private lateinit var viewModel: ChatViewModel
    private lateinit var viewModelFactory: ChatViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_chat)
        initViews()
        currentUserID = intent.getStringExtra(EXTRA_CURRENT_USER_ID).toString()
        otherUserID = intent.getStringExtra(EXTRA_OTHER_USER_ID).toString()
        viewModelFactory = ChatViewModelFactory(currentUserID, otherUserID)
        viewModel = ViewModelProvider(this, viewModelFactory).get(ChatViewModel::class.java)
        messagesAdapter = MessagesAdapter(currentUserID)
        recyclerViewMessages.adapter = messagesAdapter
        observeViewModel()
        imageViewSendMessage.setOnClickListener {
            val message = Message(
                editTextMessage.text.toString().trim(),
                currentUserID,
                otherUserID
            )
            viewModel.sendMessage(message)
        }
    }

    private fun observeViewModel() {
        viewModel.messages.observe(this, {
            messagesAdapter.updateMessages(it)
        })
        viewModel.error.observe(this, object : Observer<String>{
            override fun onChanged(value: String) {
                if (value != null) {
                    Toast.makeText(
                        this@ChatActivity,
                        value,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
        viewModel.messageSent.observe(this, {
            if (it) {
                editTextMessage.setText("")
            }
        })
        viewModel.otherUser.observe(this, object : Observer<User>{
            override fun onChanged(user: User) {
                val userInfo: String = String.format("%s %s", user.name, user.surname)
                textViewTitle.text = userInfo
                val backgroundResID = if (user.online) R.drawable.circle_green else R.drawable.circle_red
                val background: Drawable = ContextCompat.getDrawable(
                    this@ChatActivity,
                    backgroundResID
                )
                viewOnlineStatus.background = background
            }
        })
    }

    override fun onResume() {
        super.onResume()
        viewModel.setUserOnline(true)
    }

    override fun onPause() {
        super.onPause()
        viewModel.setUserOnline(false)
    }

    private fun initViews() {
        textViewTitle = findViewById(R.id.textViewTitle)
        viewOnlineStatus = findViewById(R.id.viewOnlineStatus)
        recyclerViewMessages = findViewById(R.id.recyclerViewMessages)
        editTextMessage = findViewById(R.id.editTextMessage)
        imageViewSendMessage = findViewById(R.id.imageViewSendMessage)
    }

    fun newIntent(context: Context, currentUserID: String, otherUserID: String): Intent {
        val intent = Intent(context, ChatActivity::class.java)
        intent.putExtra(EXTRA_CURRENT_USER_ID, currentUserID)
        intent.putExtra(EXTRA_OTHER_USER_ID, otherUserID)
        return intent
    }
}