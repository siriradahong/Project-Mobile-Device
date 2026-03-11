package com.example.myapplication100

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myapplication100.DataClass.Appointment_Examination.Examination
import com.example.myapplication100.LoginRegis.RetrofitClient
import com.example.myapplication100.LoginRegis.UserSession

import androidx.compose.runtime.*

@Composable
fun ProfileScreen(nav: NavController) {
    val userId = UserSession.iduser

    Column(
        Modifier
            .fillMaxSize()
            .background(Color(0xFFF0F4FF))
            .verticalScroll(rememberScrollState())
    ) {
        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF2F5DAA))
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("ข้อมูลส่วนตัว", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color.White)
                Spacer(Modifier.height(8.dp))
                Text(
                    text = "${UserSession.firstName ?: ""} ${UserSession.lastName ?: ""}",
                    fontSize = 18.sp,
                    color = Color.White
                )
                Text(UserSession.citizenId ?: "", fontSize = 14.sp, color = Color.White.copy(alpha = 0.8f))
            }
        }

        Spacer(Modifier.height(16.dp))

        // ข้อมูลส่วนตัว
        ExpandableCard(title = "ข้อมูลส่วนตัว") {
            InfoCard(userId = userId)
        }
        Spacer(Modifier.height(12.dp))

        if(UserSession.role != "Doctor" && UserSession.role != "Nurse"){
            // ประวัติการรักษา
            ExpandableCard(title = "ประวัติการรักษา") {
                HistoryScreen(userId = userId)
            }
        }


        Spacer(Modifier.height(12.dp))

        if(UserSession.role != "Doctor" && UserSession.role != "Nurse"){
            // รายการยาปัจจุบัน
            ExpandableCard(title = "รายการยาปัจจุบัน") {
                MedicineCard(userId = userId)
            }

            Spacer(Modifier.height(24.dp))
        }

        ExpandableCard(title = "สมาชิกครอบครัว") {
            FamilySection(userId = userId)
        }

        Spacer(Modifier.height(12.dp))

        if (UserSession.role == "Nurse") {
            ExpandableCard(title = "จัดการรายชื่อครอบครัว") {
                FamilyMemberListView(userId = userId)
            }
            Spacer(Modifier.height(12.dp))
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 32.dp), // ใส่ padding ซ้าย-ขวา ให้มีช่องว่างนิดหน่อย
            horizontalArrangement = Arrangement.Center
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth() // 🔴 จุดสำคัญ: สั่งให้ Card ขยายเต็มความกว้าง Row สัส!
                    .clickable {
                        UserSession.clear()
                        nav.navigate("login") {
                            popUpTo(0)
                        }
                    },
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEBEE))
            ) {
                // ใช้ Box เพื่อให้จัดตัวหนังสืออยู่ตรงกลางปุ่มที่ขยายแล้ว
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp), // ปรับความสูงของปุ่มตรงนี้
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "ออกจากระบบ",
                        color = Color(0xFFD32F2F),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun ExpandableCard(title: String, content: @Composable () -> Unit) {
    var expanded by remember { mutableStateOf(true) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column {
            // Header row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = !expanded }
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(title, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Icon(
                    imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = null
                )
            }

            if (expanded) {
                Divider(color = Color(0xFFEEEEEE))
                content()
            }
        }
    }
}