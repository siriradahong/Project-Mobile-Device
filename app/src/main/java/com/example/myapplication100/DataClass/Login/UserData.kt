package com.example.myapplication100.DataClass.Login

import com.google.gson.annotations.SerializedName

data class UserData(
    val id: Int,
    val username: String,
    val firstname: String, // ตรวจสอบตัวสะกด f ตัวเล็ก
    val lastname: String,  // ตรวจสอบตัวสะกด l ตัวเล็ก
    val role: String,

)