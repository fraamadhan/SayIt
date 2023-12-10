package com.example.sayit.view

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.sayit.data.di.Injection
import com.example.sayit.repository.UserRepository
import com.example.sayit.repository.WordRepository
import com.example.sayit.view.detailword.DetailWordViewModel
import com.example.sayit.view.login.LoginViewModel
import com.example.sayit.view.profile.ProfileViewModel
import com.example.sayit.view.profile.editprofile.EditProfileViewModel
import com.example.sayit.view.register.RegisterViewModel

class ViewModelFactory private constructor(
    private val wordRepository: WordRepository,
    private val userRepository: UserRepository
): ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(wordRepository) as T
        }
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(userRepository) as T
        }
        if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
            return RegisterViewModel(userRepository) as T
        }
        if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            return ProfileViewModel(userRepository) as T
        }
        if (modelClass.isAssignableFrom(EditProfileViewModel::class.java)) {
            return EditProfileViewModel() as T
        }
        if (modelClass.isAssignableFrom(DetailWordViewModel::class.java)) {
            return DetailWordViewModel() as T
        }
        throw IllegalArgumentException("Unknown Viewmodel CLass: " + modelClass.name)
    }

    companion object{
        @Volatile
        private var instance: ViewModelFactory? = null

        fun getInstance(context: Context): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(
                    Injection.provideRepository(context),
                    Injection.provideUserRepository(context),
                )
            }.also { instance = it }
    }
}