package com.example.myapplication100.DataClass.Login

data class RegisterRequest(
    val username: String,
    val password: String,
    val firstname: String,
    val lastname: String,
    val phone: String,
    val email: String,
    val birthdate: String,   // ส่งเป็น "YYYY-MM-DD"
    val gender: String,      // "Male", "Female"
    val bloodType: String,   // "A", "B", "AB", "O"
    val rhFactor: String,    // "+", "-"
    val chronicAllergy: String,
    val drugAllergy: String,
    val citizenId: String
)