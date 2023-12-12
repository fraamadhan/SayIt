package com.example.sayit.repository

import android.util.Log
import com.example.sayit.data.api.ApiService
import com.example.sayit.data.request.LoginRequest
import com.example.sayit.data.request.RegistrationRequest
import com.example.sayit.model.GeneralLoginResponse
import com.example.sayit.model.GeneralRegisterResponse
import com.example.sayit.model.GeneralUserResponse
import com.example.sayit.model.UserModel
import com.example.sayit.preference.LoginPreferences
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import java.io.File

class UserRepository(
    private val apiService: ApiService,
    private val dataStore: LoginPreferences,
) {


    fun addNewUser(username: String, email: String, password: String): Flow<Result<GeneralRegisterResponse>> = flow {
        emit(Result.Loading)
        try {
            val data = RegistrationRequest(username, email, password)
            val response = apiService.register(data)
            emit(Result.Success(response))
        } catch(e: HttpException) {
            emit(Result.Error(e.message.toString()))
        }
    }

    fun userLogin(email: String, password: String): Flow<Result<GeneralLoginResponse>> = flow {
        emit(Result.Loading)
        try {
            val data = LoginRequest(email, password)
            val response = apiService.login(data)
            emit(Result.Success(response))
        } catch (e: HttpException) {
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getUser(token: String): Flow<Result<GeneralUserResponse>> = flow {
        emit(Result.Loading)
        try {
            val response = apiService.getUser("Bearer $token")
            Log.d("INI RESPONSE", response.message.toString())
            emit(Result.Success(response))
        } catch (e: HttpException) {
            emit(Result.Error(e.message.toString()))
        }
    }

    fun updateUser(token: String, username: String, imageFile: File?): Flow<Result<GeneralUserResponse>> = flow {
        emit(Result.Loading)

        val requestBody = username.toRequestBody("text/plain".toMediaType())
        val requestImageFile = imageFile?.asRequestBody("image/jpeg".toMediaTypeOrNull())

        val multipartBody = MultipartBody.Part.createFormData(
            "Photo",
            imageFile?.name.orEmpty(),
            requestImageFile ?: "".toRequestBody("image/jpeg".toMediaTypeOrNull())
        )

        try {
            val response = apiService.updateUser("Bearer $token", requestBody, multipartBody)
            emit(Result.Success(response))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, GeneralUserResponse::class.java)
            emit(Result.Error(errorResponse.message.toString()))
        }
    }

    suspend fun saveSession(userModel: UserModel) {
        dataStore.saveSession(userModel)
    }

    fun getSession() : Flow<UserModel> {
        return dataStore.getSession()
    }

    fun getUserToken() = dataStore.getUserToken()

    suspend fun logout() {
        dataStore.logout()
    }

    companion object {

        @Volatile
        private var instance: UserRepository? = null

        fun getInstance(
            apiService: ApiService,
            loginPreferences: LoginPreferences
        ) : UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(apiService, loginPreferences)
            }.also { instance = it }
    }
}