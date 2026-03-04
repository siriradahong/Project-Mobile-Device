package com.example.myapplication100

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// กำหนดสีตามธีมในรูป
val HospitalBlue = Color(0xFF194A8D)
val HospitalLightBlue = Color(0xFFD0E4FF)
val OrangeHeader = Color(0xFFFDB851)
val ButtonBlue = Color(0xFF6A7DAA)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QueueScreen() {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("ห้องตรวจ", color = Color.White) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = HospitalBlue)
            )
        },
        bottomBar = { BottomNavigationBar() }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(0xFFF5F5F5))
        ) {
            // ส่วนบนสุด - บัตรคิวที่กำลังตรวจ
            CurrentQueueCard()

            Spacer(modifier = Modifier.height(16.dp))

            // ส่วนรายการคิวถัดไป
            Text(
                text = "คิวถัดไป",
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp),
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(listOf("A12", "A13")) { queueId ->
                    NextQueueItem(queueId)
                }
            }
        }
    }
}

@Composable
fun CurrentQueueCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            // แถบสีส้มด้านบน
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(OrangeHeader)
                    .padding(vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("กำลังตรวจคิว", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // หมายเลขคิว
            Text("A11", fontSize = 90.sp, fontWeight = FontWeight.ExtraBold)
            Text("นายเตชสิทธิ์ แก้ววิเชียร", fontSize = 18.sp)

            Spacer(modifier = Modifier.height(20.dp))

            // ปุ่มแสดงรายละเอียด
            Button(
                onClick = { },
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .padding(bottom = 20.dp),
                colors = ButtonDefaults.buttonColors(containerColor = ButtonBlue),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.Person, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("แสดงรายละเอียด")
            }
        }
    }
}

@Composable
fun NextQueueItem(queueNumber: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // วงกลมหมายเลขคิว
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(30.dp))
                    .background(HospitalBlue),
                contentAlignment = Alignment.Center
            ) {
                Text(queueNumber, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp)
            }

            Spacer(modifier = Modifier.width(16.dp))

            // ชื่อและเวลา
            Column(modifier = Modifier.weight(1f)) {
                Text("นาย จิรภัทร พบวงษา", fontWeight = FontWeight.Bold)
                Text("เวลานัด: 11:30 น.", color = Color.Gray, fontSize = 14.sp)
            }

            // ปุ่มเรียกคิว
            Button(
                onClick = { },
                colors = ButtonDefaults.buttonColors(containerColor = HospitalLightBlue),
                contentPadding = PaddingValues(horizontal = 12.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = null, tint = HospitalBlue, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("เรียกคิว", color = HospitalBlue, fontSize = 12.sp)
            }
        }
    }
}

@Composable
fun BottomNavigationBar() {
    NavigationBar(containerColor = Color.White) {
        val items = listOf(
            Triple("หน้าแรก", Icons.Default.Home, false),
            Triple("จองคิว", Icons.Default.AddCircle, true),
            Triple("ประวัติการรักษา", Icons.Default.Refresh, false),
            Triple("ข้อมูลส่วนตัว", Icons.Default.Person, false)
        )
        items.forEach { (label, icon, selected) ->
            NavigationBarItem(
                selected = selected,
                onClick = { },
                label = { Text(label, fontSize = 10.sp) },
                icon = { Icon(icon, contentDescription = label) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = HospitalBlue,
                    selectedTextColor = HospitalBlue,
                    unselectedIconColor = Color.Gray,
                    unselectedTextColor = Color.Gray
                )
            )
        }
    }
}