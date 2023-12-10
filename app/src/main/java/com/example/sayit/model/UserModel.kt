package com.example.sayit.model

data class UserModel(
    val userId: String,
    val email: String,
    val username: String,
    val token: String,
    val isLogin: Boolean = false
)