package com.example.myapplication100

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.example.myapplication100.DataClass.Appointment_Examination.Appointment
import com.example.myapplication100.DataClass.Appointment_Examination.AppointmentRepository
import com.example.myapplication100.DataClass.Appointment_Examination.AppointmentViewModel
import com.example.myapplication100.DataClass.Appointment_Examination.AppointmentViewModelFactory
import com.example.myapplication100.LoginRegis.*
import java.util.Calendar
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import com.example.myapplication100.LoginRegis.UserSession.iduser


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
                HomeScreen(nav)
            }

            composable("booking") { BookingScreen(nav) }
            composable("history") { CenterScreen("ประวัติการรักษา") }
            composable("profile") {
                Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
                    Spacer(Modifier.height(50.dp))
                    Text("ข้อมูลส่วนตัว", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(20.dp))

                    // แสดงข้อมูลของคนๆ นั้นที่เก็บไว้ใน Session
                    Text("ชื่อ-นามสกุล: ${UserSession.firstName} ${UserSession.lastName}")
                    Text("เลขบัตรประชาชน: ${UserSession.citizenId}")
                }
            }
        }
    }
}

@Composable
fun HomeScreen(nav: NavHostController) {
    // 1. เตรียม ViewModel และดึงข้อมูล
    val api = RetrofitClient.instance
    val repository = AppointmentRepository(api)
    val factory = AppointmentViewModelFactory(repository)
    val viewModel: AppointmentViewModel = viewModel(factory = factory)

    val appointments by viewModel.appointments.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadAppointments(UserSession.iduser)
    }
    val currentQueue = viewModel.currentQueue.value
    val remaining = viewModel.remainingQueue.value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF21539D)) // สีน้ำเงินด้านบน
    ) {
        // ส่วนหัว "หน้าแรก"
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "หน้าแรก",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }

        // พื้นหลังสีขาวโค้งมน
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color.White,
            shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item { Spacer(modifier = Modifier.height(20.dp)) }

                // แถวโปรไฟล์และแจ้งเตือน
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            // จำลองรูปโปรไฟล์
                            Box(
                                modifier = Modifier
                                    .size(45.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFE0E0E0)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.Groups, contentDescription = null, tint = Color.Gray)
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = "สมาชิกในครอบครัว",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }

                        // ไอคอนแจ้งเตือนพร้อมจุดแดง
                        Box {
                            Icon(
                                imageVector = Icons.Default.Notifications,
                                contentDescription = "Notifications",
                                tint = Color(0xFF21539D),
                                modifier = Modifier.size(28.dp)
                            )
                            Box(
                                modifier = Modifier
                                    .size(10.dp)
                                    .clip(CircleShape)
                                    .background(Color.Red)
                                    .align(Alignment.TopEnd)
                            )
                        }
                    }
                }

                item { Spacer(modifier = Modifier.height(24.dp)) }

                // การ์ดแสดงคิว
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(8.dp, RoundedCornerShape(20.dp)),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color(0xFFFFA726)) // สีส้ม
                                    .padding(vertical = 10.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "กำลังตรวจคิว",
                                    color = Color.White,
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Text(
                                text = currentQueue, // เลขคิวจริง
                                fontSize = 100.sp,
                                fontWeight = FontWeight.Black
                            )

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(text = "เหลือก่อนถึงคิวคุณ ", fontSize = 18.sp)
                                Text(
                                    text = "$remaining", // จำนวนคิวที่เหลือจริง
                                    color = Color.Red,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(text = " คิว", fontSize = 18.sp)
                            }
                            Spacer(modifier = Modifier.height(20.dp))
                        }
                    }
                }

                item { Spacer(modifier = Modifier.height(24.dp)) }

                // ปุ่มจองคิวใหม่
                item {
                    Button(
                        onClick = { nav.navigate("booking") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(80.dp)
                            .shadow(4.dp, RoundedCornerShape(15.dp)),
                        shape = RoundedCornerShape(15.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3F7F8B)) // สีเขียวหัวเป็ด
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default.CalendarMonth,
                                contentDescription = null,
                                modifier = Modifier.size(36.dp)
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Text(
                                text = "จองคิวใหม่",
                                fontSize = 30.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                item { Spacer(modifier = Modifier.height(30.dp)) }

                // หัวข้อนัดหมาย
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.LocationOn,
                            contentDescription = null,
                            tint = Color.Red,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "นัดหมายของฉัน",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                item { Spacer(modifier = Modifier.height(10.dp)) }


                // 3. แสดงรายการนัดหมายจริง
                items(appointments) { app ->
                    AppointmentCard("${app.Appointment_date} ${app.time_slot} - ${app.initial_symptom}")
                    Spacer(modifier = Modifier.height(12.dp))
                }

                item { Spacer(modifier = Modifier.height(100.dp)) } // เผื่อที่ให้ Bottom Bar
            }
        }
    }
}
@Composable
fun AppointmentCard(text: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(2.dp, RoundedCornerShape(12.dp)),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = androidx.compose.foundation.BorderStroke(0.5.dp, Color.LightGray)
    ) {
        Box(modifier = Modifier.padding(16.dp)) {
            Text(
                text = text,
                fontSize = 15.sp,
                fontWeight = FontWeight.Normal,
                color = Color.Black
            )
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

                val fn = UserSession.firstName.ifEmpty { "User" }
                val ln = UserSession.lastName.ifEmpty { " " }
                navController.navigate("home/$fn/$ln") {
                    popUpTo(navController.graph.startDestinationId) { saveState = true }
                    launchSingleTop = true
                    restoreState = true

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingScreen(navController: NavHostController) {
    val api = RetrofitClient.instance
    val repository = AppointmentRepository(api)
    val factory = AppointmentViewModelFactory(repository)
    val viewModel: AppointmentViewModel = viewModel(factory = factory)

    var selectedType by remember { mutableStateOf("") }

    var selectedTimeIndex by remember { mutableStateOf("") }
    var symptom by remember { mutableStateOf("") }

    val timeSlots = listOf(
        "09:00 - 12:00",
        "13:00 - 16:00"
    )
    val type = listOf(
        "Online","Walk-in"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF2F2F2))
    ) {

        // 🔵 Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF2F5DAA))
                .padding(vertical = 20.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "จองคิวหาหมอ",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),) {

            // 📅 เลือกวันที่
            val context = LocalContext.current
            val calendar = Calendar.getInstance()

            var selectedDate by rememberSaveable { mutableStateOf("") }

            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                context,
                { _, selectedYear, selectedMonth, selectedDay ->
                    val formattedDate = String.format(
                        "%04d-%02d-%02d",
                        selectedYear,
                        selectedMonth + 1,
                        selectedDay
                    )
                    selectedDate = formattedDate
                },
                year, month, day
            )

            Text(
                text = "วันที่จอง",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = selectedDate,
                onValueChange = {},
                placeholder = { Text("เลือกวันที่") },
                readOnly = true,
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(5.dp),
                trailingIcon = {
                    IconButton(onClick = { datePickerDialog.show() }) {
                        Icon(Icons.Default.DateRange, contentDescription = "Pick Date")
                    }
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "เลือกรูปแบบการจอง",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                type.forEach { it ->

                    val isSelected = it == selectedType

                    OutlinedButton(
                        onClick = { selectedType = it },
                        modifier = Modifier
                            .weight(1f)
                            .height(55.dp),
                        shape = RoundedCornerShape(5.dp),
                        border = BorderStroke(
                            1.dp,
                            if (isSelected) Color(0xFF2F5DAA)
                            else Color.Gray
                        ),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = if (isSelected)
                                Color(0xFFE3EDFF)
                            else Color.Transparent
                        )
                    ) {
                        Text(
                            text = it,
                            color = Color.Black,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))


            // ⏰ เลือกช่วงเวลา
            Text(
                text = "เลือกช่วงเวลา",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                timeSlots.forEach { it ->

                    val isSelected = it == selectedTimeIndex

                    OutlinedButton(
                        onClick = { selectedTimeIndex = it },
                        modifier = Modifier
                            .weight(1f)
                            .height(55.dp),
                        shape = RoundedCornerShape(5.dp),
                        border = BorderStroke(
                            1.dp,
                            if (isSelected) Color(0xFF2F5DAA)
                            else Color.Gray
                        ),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = if (isSelected)
                                Color(0xFFE3EDFF)
                            else Color.Transparent
                        )
                    ) {
                        Text(
                            text = it,
                            color = Color.Black,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 🩺 อาการเบื้องต้น
            Text(
                text = "อาการเบื้องต้น",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = symptom,
                onValueChange = { symptom = it },
                placeholder = { Text("รายละเอียดอาการ...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                shape = RoundedCornerShape(5.dp)
            )

            Spacer(modifier = Modifier.height(100.dp))

            // 🔘 ปุ่มยืนยัน
            Button(
                onClick = {
                    if (selectedDate.isNotEmpty() && selectedType.isNotEmpty() && selectedTimeIndex.isNotEmpty() && symptom.isNotEmpty()){
                        val appointment = Appointment(
                            idAppointment = null,
                            patient_iduser = UserSession.iduser,
                            booking_by_user_id = UserSession.iduser,
                            Appointment_date = selectedDate,
                            time_slot = selectedTimeIndex,
                            initial_symptom = symptom,
                            queue_number = null,
                            check_in_time = null,
                            status = "Pending",
                            booking_type = selectedType
                        )
                        viewModel.createAppointment(appointment)
                        navController.popBackStack()
                    }else{

                    }
                },

                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp),
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF4A8C8E)
                )
            ) {
                Text(
                    text = "ยืนยันการจองคิว",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

