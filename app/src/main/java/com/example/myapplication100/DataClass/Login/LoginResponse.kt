package com.example.myapplication100.DataClass.Login

data class LoginResponse(
    val success: Boolean,
    val message: String?,
    val user: UserResponse?
)

data class User(
    val iduser: Int,
    val firstname: String
)