package com.example.myapplication100

data class VitalsRequest(

    val appointment_id: Int,

    val blood_pressure_sys: Int,
    val blood_pressure_dia: Int,
    val heart_rate: Int,
    val weight: Double,
    val height: Double,
    val temperature: Double,
    val note: String

)
