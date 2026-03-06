package com.example.myapplication100

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication100.DataClass.Family.FamilyMemberDetail
import com.example.myapplication100.LoginRegis.RetrofitClient

@Composable
fun FamilySection(userId: Int) {
    var members by remember { mutableStateOf<List<FamilyMemberDetail>>(emptyList()) }
    var familyName by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(true) }
    var hasFamily by remember { mutableStateOf(false) } // เช็คว่ามีครอบครัวไหม

    LaunchedEffect(userId) {
        try {
            val response = RetrofitClient.instance.getFamilyMembersByUserId(userId)
            if (response.isSuccessful) {
                val data = response.body() ?: emptyList()
                if (data.isNotEmpty()) {
                    members = data
                    familyName = data[0].family_name ?: "ครอบครัวของฉัน"
                    hasFamily = true
                } else {
                    hasFamily = false
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            isLoading = false
        }
    }

    if (isLoading) {
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(modifier = Modifier.padding(16.dp))
        }
    } else if (hasFamily) {
        Column(modifier = Modifier.padding(8.dp)) {
            // แสดงเฉพาะเมื่อมีครอบครัว (Family_idFamily ไม่เป็น NULL)
            FamilyHeaderGroup(title = familyName, members = members)
        }
    } else {
        // กรณี Family_idFamily เป็น NULL
        Card(
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
        ) {
            Text(
                "ยังไม่ได้ลงทะเบียนข้อมูลครอบครัว",
                modifier = Modifier.padding(16.dp),
                color = Color.Gray,
                fontSize = 14.sp
            )
        }
    }
}

@Composable
fun FamilyHeaderGroup(title: String, members: List<FamilyMemberDetail>) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFDDE8FF)) // สีฟ้าอ่อน DDE8FF
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = !expanded }
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = title, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Icon(
                    imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = null
                )
            }

            if (expanded) {
                Column(
                    modifier = Modifier
                        .background(Color.White) // พื้นหลังข้างในเป็นสีขาวเพื่อให้การ์ดลูกเด่น
                        .padding(8.dp)
                ) {
                    if (members.isEmpty()) {
                        Text("ไม่พบสมาชิกในครอบครัว", modifier = Modifier.padding(8.dp))
                    } else {
                        members.forEach { member ->
                            FamilyMemberCard(member)
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FamilyMemberCard(member: FamilyMemberDetail) {
    // ดีไซน์การ์ดลูกสีขาว ขอบเทา ตามรูปที่ 2
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color(0xFFEEEEEE))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "${member.firstName} ${member.lastName}",
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = Color(0xFF2F5DAA)
            )
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), thickness = 0.5.dp)

            Text("อาการ: ${member.symptom ?: "ไม่มีข้อมูล"}", fontSize = 14.sp)
            Text("ผลวินิจฉัย: ${member.diagnosis ?: "รอการตรวจ"}", fontSize = 14.sp)

            member.examinationDate?.let {
                Text(
                    text = "วันที่ตรวจ: ${formatDate(it)}",
                    fontSize = 12.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}