package com.example.myapplication100.DataClass.Medicine_Prescription

import com.google.gson.annotations.SerializedName

data class Medicine(
    @SerializedName("idMedicine") val idMedicine: Int? = null,

    @SerializedName("medicine_name") val medicineName: String,

    @SerializedName("type") val type: String?,

    @SerializedName("unit_price") // 🟢 ตรงกับ unit_price ใน DB
    val unitPrice: Double,

    @SerializedName("stock_quantity") // 🟢 ตรงกับ stock_quantity ใน DB
    val stockQuantity: Int,

    @SerializedName("unit") val unit: String?,
    @SerializedName("description") val description: String?
)