package com.example.myapplication100

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// กำหนดสีตามตัวอย่าง
val DarkBlue = Color(0xFF194A8D)
val LightPurple = Color(0xFF6A7DAA)
val HeaderGrey = Color(0xFF949FB7)
val BgGray = Color(0xFFF8F9FA)
val TealButton = Color(0xFF4A868E)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TreatmentNoteScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBlue) // พื้นหลังส่วนบนสุด
    ) {
        // ส่วน Header / Back Button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 48.dp, start = 8.dp, bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.ArrowBack, contentDescription = null, tint = Color.White)
            Text(
                " Medical Record(Treatment Note)",
                color = Color.White,
                fontSize = 16.sp
            )
        }

        // ส่วนเนื้อหาหลัก (สีขาวขอบโค้ง)
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color.White,
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // ชื่อคนไข้และคิว
                Text("นายคัดมุสคิส ฟัดคัสตุช", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Text("คิว (A50)", fontSize = 20.sp, fontWeight = FontWeight.Bold)

                Spacer(modifier = Modifier.height(16.dp))

                // ส่วนแสดงผล Vitals (BP, TP, HG, WG)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    VitalBox("BP", "150", labelColor = Color.Red)
                    VitalBox("TP", "36.5")
                    VitalBox("HG", "178")
                    VitalBox("WG", "60")
                }

                Spacer(modifier = Modifier.height(20.dp))

                // ประวัติแพ้ยา
                InfoBanner("ประวัติแพ้ยา: Penicillin", LightPurple)

                Spacer(modifier = Modifier.height(16.dp))

                // การ์ดประวัติการรักษา
                TreatmentHistoryCard()

                Spacer(modifier = Modifier.height(16.dp))

                // ช่องกรอกข้อมูล
                OutlinedTextField(
                    value = "", onValueChange = {},
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("กรอกอาการ", color = Color.LightGray) },
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = "", onValueChange = {},
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("กรอกผลวินิจฉัย", color = Color.LightGray) },
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                // ... (โค้ดส่วนบนคงเดิม)

                // Dropdown กรอกยา
                OutlinedTextField(
                    value = "", onValueChange = {},
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("กรอกยา", color = Color.LightGray) },
                    trailingIcon = { Icon(Icons.Default.KeyboardArrowDown, null) },
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                // --- เพิ่มช่องนัดหมายล่วงหน้าตรงนี้ ---
                OutlinedTextField(
                    value = "", // สามารถเชื่อมต่อกับ State ของเวลาหรือวันที่ได้
                    onValueChange = {},
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("นัดหมายล่วงหน้า", color = Color.Gray) }, // ใส่ label เพื่อให้ชื่อช่องชัดเจน
                    placeholder = { Text("ระบุวันและเวลาที่นัดหมาย", color = Color.LightGray) },
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.DateRange, // แนะนำให้ใช้ไอคอนปฏิทินเพื่อให้สื่อความหมาย
                            contentDescription = null,
                            tint = DarkBlue
                        )
                    },
                    shape = RoundedCornerShape(12.dp),
                    readOnly = true // ปกติช่องเวลาควรให้กดเลือกจาก Dialog มากกว่าพิมพ์เอง
                )

                Spacer(modifier = Modifier.height(24.dp))

                // ปุ่มปิดเคส
                Button(
                    onClick = { /* ไปยังหน้าใบเสร็จ */ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = TealButton),
                    shape = RoundedCornerShape(28.dp)
                ) {
                    Text("ปิดเคส", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
// ... (โค้ดส่วนล่างคงเดิม)

                Spacer(modifier = Modifier.height(24.dp))

                // ปุ่มปิดเคส
                Button(
                    onClick = { },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = TealButton),
                    shape = RoundedCornerShape(28.dp)
                ) {
                    Text("ปิดเคส", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun VitalBox(label: String, value: String, labelColor: Color = Color.Black) {
    Card(
        modifier = Modifier.size(width = 75.dp, height = 80.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(label, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            Text(value, fontSize = 18.sp, color = labelColor, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun InfoBanner(text: String, color: Color) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = color,
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(12.dp),
            color = Color.White,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun TreatmentHistoryCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(HeaderGrey)
                    .padding(8.dp)
            ) {
                Text("ประวัติการรักษา", color = Color.White, fontWeight = FontWeight.Bold)
            }
            Column(modifier = Modifier.padding(12.dp)) {
                TreatmentRow("อาการ:", "ปวดอก ปวดหัว")
                TreatmentRow("ผลวินิจฉัย:", "อกหัก")
                TreatmentRow("ยาที่ได้รับ:", "พารา 1 ชุด, เพลงเมื่อไหร่ที่คิดถึงเธอ")
            }
        }
    }
}

@Composable
fun TreatmentRow(label: String, value: String) {
    Row(modifier = Modifier.padding(vertical = 4.dp)) {
        Text(label, modifier = Modifier.width(80.dp), fontWeight = FontWeight.Bold)
        Text(value, modifier = Modifier.weight(1f))
    }
}