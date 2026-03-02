package com.example.myapplication100.DataClass.User_Roles

// ตารางคนไข้ (ที่เก็บข้อมูลสุขภาพตอนสมัคร Step 2)
data class Patient(
    val iduser: Int, // PK ที่มาจาก user
    val birthdate: String?, // รูปแบบ YYYY-MM-DD
    val gender: String?,    // Male, Female, Other
    val blood_type: String?, // A, B, AB, O
    val rh_factor: String?,  // +, -
    val chronic_allergy: String?,
    val drug_allergy: String?,
    val Family_idFamily: Int?
)