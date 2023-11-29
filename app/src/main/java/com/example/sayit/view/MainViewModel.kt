package com.example.sayit.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.sayit.model.WordItem
import com.example.sayit.repository.WordRepository
import kotlinx.coroutines.launch

class MainViewModel(private val wordRepository: WordRepository): ViewModel() {
    fun getWordsFromApi() = wordRepository.getWordsFromApi().asLiveData()


    fun deleteALl() {
        viewModelScope.launch {
            wordRepository.deleteAll()
        }
    }

    fun getAllWords() {
        viewModelScope.launch {
            wordRepository.getWordFromLocal()
        }
    }

    // Use this variable to observe data based on the search query
    private val _searchedWords = MutableLiveData<List<WordItem>>()
    val searchedWords: LiveData<List<WordItem>> get() = _searchedWords

    // Function to perform search
    fun searchWords(query: String) {
        _searchedWords.value = wordRepository.searchWords(query).value
    }
}