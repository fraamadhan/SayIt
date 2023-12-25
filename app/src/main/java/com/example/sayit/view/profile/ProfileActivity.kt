package com.example.sayit.view.profile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.sayit.R
import com.example.sayit.databinding.ActivityProfileBinding
import com.example.sayit.repository.Result
import com.example.sayit.view.ViewModelFactory
import com.example.sayit.view.login.LoginActivity
import com.example.sayit.view.profile.editprofile.EditProfileActivity
import com.example.sayit.view.profile.editprofile.EditProfileActivity.Companion.URL_IMAGE
import com.example.sayit.view.profile.editprofile.EditProfileActivity.Companion.USERNAME

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding : ActivityProfileBinding
    private val viewModel by viewModels<ProfileViewModel>{
        ViewModelFactory.getInstance(this)
    }
    private var username : String? = null
    private var urlImage : String? = null
    private var email : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.btnEditProfile.setOnClickListener {
            val intent = Intent(this@ProfileActivity, EditProfileActivity::class.java)
            intent.putExtra(USERNAME, viewModel.username.value)
            intent.putExtra(URL_IMAGE, viewModel.urlImage.value)
            startActivity(intent)
        }

        binding.btnLogout.setOnClickListener {
            logout()
        }

        getUser()
    }

    private fun getUser() {
        showLoading()
        viewModel.getUserToken().observe(this@ProfileActivity) {token ->
            viewModel.getUser(token.toString()).observe(this@ProfileActivity) {result ->
                when(result) {
                    is Result.Error -> {
                        hideLoading()
                        showToast(result.error)
                    }
                    is Result.Loading -> {
                        showLoading()
                    }
                    is Result.Success -> {
                        hideLoading()
                        Log.d("INI DETAIL USER", result.data.user.toString())
                        Log.d("TOKEN DETAIL", token.toString())
                        username =  result.data.user?.username
                        email = result.data.user?.email
                        urlImage = result.data.user?.profilePicture
                        binding.profileImage.visibility = View.VISIBLE

                        viewModel.setUsername(username)
                        viewModel.setUrlImage(urlImage)

                        if (urlImage != "") {
                            Glide.with(this)
                                .load(urlImage)
                                .into(binding.profileImage)
                        }
                        else {
                            Glide.with(this)
                                .load(R.drawable.bneparamore)
                                .into(binding.profileImage)
                        }
                        binding.email.text = email
                        binding.username.text = username

                        Log.d("INI RESULT USER", result.data.user.toString())
                    }
                }
            }
        }
    }
    private fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    private fun hideLoading() {
        binding.progressBar.visibility = View.GONE
    }

    private fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
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