package com.example.messenger

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import kotlin.random.Random

class UsersActivity : AppCompatActivity() {
    private lateinit var usersAdapter: UsersAdapter
    private lateinit var recyclerViewUsers: RecyclerView
    private lateinit var viewModel: UsersViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_users)
        initViews()
        viewModel = ViewModelProvider(this).get(UsersViewModel::class.java)
        observeViewModel()

        val users = mutableListOf<User>()
        for (i in 0 .. 10) {
            users.add(User(
                id = "id:$i",
                name = "Name$i",
                surname = "Surname$i",
                age = Random.nextInt(40),
                isOnline = Random.nextBoolean()
            ))
        }
        usersAdapter.updateUsers(users)
    }

    private fun observeViewModel() {
        viewModel.user.observe(this, { firebaseUser ->
            if (firebaseUser == null) {
                val intent = UsersViewModel().newIntent(this@UsersActivity)
                startActivity(intent)
                finish()
            }
        })
    }

    fun newIntent(context: Context): Intent {
        return Intent(context, UsersActivity::class.java)
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