package com.example.sayit.view.detailword

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.sayit.repository.UserRepository
import com.example.sayit.repository.WordRepository
import kotlinx.coroutines.Dispatchers

class DetailWordViewModel(private val wordRepository: WordRepository, private val userRepository: UserRepository): ViewModel() {
    fun getDetailWord(token: String, wordId: Int) = wordRepository.getDetailStory(token, wordId)
    fun getUserToken() = userRepository.getUserToken().asLiveData(Dispatchers.IO)
}