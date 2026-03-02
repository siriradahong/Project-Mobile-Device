package com.example.myapplication100.DataClass.Login

// สำหรับพยาบาลกรอกสัญญาณชีพ
data class VitalsRequest(
    val weight: Double,
    val height: Double,
    val temperature: Double,
    val blood_presure_sys: Int,
    val blood_presure_dia: Int,
    val heart_rate: Int
)