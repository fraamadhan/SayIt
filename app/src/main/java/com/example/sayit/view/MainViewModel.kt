package com.example.sayit.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.sayit.model.WordItem
import com.example.sayit.repository.UserRepository
import com.example.sayit.repository.WordRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(private val wordRepository: WordRepository, private val userRepository: UserRepository): ViewModel() {
    fun getWordsFromApi(token: String): LiveData<PagingData<WordItem>> =
        wordRepository.getWordsFromApi(token)
        .cachedIn(viewModelScope)
    fun deleteALl() {
        viewModelScope.launch {
            wordRepository.deleteAll()
        }
    }


    fun getUserToken() = userRepository.getUserToken().asLiveData(Dispatchers.IO)

    private val _searchResults = MutableLiveData<List<WordItem>>()
    val searchResults: LiveData<List<WordItem>> get() = _searchResults

    fun search(query: String) {
        viewModelScope.launch (Dispatchers.IO){
            val results = wordRepository.searchWords(query)
            _searchResults.postValue(results)
        }
    }
}