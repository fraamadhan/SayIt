package com.example.sayit.view.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.sayit.model.GeneralLoginResponse
import com.example.sayit.model.UserModel
import com.example.sayit.repository.Result
import com.example.sayit.repository.UserRepository
import kotlinx.coroutines.launch

class LoginViewModel(private val userRepository: UserRepository): ViewModel() {
    private val loginResult: LiveData<Result<GeneralLoginResponse>> = MutableLiveData()

    // Fungsi untuk mendapatkan LiveData hasil login
    fun getUserLogin(): LiveData<Result<GeneralLoginResponse>> {
        return loginResult
    }

    fun userLogin(email: String, password: String) {
        viewModelScope.launch {
            val loginResultLiveData = userRepository.userLogin(email, password).asLiveData()

            loginResultLiveData.observeForever { result ->
                (loginResult as MutableLiveData).value = result
            }
        }
    }
    fun saveSession(user: UserModel) {
        viewModelScope.launch {
            userRepository.saveSession(user)
        }
    }

    fun getSession() : LiveData<UserModel> {
        return userRepository.getSession().asLiveData()
    }
}