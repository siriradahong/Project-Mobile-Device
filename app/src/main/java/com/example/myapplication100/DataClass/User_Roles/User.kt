package com.example.myapplication100.DataClass.User_Roles

import com.google.gson.annotations.SerializedName

// ตารางหลัก user
data class User(
    @SerializedName("iduser") val iduser: Int,
    @SerializedName("username") val username: String,
    @SerializedName("firstname") val firstname: String,
    @SerializedName("lastname") val lastname: String,
    @SerializedName("phone") val phone: String,
    @SerializedName("email") val email: String
)