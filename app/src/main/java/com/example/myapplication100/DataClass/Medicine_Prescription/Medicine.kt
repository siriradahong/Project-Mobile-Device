package com.example.myapplication100.DataClass.Medicine_Prescription

data class Medicine(
    val idMedicine: Int? = null,
    val Medicine_name: String,
    val type: String?,
    val unit_price: Double,
    val stock_quantity: Int,
    val unit: String?,
    val description: String?
)