package com.example.messenger

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private lateinit var editTextEmail: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var buttonSignIn: Button
    private lateinit var textViewForgotPassword: TextView
    private lateinit var textViewRegistration: TextView

    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        initViews()
        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        observeViewModel()
        setupClickListeners()
    }

    private fun setupClickListeners() {
        buttonSignIn.setOnClickListener {
            val email = editTextEmail.text.trim().toString()
            val password = editTextPassword.text.trim().toString()
            viewModel.login(email, password)
        }

        textViewForgotPassword.setOnClickListener {
            val intent = ResetPasswordActivity().newIntent(
                this,
                editTextEmail.text.trim().toString()
            )
            startActivity(intent)
        }

        textViewRegistration.setOnClickListener {
            val intent = RegistrationActivity().newIntent(this)
            startActivity(intent)
        }
    }

    private fun observeViewModel() {
        viewModel.user.observe(this, { user ->
            if (user != null) {
                val intent = UsersActivity().newIntent(this@MainActivity)
                startActivity(intent)
                finish()
            }
        })
        viewModel.error.observe(this, { err ->
            Toast.makeText(this@MainActivity, err.toString(), Toast.LENGTH_SHORT).show()
        })
    }

    private fun initViews() {
        editTextEmail = findViewById(R.id.editTextEmail)
        editTextPassword = findViewById(R.id.editTextPassword)
        buttonSignIn = findViewById(R.id.buttonSignIn)
        textViewForgotPassword = findViewById(R.id.textViewForgotPassword)
        textViewRegistration = findViewById(R.id.textViewRegistration)
    }
}