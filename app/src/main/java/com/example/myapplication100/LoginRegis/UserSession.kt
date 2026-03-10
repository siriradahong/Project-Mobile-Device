package com.example.myapplication100.LoginRegis

object UserSession {
    var iduser: Int = 0
    var firstName: String = ""
    var lastName: String = ""
    var citizenId: String = ""

    var role: String = ""
    // เพิ่มฟิลด์อื่นๆ ที่ต้องการโชว์ เช่น
    // var birthdate: String = ""
    fun clear() {
        iduser = 0
        firstName = ""
        lastName = ""
        citizenId = ""
        role = ""
        // เคลียร์ตัวแปรทุกตัวที่มีในนี้ให้เป็น null หรือค่าเริ่มต้น
    }

}