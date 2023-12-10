package com.example.sayit.view.register

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.sayit.databinding.ActivityRegisterBinding
import com.example.sayit.repository.Result
import com.example.sayit.view.ViewModelFactory
import com.example.sayit.view.login.LoginActivity

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private var username: String = ""
    private var email: String = ""
    private var password: String = ""

    private val viewModel by viewModels<RegisterViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnRegister.isEnabled = false
        setupAction()

        binding.btnGoLogin.setOnClickListener {
            startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
        }

        binding.btnRegister.setOnClickListener {
            showLoading()
            binding.apply {
                username = nameEditText.text.toString().trim()
                email = emailEditText.text.toString().trim()
                password = passwordEditText.text.toString().trim()
            }

            viewModel.addNewUser(username, email, password).observe(this) { result ->
                when(result) {
                    is Result.Loading -> {
                        showLoading()
                    }
                    is Result.Error -> {
                        showToast(result.error)
                    }
                    is Result.Success -> {
                        onSuccess()
                        Log.d("INI RESPONSE", result.data.data.toString())
                    }
                }
            }
        }
    }

    private fun setupAction() {
        binding?.apply {
            emailEditText.addTextChangedListener(watcher)
            passwordEditText.addTextChangedListener(watcher)
            nameEditText.addTextChangedListener(watcher)
        }
    }

    private fun onSuccess() {
        binding.progressBar.visibility = View.GONE
        val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun showLoading(){
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun setMyButtonEnabled() {
        val email = binding?.emailEditText
        val password = binding?.passwordEditText
        val name = binding?.nameEditText?.text

        if (email?.text!!.isNotEmpty() && password?.text!!.isNotEmpty() && name!!.isNotEmpty()) {
            binding?.btnRegister?.isEnabled = email?.error == null && password?.error == null && name!!.isNotEmpty()
        }
        else {
            binding?.btnRegister?.isEnabled = false
        }
    }

    private val watcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            //
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            setMyButtonEnabled()
        }

        override fun afterTextChanged(s: Editable?) {
            //
        }
    }
}