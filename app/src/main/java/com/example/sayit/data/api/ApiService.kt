package com.example.sayit.data.api

import com.example.sayit.data.request.LoginRequest
import com.example.sayit.data.request.RegistrationRequest
import com.example.sayit.model.GeneralLoginResponse
import com.example.sayit.model.GeneralRegisterResponse
import com.example.sayit.model.GeneralUserResponse
import com.example.sayit.model.WordDetailResponse
import com.example.sayit.model.WordResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query


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
        @Part imageFile: MultipartBody.Part? = null,
        @Part("username") username: RequestBody,
    ): GeneralUserResponse

    @GET("/words")
    suspend fun getWords(
        @Header("Authorization") token: String,
        @Query("page") page: Int = 1,
        @Query("size") size: Int = 16
    ): WordResponse

    @GET("/words/{id}")
    suspend fun getWordDetail(
        @Header("Authorization") token: String,
        @Path("id") wordId: Int,
    ): WordDetailResponse

//    @Multipart
//    @POST("/words/{id}")
//    suspend fun uploadAudio(
//        @Header("Authorization") token: String,
//        @Part audioFile: MultipartBody.Part? = null,
//        @Path("id") wordId: Int
//    )
}