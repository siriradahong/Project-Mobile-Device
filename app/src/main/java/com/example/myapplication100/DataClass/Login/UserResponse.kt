package com.example.myapplication100.DataClass.Login

import com.google.gson.annotations.SerializedName

data class UserResponse(
    val iduser: Int,
    val username: String,
    val firstname: String,
    val lastname: String,
    val role: String,
    @SerializedName("citizen_id")
    val citizen_id: String,
    // --- เพิ่ม 2 บรรทัดนี้เข้าไปสัส! ---
    val chronic_allergy: String? = null,
    val drug_allergy: String? = null
)