package com.example.myapplication100.DataClass.Medicine_Prescription

data class Prescription(
    val idPrescription: Int? = null,
    val dosage: String?,
    val frequency: String?,
    val duration: String?,
    val instruction: String?,
    val quantity: Int,
    val idexamination: Int,
    val idMedicine: Int
)