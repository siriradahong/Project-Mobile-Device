package com.example.myapplication100.DataClass.Login

import com.google.gson.annotations.SerializedName

// 🟢 ตัวนี้แหละสัสที่จะแก้ Unresolved Reference
data class QueueResponse(
    @SerializedName("current_queue")
    val current_queue: String
)