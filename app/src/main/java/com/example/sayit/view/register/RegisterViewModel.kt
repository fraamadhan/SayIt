package com.example.sayit.view.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.sayit.repository.UserRepository

class RegisterViewModel(private val userRepository: UserRepository): ViewModel() {
    fun addNewUser(username: String, email: String, password: String) = userRepository.addNewUser(username, email, password).asLiveData()
}