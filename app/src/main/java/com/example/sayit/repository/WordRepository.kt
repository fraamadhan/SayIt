package com.example.sayit.repository

import androidx.lifecycle.LiveData
import com.example.sayit.data.api.ApiService
import com.example.sayit.database.WordDao
import com.example.sayit.model.WordItem

class WordRepository(
    private val apiService: ApiService,
    private val dao: WordDao
) {
//    fun getWordsFromApi(): Flow<Result<List<WordItem>>> = flow {
//        emit(Result.Loading)
//        try {
//            val responseData = apiService.getWords()
//            val wordDomain = DataMapper.getWordsDataClassed(responseData)
//            dao.insertWord(wordDomain)
//            emit(Result.Success(wordDomain))
//        } catch (e: HttpException) {
//            emit(Result.Error(e.message().toString()))
//        }
//    }

    suspend fun getWordFromLocal(): LiveData<List<WordItem>> {
        return dao.getAllWords()
    }
    suspend fun deleteAll() {
        dao.deleteAll()
    }

    fun searchWords(query: String): LiveData<List<WordItem>> {
        return dao.searchWords(query)
    }
    companion object {
        @Volatile
        private var instance: WordRepository? = null

        fun getInstance(
            apiService: ApiService,
            dao: WordDao
        ) : WordRepository =
            instance ?: synchronized(this) {
                instance ?: WordRepository(apiService, dao)
            }.also { instance = it }
    }
}