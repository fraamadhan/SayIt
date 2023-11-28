package com.example.sayit.data.api


import com.example.sayit.model.WordResponseItem
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("/words")
    suspend fun getWords(
        @Query("rel_jja") realJja: String = "Teacher",
        @Query("topics") topics: String = "Education",
    ): List<WordResponseItem>
}