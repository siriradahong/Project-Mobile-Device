package com.example.myapplication100

import androidx.compose.ui.graphics.vector.Group
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.rememberNavController
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.*
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person

import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.navigation.NavController


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { App() }
    }
}

@Composable
fun App() {
    val nav = rememberNavController()

    Scaffold(
        bottomBar = { BottomBar(nav) },
        containerColor = Color(0xFF21539D)
    ) { pad ->

        NavHost(nav, "home", Modifier.padding(pad)) {
            composable("home") { HomeScreen(nav) }
            composable("history") { HistoryScreen() }
            composable("profile") { ProfileScreen() }
            composable("booking") { BookingScreen(nav) }
        }
    }
}

@Composable
fun BottomBar(nav: NavHostController) {
    NavigationBar(containerColor = Color.White) {

        NavigationBarItem(
            selected = false,
            onClick = { nav.navigate("home") },
            icon = { Icon(Icons.Default.Home, null) },
            label = { Text("หน้าแรก") }
        )

        NavigationBarItem(
            selected = false,
            onClick = { nav.navigate("booking") },
            icon = { Icon(Icons.Default.Add, null) },
            label = { Text("จองคิว") }
        )

        NavigationBarItem(
            selected = false,
            onClick = { nav.navigate("history") },
            icon = { Icon(Icons.Default.History, null) },
            label = { Text("ประวัติการรักษา") }
        )

        NavigationBarItem(
            selected = false,
            onClick = { nav.navigate("profile") },
            icon = { Icon(Icons.Default.Person, null) },
            label = { Text("ข้อมูลส่วนตัว") }
        )
    }
}

@Composable
fun HomeScreen(nav: NavHostController) {
    Column(
        Modifier.fillMaxSize().background(Color(0xFF21539D))
    ) {

        Box(
            Modifier.fillMaxWidth().height(100.dp),
            contentAlignment = Alignment.Center
        ) {
            Text("หน้าแรก", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
        }

        Column(
            Modifier.fillMaxSize()
                .background(Color(0xFFF4F4F4), RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp))
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Group, null, tint = Color.Gray)
                Spacer(Modifier.width(10.dp))
                Text("สมาชิกในครอบครัว")
                Spacer(Modifier.weight(1f))
                Box {
                    Icon(Icons.Default.Notifications, null)
                    Box(
                        Modifier.size(8.dp)
                            .background(Color.Red, CircleShape)
                            .align(Alignment.TopEnd)
                    )
                }
            }

            Card(
                shape = RoundedCornerShape(30.dp),
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {

                    Box(
                        Modifier.fillMaxWidth()
                            .background(Color(0xFFF79D3C))
                            .padding(12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("กำลังตรวจคิว", color = Color.White, fontSize = 26.sp, fontWeight = FontWeight.Bold)
                    }

                    Spacer(Modifier.height(10.dp))

                    Text("A12", fontSize = 100.sp, fontWeight = FontWeight.Bold)

                    Spacer(Modifier.height(8.dp))

                    Text(
                        buildAnnotatedString {
                            append("เหลือก่อนถึงคิวคุณ ")
                            withStyle(SpanStyle(color = Color.Red)) { append("4") }
                            append(" คิว")
                        }
                    )

                    Spacer(Modifier.height(20.dp))
                }
            }

            Card(
                Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .shadow(6.dp, RoundedCornerShape(24.dp))
                    .clickable { nav.navigate("booking") },
                shape = RoundedCornerShape(24.dp)
            ) {
                Box(
                    Modifier.fillMaxSize().background(Color(0xFF3F7F8B)),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Event, null, tint = Color.White)
                        Spacer(Modifier.width(12.dp))
                        Text("จองคิวใหม่", color = Color.White, fontSize = 26.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {

                Text("📍 นัดหมายของฉัน", fontSize = 18.sp, fontWeight = FontWeight.Bold)

                Appointment("ศุกร์ 25 ก.พ. 2569  10:30 น. - ปวดศีรษะ")
                Appointment("อังคาร 10 มี.ค. 2569  14:00 น. - ปวดใจ")
                Appointment("พฤหัส 22 มี.ค. 2569  10:30 น. - ปวดท้อง")
            }
        }
    }
}

@Composable
fun Appointment(text: String) {
    Card(
        Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Text(text, Modifier.padding(16.dp))
    }
}

@Composable
fun BookingScreen(nav: NavController) {

    var symptom by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF21539D))
    ) {

        Box(
            Modifier
                .fillMaxWidth()
                .padding(top = 48.dp, bottom = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            Text("จองคิวหาหมอ", color = Color.White, fontSize = 20.sp)
        }

        Column(
            Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
                .background(Color.White)
                .padding(20.dp)
        ) {

            Text("เลือกวันที่", fontSize = 20.sp, color = Color.Gray)

            Spacer(Modifier.height(12.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                repeat(5) {
                    DateItem(selected = it == 2)
                }
            }

            Spacer(Modifier.height(24.dp))

            Text("เลือกช่วงเวลา", fontSize = 20.sp, color = Color.Gray)

            Spacer(Modifier.height(12.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                TimeItem("09:00 - 12:00", "เช้า")
                TimeItem("13:00 - 16:00", "บ่าย")
            }

            Spacer(Modifier.height(24.dp))

            Text("อาการเบื้องต้น", fontSize = 20.sp, color = Color.Gray)

            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = symptom,
                onValueChange = { symptom = it },
                placeholder = { Text("รายละเอียดอาการ...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                shape = RoundedCornerShape(16.dp)
            )

            Spacer(Modifier.weight(1f))

            Button(
                onClick = { nav.navigate("home") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(40.dp),
                colors = ButtonDefaults.buttonColors(Color(0xFF3F7F8A))
            ) {
                Text("ยืนยันการจองคิว", fontSize = 18.sp)
            }
        }
    }
}

@Composable
fun DateItem(selected: Boolean) {

    val bg = if (selected) Color(0xFF21539D) else Color(0xFFF4F8F5)
    val text = if (selected) Color.White else Color.Gray

    Column(
        modifier = Modifier
            .size(width = 60.dp, height = 90.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(bg),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("จ.", color = text)
        Text("26", color = text, fontSize = 22.sp, fontWeight = FontWeight.Bold)
        Text("ม.ค.", color = text)
    }
}

@Composable
fun TimeItem(time: String, period: String) {

    Box(
        modifier = Modifier
            .width(160.dp)
            .height(64.dp)
            .clip(RoundedCornerShape(40.dp))
            .border(1.dp, Color(0xFF21539D), RoundedCornerShape(40.dp)),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(time)
            Text("($period)")
        }
    }
}






@Composable
fun CenterScreen(title: String) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(title, fontSize = 22.sp)
    }
}