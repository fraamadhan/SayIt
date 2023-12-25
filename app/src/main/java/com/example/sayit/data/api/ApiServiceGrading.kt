package com.example.sayit.data.api

import com.example.sayit.model.GradingResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiServiceGrading {
    @Multipart
    @POST("/transcribe")
    suspend fun grading(
        @Part audioFile: MultipartBody.Part,
        @Part("text") text: RequestBody,
    ): GradingResponse
}