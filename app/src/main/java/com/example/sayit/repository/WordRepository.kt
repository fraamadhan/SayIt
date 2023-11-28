package com.example.sayit.repository

import com.example.sayit.data.api.ApiService
import com.example.sayit.model.WordItem
import com.example.sayit.utils.DataMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException

class WordRepository(
    private val apiService: ApiService,
) {
    fun getStoriesFromApi(): Flow<Result<List<WordItem>>> = flow {
        emit(Result.Loading)
        try {
            val responseData = apiService.getWords()
            val wordDomain = DataMapper.getWordsDataClassed(responseData)
            emit(Result.Success(wordDomain))
        } catch (e: HttpException) {
            emit(Result.Error(e.message().toString()))
        }
    }
    companion object {
        @Volatile
        private var instance: WordRepository? = null

        fun getInstance(
            apiService: ApiService,
        ) : WordRepository =
            instance ?: synchronized(this) {
                instance ?: WordRepository(apiService)
            }.also { instance = it }
    }
}