package com.example.sayit.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.sayit.repository.WordRepository

class MainViewModel(private val wordRepository: WordRepository): ViewModel() {
    fun getWordsFromApi() = wordRepository.getStoriesFromApi().asLiveData()
}