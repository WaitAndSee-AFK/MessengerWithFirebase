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

private const val EXTRA_EMAIL = "email"

class ResetPasswordActivity : AppCompatActivity() {
    private lateinit var editTextEmail: EditText
    private lateinit var buttonResetPassword: Button
    private lateinit var viewModel: ResetPasswordViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)
        initViews()
        viewModel = ViewModelProvider(this@ResetPasswordActivity)
            .get(ResetPasswordViewModel::class.java)
        observeViewModel()
        installingEmailFromTheLoginScreen()

        buttonResetPassword.setOnClickListener {
            val email = editTextEmail.text.trim().toString()
            viewModel.resetPassword(email)
        }
    }

    private fun observeViewModel() {
        viewModel.error.observe(this, { errorMessage ->
            if (errorMessage != null) {
                Toast.makeText(
                    this@ResetPasswordActivity,
                    errorMessage,
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
        viewModel.isSuccess.observe(this, { success ->
            if (success) {
                Toast.makeText(
                    this@ResetPasswordActivity,
                    getString(R.string.reset_link_sent),
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun installingEmailFromTheLoginScreen() {
        val email = intent.getStringExtra(EXTRA_EMAIL)
        editTextEmail.setText(email)
    }

    fun newIntent(context: Context, email: String): Intent {
        val intent = Intent(context, ResetPasswordActivity::class.java)
        intent.putExtra(EXTRA_EMAIL, email)
        return intent
    }

    private fun initViews() {
        editTextEmail = findViewById(R.id.editTextEmail)
        buttonResetPassword = findViewById(R.id.buttonResetPassword)
    }
}