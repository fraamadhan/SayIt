package com.example.sayit.view.register

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import com.example.sayit.databinding.ActivityRegisterBinding
import com.example.sayit.view.login.LoginActivity

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnRegister.isEnabled = false
        setupAction()

        binding.btnGoLogin.setOnClickListener {
            startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
        }
    }

    private fun setupAction() {
        binding?.apply {
            emailEditText.addTextChangedListener(watcher)
            passwordEditText.addTextChangedListener(watcher)
            nameEditText.addTextChangedListener(watcher)
        }
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