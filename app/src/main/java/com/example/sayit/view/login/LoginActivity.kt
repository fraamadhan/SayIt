package com.example.sayit.view.login

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.sayit.databinding.ActivityLoginBinding
import com.example.sayit.model.UserModel
import com.example.sayit.repository.Result
import com.example.sayit.view.MainActivity
import com.example.sayit.view.ViewModelFactory
import com.example.sayit.view.register.RegisterActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val viewModel by viewModels<LoginViewModel>{
        ViewModelFactory.getInstance(this)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.isEnabled = false
        setupAction()

        binding.btnGoRegister.setOnClickListener {
            startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
        }

        viewModel.getUserLogin().observe(this@LoginActivity) {result ->
            when(result) {
                is Result.Loading -> {
                    showLoading()
                }
                is Result.Error -> {
                    showToast(result.error)
                }
                is Result.Success -> {
                    onSuccess()
                    Log.d("INI RESPONSE", result.data.loginResult.toString())
                    val userId = result.data.loginResult?.id
                    val email = result.data.loginResult?.email
                    val username = result.data.loginResult?.username
                    val token = result.data.loginResult?.token
                    val user = UserModel(userId!!, email!!, username!!, token!!, true)
                    viewModel.saveSession(user)
                }
            }
        }

        binding.btnLogin.setOnClickListener {
            showLoading()
            val email = binding?.emailEditText?.text.toString().trim()
            val password = binding?.passwordEditText?.text.toString().trim()
            viewModel.userLogin(email, password)
        }
    }

    private fun onSuccess() {
        binding?.progressBar?.visibility = View.GONE
        val intent = Intent(this@LoginActivity, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun showLoading(){
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun setupAction() {
        binding?.apply {
            emailEditText.addTextChangedListener(watcher)
            passwordEditText.addTextChangedListener(watcher)
        }
    }

    private fun setMyButtonEnabled() {
        val email = binding?.emailEditText
        val password = binding?.passwordEditText

        if (email?.text!!.isNotEmpty() && password?.text!!.isNotEmpty()) {
            binding?.btnLogin?.isEnabled = email?.error == null && password?.error == null
        }
        else {
            binding?.btnLogin?.isEnabled = false
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