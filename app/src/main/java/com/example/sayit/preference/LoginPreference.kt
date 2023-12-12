package com.example.sayit.preference

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.sayit.model.UserModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "session")

class LoginPreferences private constructor(private val dataStore: DataStore<Preferences>){
    suspend fun saveSession(userModel: UserModel) {
        dataStore.edit {preferences ->
            preferences[USERID_KEY] = userModel.userId
            preferences[EMAIL] = userModel.email
            preferences[USERNAME_KEY] = userModel.username
            preferences[TOKEN_KEY] = userModel.token
            preferences[IS_LOGIN_KEY] = true
        }
    }

    fun getSession(): Flow<UserModel> {
        return dataStore.data.map { preferences->
            val userModel = UserModel(
                preferences[USERID_KEY] ?: "",
                preferences[USERNAME_KEY] ?: "",
                preferences[EMAIL] ?: "",
                preferences[TOKEN_KEY] ?: "",
                preferences[IS_LOGIN_KEY] ?: false,
            )
            userModel
        }
    }

    fun getUserId(): Flow<String?> {
        return dataStore.data.map { preferences ->
            preferences[USERID_KEY]
        }
    }

    fun getUserToken() : Flow<String?> {
        return dataStore.data.map { preferences ->
            preferences[TOKEN_KEY]
        }
    }

    suspend fun logout() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    companion object{
        @Volatile
        private var INSTANCE: LoginPreferences? = null

        private val USERID_KEY = stringPreferencesKey("userId")
        private val EMAIL = stringPreferencesKey("email")
        private val USERNAME_KEY = stringPreferencesKey("username")
        private val TOKEN_KEY = stringPreferencesKey("token")
        private val IS_LOGIN_KEY = booleanPreferencesKey("isLogin")

        fun getInstance(dataStore: DataStore<Preferences>) : LoginPreferences {
            return INSTANCE ?: synchronized(this) {
                val instance = LoginPreferences(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}