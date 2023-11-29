package com.example.sayit.data.api


import com.example.sayit.model.WordResponseItem
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("/words")
    suspend fun getWords(
        @Query("rel_jja") realJja: String = "Ocean",
        @Query("topics") topics: String = "Temperature",
    ): List<WordResponseItem>
}