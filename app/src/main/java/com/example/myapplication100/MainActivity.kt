package com.example.myapplication100

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.example.myapplication100.LoginRegis.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { App() }
    }
}

@Composable
fun App() {
    val nav = rememberNavController()
    val navBackStackEntry by nav.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: ""

    Scaffold(
        bottomBar = {
            if (currentRoute.startsWith("home") || currentRoute == "history" || currentRoute == "profile" || currentRoute == "booking") {
                BottomBar(nav)
            }
        }
    ) { pad ->
        NavHost(nav, startDestination = "login", Modifier.padding(pad)) {
            composable("login") { LoginScreen(nav) }
            composable("register1") { RegisterStep1Screen(nav) }
            composable("register2") { RegisterStep2Screen(nav) }

            // หน้า Home รับชื่อมาแสดง
            composable("home/{fname}/{lname}") { entry ->
                val fname = entry.arguments?.getString("fname") ?: "User"
                val lname = entry.arguments?.getString("lname") ?: ""
                HomeScreen(nav, fname, lname)
            }

            composable("booking") { BookingScreen(nav) }
            composable("history") { CenterScreen("ประวัติการรักษา") }
            composable("profile") { CenterScreen("ข้อมูลส่วนตัว") }
        }
    }
}

@Composable
fun HomeScreen(nav: NavHostController, fname: String, lname: String) {
    Column(Modifier.fillMaxSize().background(Color(0xFF21539D))) {
        // ส่วนหัวแสดงชื่อคน Login
        Column(Modifier.fillMaxWidth().padding(30.dp)) {
            Text("สวัสดี,", color = Color.White, fontSize = 16.sp)
            Text("คุณ $fname $lname", color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.Bold)
        }

        Column(Modifier.fillMaxSize().background(Color(0xFFF4F4F4), RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp)).padding(20.dp)) {
            // ... (UI บัตรคิว และ ปุ่มจองคิวเดิมของคุณ) ...
            Card(Modifier.fillMaxWidth().height(100.dp).clickable { nav.navigate("booking") }) {
                Box(Modifier.fillMaxSize().background(Color(0xFF3F7F8B)), contentAlignment = Alignment.Center) {
                    Text("จองคิวใหม่", color = Color.White, fontSize = 24.sp)
                }
            }
        }
    }
}
@Composable
fun BottomBar(navController: NavHostController) {
    // ตรวจสอบหน้าปัจจุบันเพื่อให้ปุ่ม Highlight สีได้ถูกต้อง
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 8.dp
    ) {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            label = { Text("หน้าแรก") },
            selected = currentRoute?.startsWith("home") == true,
            onClick = {
                // กันการกดซ้ำหน้าเดิม
                if (currentRoute?.startsWith("home") != true) {
                    // ดึงค่า fname/lname เดิมกลับไป (ถ้ามี) หรือไปหน้า home เริ่มต้น
                    navController.navigate("home/User/ ") {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.DateRange, contentDescription = "Booking") },
            label = { Text("จองคิว") },
            selected = currentRoute == "booking",
            onClick = { navController.navigate("booking") }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.List, contentDescription = "History") },
            label = { Text("ประวัติ") },
            selected = currentRoute == "history",
            onClick = { navController.navigate("history") }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
            label = { Text("โปรไฟล์") },
            selected = currentRoute == "profile",
            onClick = { navController.navigate("profile") }
        )
    }
}

@Composable
fun CenterScreen(title: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = title, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
    }
}

// ถ้าคุณยังไม่มี BookingScreen ให้เพิ่มตัวนี้ไว้ชั่วคราวกัน Error ครับ
@Composable
fun BookingScreen(navController: NavHostController) {
    CenterScreen("หน้าจองคิวใหม่")
}