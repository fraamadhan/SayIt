package com.example.sayit.model

import com.google.gson.annotations.SerializedName

data class WordResponse(

	@field:SerializedName("WordResponse")
	val wordResponse: List<WordResponseItem>
)

data class WordResponseItem(

	@field:SerializedName("score")
	val score: Int? = null,

	@field:SerializedName("word")
	val word: String? = null
)
