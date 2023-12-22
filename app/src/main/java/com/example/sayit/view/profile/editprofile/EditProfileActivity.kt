package com.example.sayit.view.profile.editprofile

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.sayit.R
import com.example.sayit.databinding.ActivityEditProfileBinding
import com.example.sayit.repository.Result
import com.example.sayit.utils.reduceImageFile
import com.example.sayit.utils.uriToFile
import com.example.sayit.view.ViewModelFactory
import com.example.sayit.view.profile.ProfileActivity

class EditProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditProfileBinding
    private var currentImageUrl: Uri? = null
    private val viewModel by viewModels<EditProfileViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.addPicture.setOnClickListener { startGallery() }

        binding.btnUpdateProfile.setOnClickListener { updateUser() }

        observeUri()
        bindingProfile()
    }

    private fun bindingProfile() {
        val username = intent.getStringExtra(USERNAME)
        val urlImage = intent.getStringExtra(URL_IMAGE)

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

        binding.nameEditText.setText(username)
    }

    private fun updateUser() {
        val username = binding.nameEditText.text.toString().trim()
        val imageFile = currentImageUrl?.let {uri ->
            Log.d("INI URI", uri.toString())
            uriToFile(uri, this).reduceImageFile()
        }
        Log.d("INI IMAGE FILE", imageFile.toString())

        showLoading()

        viewModel.getUserToken().observe(this@EditProfileActivity) { token ->
            Log.d("TOKEN EDIT", token.toString())
            viewModel.updateUser(token.toString(), username, imageFile).observe(this@EditProfileActivity) { result ->
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
                        showToast(result.data.message.toString())
                        Log.d("INI RESPONSE EDIT", result.data.user.toString())
                        val intent = Intent(this@EditProfileActivity, ProfileActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }
            }
        }
    }
    private fun observeUri() {
        viewModel.imageUri.observe(this) { uri ->
            currentImageUrl = uri
            showImage()
        }
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) {uri: Uri? ->
        if (uri != null) {
            currentImageUrl = uri
            viewModel.setImageUri(uri)
            showImage()
        } else {
            Log.d("Photo Picker", "No media selected")
            showToast("No media selected")

        }
    }

    private fun showImage() {
        currentImageUrl?.let {uri ->
            Log.d("Image Uri", "show image: $uri")
            binding.profileImage.setImageURI(uri)
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

    companion object {
        const val USERNAME = "username"
        const val URL_IMAGE = "url_image"
    }
}