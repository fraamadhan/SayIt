package com.example.sayit.data.api

import com.example.sayit.data.request.LoginRequest
import com.example.sayit.data.request.RegistrationRequest
import com.example.sayit.model.GeneralLoginResponse
import com.example.sayit.model.GeneralRegisterResponse
import retrofit2.http.Body
import retrofit2.http.POST


interface ApiService {
    @POST("register")
    suspend fun register(
        @Body request: RegistrationRequest
    ): GeneralRegisterResponse

    @POST("/login")
    suspend fun login(
        @Body request: LoginRequest
    ): GeneralLoginResponse
}