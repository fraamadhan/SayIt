package com.example.sayit.data.di

import android.content.Context
import com.example.sayit.data.api.ApiConfig
import com.example.sayit.database.WordDatabase
import com.example.sayit.repository.WordRepository

object Injection {
    fun provideRepository(context: Context): WordRepository {
        val apiService = ApiConfig.getApiService()
        val database = WordDatabase.getDatabase(context)
        val dao = database.wordDao()
        return WordRepository.getInstance(apiService, dao)
    }
}