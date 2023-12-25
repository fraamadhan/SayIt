package com.example.sayit.data.di

import android.content.Context
import com.example.sayit.data.api.ApiConfig
import com.example.sayit.database.WordDatabase
import com.example.sayit.preference.LoginPreferences
import com.example.sayit.preference.dataStore
import com.example.sayit.repository.UserRepository
import com.example.sayit.repository.WordRepository

object Injection {
    fun provideRepository(context: Context): WordRepository {
        val apiService = ApiConfig.getApiService()
        val database = WordDatabase.getDatabase(context)
        val dao = database.wordDao()
        val apiServiceGrading = ApiConfig.getApiServiceGrading()
        return WordRepository.getInstance(apiService, dao, database, apiServiceGrading)
    }

    fun provideUserRepository(context: Context): UserRepository {
        val pref = LoginPreferences.getInstance(context.dataStore)
        val apiService = ApiConfig.getApiService()
        return UserRepository.getInstance(apiService, pref)
    }
}