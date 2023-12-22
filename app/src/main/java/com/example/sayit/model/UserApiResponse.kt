package com.example.sayit.model

import com.google.gson.annotations.SerializedName

data class GeneralRegisterResponse(

	@field:SerializedName("data")
	val data: RegisterResponse? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class RegisterResponse(

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("username")
	val username: String? = null
)

data class GeneralLoginResponse(

	@field:SerializedName("data")
	val loginResult: LoginResult? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class LoginResult(

	@field:SerializedName("Id")
	val id: String? = null,

	@field:SerializedName("Email")
	val email: String? = null,

	@field:SerializedName("Username")
	val username: String? = null,

	@field:SerializedName("Token")
	val token: String? = null
)

data class GeneralUserResponse(

	@field:SerializedName("data")
	val user: UserResponse? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class UserResponse(

	@field:SerializedName("ProfilePicture")
	val profilePicture: String? = null,

	@field:SerializedName("Id")
	val id: String? = null,

	@field:SerializedName("Email")
	val email: String? = null,

	@field:SerializedName("Username")
	val username: String? = null
)




