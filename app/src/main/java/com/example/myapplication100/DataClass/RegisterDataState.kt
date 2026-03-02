package com.example.myapplication100.DataClass

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue  // เพิ่มบรรทัดนี้
import androidx.compose.runtime.setValue  // เพิ่มบรรทัดนี้
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
// ในไฟล์ RegisterDataState.kt หรือตำแหน่งที่ประกาศ object นี้
object RegisterDataState {
    var citizenId by mutableStateOf("") // เพิ่มตัวนี้
    var firstName by mutableStateOf("")
    var lastName by mutableStateOf("")
    var email by mutableStateOf("")
    var password by mutableStateOf("")
}