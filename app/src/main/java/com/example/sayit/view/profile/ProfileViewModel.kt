package com.example.sayit.view.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sayit.repository.UserRepository
import kotlinx.coroutines.launch

class ProfileViewModel(private val userRepository: UserRepository): ViewModel() {
    fun logout() {
        viewModelScope.launch {
            userRepository.logout()
        }
    }
}