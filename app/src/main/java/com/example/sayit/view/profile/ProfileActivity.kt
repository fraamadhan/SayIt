package com.example.sayit.view.profile

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.sayit.R
import com.example.sayit.databinding.ActivityProfileBinding
import com.example.sayit.view.ViewModelFactory
import com.example.sayit.view.login.LoginActivity
import com.example.sayit.view.profile.editprofile.EditProfileActivity

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding : ActivityProfileBinding
    private val viewModel by viewModels<ProfileViewModel>{
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.btnEditProfile.setOnClickListener {
            startActivity(Intent(this@ProfileActivity, EditProfileActivity::class.java))
        }

        binding.btnLogout.setOnClickListener {
            logout()
        }
    }

    private fun logout() {
        AlertDialog.Builder(this)
            .setIcon(android.R.drawable.ic_dialog_alert)
            .setTitle(getString(R.string.logout_alert))
            .setMessage(getString(R.string.logout_message))
            .setPositiveButton("Yes"){ _, _ ->
                viewModel.logout()
                startActivity(Intent(this@ProfileActivity, LoginActivity::class.java))
                finish()
            }
            .setNegativeButton("No", null)
            .show()
    }


}