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

data class GradingResponse(

	@field:SerializedName("data")
	val gradeResult: GradingResult,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class GradingResult(

	@field:SerializedName("transcription")
	val transcription: String? = null,

	@field:SerializedName("accuracy")
	val accuracy: Int? = null,

	@field:SerializedName("compute_cer")
	val computeCer: Int? = null
)
