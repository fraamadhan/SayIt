package com.example.sayit.view.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.sayit.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProfileViewModel(private val userRepository: UserRepository): ViewModel() {
    private val _username = MutableLiveData<String?>()
    val username : LiveData<String?> = _username
    private val _urlImage = MutableLiveData<String?>()
    val urlImage : LiveData<String?> = _urlImage

    fun setUsername(username: String?) {
        _username.value = username
    }

    fun setUrlImage(url: String?) {
        _urlImage.value = url
    }
    fun getUser(token: String) = userRepository.getUser(token).asLiveData()

    fun getUserToken() = userRepository.getUserToken().asLiveData(Dispatchers.IO)
    fun logout() {
        viewModelScope.launch {
            userRepository.logout()
        }
    }
}