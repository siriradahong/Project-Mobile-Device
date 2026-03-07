package com.example.myapplication100.DataClass.Medicine_Prescription

data class Prescription(
    val idPrescription: Int? = null,
    val medicineName: String,  // เพิ่มชื่อยา
    val unitPrice: Double,     // เพิ่มราคายา
    val dosage: String?,
    val frequency: String?,
    val duration: String?,
    val instruction: String?,
    val quantity: Int,         // จำนวนที่หมอสั่ง
    val idexamination: Int,
    val idMedicine: Int
)