package com.example.myapplication100.DataClass.Login

data class LoginRequest(
    val citizenId: String, // เปลี่ยนจาก username เป็น citizenId
    val password: String
)