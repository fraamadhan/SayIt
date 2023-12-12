package com.example.sayit.view.profile.editprofile

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.sayit.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import java.io.File

class EditProfileViewModel(private val userRepository: UserRepository): ViewModel() {
    private val _imageUri = MutableLiveData<Uri?>()
    val imageUri: LiveData<Uri?> = _imageUri

    fun setImageUri(uri: Uri?) {
        _imageUri.value = uri
    }

    fun updateUser(token: String, username: String, imageFile: File?) = userRepository.updateUser(token, username, imageFile).asLiveData()

    fun getUserToken() = userRepository.getUserToken().asLiveData(Dispatchers.IO)
}