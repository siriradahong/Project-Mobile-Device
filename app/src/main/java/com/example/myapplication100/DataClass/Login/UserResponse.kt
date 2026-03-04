package com.example.myapplication100.DataClass.Login

import com.google.gson.annotations.SerializedName

data class UserResponse(
    val iduser: Int,
    val username: String,
    val firstname: String, // ต้องสะกด f ตัวเล็กตามที่ UI เรียก
    val lastname: String,  // ต้องสะกด l ตัวเล็กตามที่ UI เรียก
    val role: String,
    @SerializedName("citizen_id")
    val citizen_id: String,
)