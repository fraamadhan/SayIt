package com.example.sayit.data.api

import com.example.sayit.data.request.LoginRequest
import com.example.sayit.data.request.RegistrationRequest
import com.example.sayit.model.GeneralLoginResponse
import com.example.sayit.model.GeneralRegisterResponse
import com.example.sayit.model.GeneralUserResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part


interface ApiService {
    @POST("register")
    suspend fun register(
        @Body request: RegistrationRequest
    ): GeneralRegisterResponse

    @POST("/login")
    suspend fun login(
        @Body request: LoginRequest
    ): GeneralLoginResponse

    @GET("/user")
    suspend fun getUser(
        @Header("Authorization") token: String
    ): GeneralUserResponse

    @Multipart
    @PUT("/user/update")
    suspend fun updateUser(
        @Header("Authorization") token: String,
        @Part("username") username: RequestBody,
        @Part imageFile: MultipartBody.Part? = null,
    ): GeneralUserResponse
}