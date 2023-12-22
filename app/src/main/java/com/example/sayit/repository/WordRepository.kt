package com.example.sayit.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.sayit.data.api.ApiService
import com.example.sayit.database.WordDao
import com.example.sayit.database.WordDatabase
import com.example.sayit.model.Word
import com.example.sayit.model.WordItem
import com.example.sayit.remote.WordRemoteMediator
import retrofit2.HttpException

class WordRepository(
    private val apiService: ApiService,
    private val dao: WordDao,
    private val wordDatabase: WordDatabase,
) {
//    fun getWordFromApi(token: String): Flow<Result<List<WordItem>>> = flow {
//        emit(Result.Loading)
//        Log.d("KOK GAMASUK", "HAIYA")
//        try {
//            val responseData = apiService.getWords(token)
//            val wordDomain = DataMapper.getWordsDataClassed(responseData)
//            Log.d("INI RESPONSE", responseData.data.toString())
//            dao.insertWord(wordDomain)
//            emit(Result.Success(wordDomain))
//        } catch (e: HttpException) {
//            emit(Result.Error(e.message().toString()))
//        }
//    }

    fun getWordsFromApi(token: String): LiveData<PagingData<WordItem>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            remoteMediator = WordRemoteMediator(token, wordDatabase, apiService),
            pagingSourceFactory = {
//                QuotePagingSource(apiService)
                wordDatabase.wordDao().getAllWords()
            }
        ).liveData

    }

    fun getDetailStory(token: String, wordId: Int) : LiveData<Result<Word>> = liveData {
        emit(Result.Loading)
        try {
            val successResponse = apiService.getWordDetail("Bearer $token", wordId)
            emit(Result.Success(successResponse.word))
        } catch(e: HttpException) {
            emit(Result.Error(e.toString()))
        }
    }
    suspend fun deleteAll() {
        dao.deleteAll()
    }

    fun searchWords(query: String): List<WordItem> {
        return dao.searchWords(query)
    }
    companion object {
        @Volatile
        private var instance: WordRepository? = null

        fun getInstance(
            apiService: ApiService,
            dao: WordDao,
            wordDb: WordDatabase
        ) : WordRepository =
            instance ?: synchronized(this) {
                instance ?: WordRepository(apiService, dao, wordDb)
            }.also { instance = it }
    }
}