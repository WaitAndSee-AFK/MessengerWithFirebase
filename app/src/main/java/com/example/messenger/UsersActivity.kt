package com.example.messenger

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlin.random.Random

private const val TAG = "UsersActivity"
private const val EXTRA_CURRENT_USER_ID = "current_id"

class UsersActivity : AppCompatActivity() {
    private lateinit var usersAdapter: UsersAdapter
    private lateinit var recyclerViewUsers: RecyclerView
    private lateinit var viewModel: UsersViewModel

    private lateinit var currentUserID: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_users)
        setSupportActionBar(findViewById(R.id.toolbar))
        initViews()
        currentUserID = intent.getStringExtra(EXTRA_CURRENT_USER_ID).toString()
        viewModel = ViewModelProvider(this).get(UsersViewModel::class.java)
        observeViewModel()
        usersAdapter.onUserClickListener = object : UsersAdapter.OnUserClickListener {
            override fun onUserClick(user: User) {
                val intent: Intent = ChatActivity().newIntent(
                    this@UsersActivity,
                    currentUserID,
                    user.id
                )
                startActivity(intent)
            }
        }
    }

    private fun observeViewModel() {
        viewModel.user.observe(this, { firebaseUser ->
            if (firebaseUser == null) {
                val intent = UsersViewModel().newIntent(this@UsersActivity)
                startActivity(intent)
                finish()
            }
        })
        viewModel.users.observe(this, { users ->
            usersAdapter.updateUsers(users)
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

    fun newIntent(context: Context, currentUserID: String): Intent {
        val intent: Intent = Intent(context, UsersActivity::class.java)
        intent.putExtra(EXTRA_CURRENT_USER_ID, currentUserID)
        return intent
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.item_logout) {
            viewModel.logout()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    private fun initViews() {
        recyclerViewUsers = findViewById(R.id.recyclerViewUsers)
        usersAdapter = UsersAdapter()
        recyclerViewUsers.adapter = usersAdapter
    }
}