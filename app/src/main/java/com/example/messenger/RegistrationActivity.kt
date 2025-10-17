package com.example.messenger

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider

class RegistrationActivity : AppCompatActivity() {
    private lateinit var editTextEmail: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var editTextName: EditText
    private lateinit var editTextSurname: EditText
    private lateinit var editTextAge: EditText
    private lateinit var buttonSignUp: Button
    private lateinit var viewModel: RegistrationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)
        initViews()
        viewModel = ViewModelProvider(this).get(RegistrationViewModel::class.java)
        observeViewModel()
        buttonSignUp.setOnClickListener {
            val email = editTextEmail.text.toString().trim()
            val password = editTextPassword.text.toString().trim()
            val name = editTextName.text.toString().trim()
            val surname = editTextSurname.text.toString().trim()
            val age = editTextAge.text.toString().trim().toInt()
            viewModel.signUp(email, password, name, surname, age)
        }
    }

    private fun observeViewModel() {
        viewModel.user.observe(this, { firebaseUser ->
            if (firebaseUser != null) {
                val intent = RegistrationViewModel().newIntent(this@RegistrationActivity)
                startActivity(intent)
                finish()
            }
        })
        viewModel.error.observe(this, { errorMessage ->
            if (errorMessage != null) {
                Toast.makeText(
                    this@RegistrationActivity,
                    errorMessage,
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    fun newIntent(context: Context): Intent {
        return Intent(context, RegistrationActivity::class.java)
    }

    private fun initViews() {
        editTextEmail = findViewById(R.id.editTextEmail)
        editTextPassword = findViewById(R.id.editTextPassword)
        editTextName = findViewById(R.id.editTextName)
        editTextSurname = findViewById(R.id.editTextSurname)
        editTextAge = findViewById(R.id.editTextAge)
        buttonSignUp = findViewById(R.id.buttonSignUp)
    }
}