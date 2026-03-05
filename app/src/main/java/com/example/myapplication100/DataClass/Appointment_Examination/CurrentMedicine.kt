package com.example.myapplication100.DataClass.Appointment_Examination


data class CurrentMedicine(
    val idPrescription: Int,
    val medicine_name: String,  // เพิ่มตรงนี้
    val dosage: String,
    val frequency: String,
    val instruction: String
)