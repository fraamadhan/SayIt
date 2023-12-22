package com.example.sayit.model

import com.google.gson.annotations.SerializedName

data class WordResponse(

	@field:SerializedName("data")
	val word: List<Words?>,

	@field:SerializedName("status")
	val status: String? = null
)

data class Words(

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("word")
	val word: String? = null
)

data class WordDetailResponse(

	@field:SerializedName("data")
	val word: Word,

	@field:SerializedName("status")
	val status: String? = null
)

data class Word(

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("word")
	val word: String? = null,

	@field:SerializedName("Description")
	val description: String? = null,
)
