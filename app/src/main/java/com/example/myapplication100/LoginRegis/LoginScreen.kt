package com.example.myapplication100.LoginRegis

import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.* // นำเข้าทั้งหมดเพื่อใช้ getValue และ setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.myapplication100.DataClass.Login.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

// --- Theme Colors ---
val MainBlue = Color(0xFF154897)
val DeepBlue = Color(0xFF003366)
val BgGray = Color(0xFFF5F5F5)
val NavyButton = Color(0xFF0D1B2A)
val BlueGradient = Brush.verticalGradient(colors = listOf(MainBlue, DeepBlue))

// --- State Management ---
object RegisterDataState {
    var citizenId by mutableStateOf("") // เพิ่มตัวแปรเก็บเลขบัตร
    var firstName by mutableStateOf("")
    var lastName by mutableStateOf("")
    var email by mutableStateOf("")
    var password by mutableStateOf("")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginTextField(value: String, onValueChange: (String) -> Unit, placeholder: String, isPassword: Boolean = false) {
    TextField(
        value = value, onValueChange = onValueChange,
        placeholder = { Text(placeholder, color = Color.Black, fontSize = 14.sp) },
        modifier = Modifier.fillMaxWidth().height(60.dp).shadow(8.dp, RoundedCornerShape(12.dp)),
        shape = RoundedCornerShape(12.dp),
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
        colors = TextFieldDefaults.colors(focusedContainerColor = BgGray, unfocusedContainerColor = BgGray, focusedIndicatorColor = Color.Transparent, unfocusedIndicatorColor = Color.Transparent),
        singleLine = true
    )
}

@Composable
fun RegisterHeader(step: String, onBackClick: () -> Unit) {
    Box(modifier = Modifier.fillMaxWidth()) {
        IconButton(onClick = onBackClick, modifier = Modifier.padding(start = 10.dp, top = 20.dp)) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back", tint = Color.White, modifier = Modifier.size(30.dp))
        }
        Row(modifier = Modifier.fillMaxWidth().padding(start = 35.dp, top = 60.dp, bottom = 30.dp), verticalAlignment = Alignment.CenterVertically) {
            Surface(modifier = Modifier.size(50.dp), shape = CircleShape, color = Color.White) {
                Box(contentAlignment = Alignment.Center) { Text(step, fontSize = 26.sp, fontWeight = FontWeight.Bold, color = DeepBlue) }
            }
            Spacer(Modifier.width(20.dp))
            Column {
                Text("หมอ prompt", color = Color.White, fontSize = 38.sp, fontWeight = FontWeight.Bold)
                Text("สมัครสมาชิก", color = Color.White, fontSize = 20.sp)
            }
        }
    }
}

@Composable
fun RegisterStep1Screen(navController: NavHostController) {
    Column(modifier = Modifier.fillMaxSize().background(BlueGradient)) {
        RegisterHeader(step = "1", onBackClick = { navController.navigate("login") })
        Surface(modifier = Modifier.fillMaxSize(), color = Color.White, shape = RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp)) {
            Column(modifier = Modifier.padding(horizontal = 30.dp).verticalScroll(rememberScrollState())) {
                Spacer(Modifier.height(35.dp))
                Text("ข้อมูลบัญชี"  , fontSize = 16.sp, fontWeight = FontWeight.Bold, color = MainBlue)
                Spacer(Modifier.height(15.dp))

                // ช่องเลขบัตรประชาชน
                LoginTextField(
                    value = RegisterDataState.citizenId,
                    onValueChange = { if (it.length <= 13) RegisterDataState.citizenId = it },
                    placeholder = "เลขบัตรประชาชน 13 หลัก"
                )
                Spacer(Modifier.height(20.dp))

                Row {
                    Box(Modifier.weight(1f)) { LoginTextField(RegisterDataState.firstName, { RegisterDataState.firstName = it }, "ชื่อ") }
                    Spacer(Modifier.width(12.dp))
                    Box(Modifier.weight(1f)) { LoginTextField(RegisterDataState.lastName, { RegisterDataState.lastName = it }, "นามสกุล") }
                }
                Spacer(Modifier.height(20.dp))
                LoginTextField(RegisterDataState.email, { RegisterDataState.email = it }, "อีเมล")
                Spacer(Modifier.height(20.dp))
                LoginTextField(RegisterDataState.password, { RegisterDataState.password = it }, "รหัสผ่าน", isPassword = true)
                Spacer(Modifier.height(40.dp))
                Button(
                    onClick = {
                        if (RegisterDataState.citizenId.length == 13) {
                            navController.navigate("register2")
                        }
                    },
                    modifier = Modifier.align(Alignment.End).width(150.dp).height(60.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = NavyButton)
                ) { Text("ถัดไป"); Icon(Icons.Default.ArrowForward, null) }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterStep2Screen(navController: NavHostController) {
    var phone by remember { mutableStateOf("") }
    var allergy by remember { mutableStateOf("") }
    var selectedGender by remember { mutableStateOf("") }
    var selectedDateText by remember { mutableStateOf("วันเกิด") }
    var genderExpanded by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()
    val scope = rememberCoroutineScope()

    val genderOptions = listOf("Male", "Female", "Other")

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    selectedDateText = datePickerState.selectedDateMillis?.let {
                        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date(it))
                    } ?: "วันเกิด"
                    showDatePicker = false
                }) { Text("เลือก") }
            }
        ) { DatePicker(state = datePickerState) }
    }

    Column(modifier = Modifier.fillMaxSize().background(BlueGradient)) {
        RegisterHeader(step = "2", onBackClick = { navController.popBackStack() })
        Surface(modifier = Modifier.fillMaxSize(), color = Color.White, shape = RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp)) {
            Column(modifier = Modifier.padding(horizontal = 30.dp).verticalScroll(rememberScrollState())) {
                Spacer(Modifier.height(35.dp))
                LoginTextField(phone, { phone = it }, "เบอร์โทรศัพท์")
                Spacer(Modifier.height(20.dp))

                ExposedDropdownMenuBox(expanded = genderExpanded, onExpandedChange = { genderExpanded = !genderExpanded }) {
                    TextField(
                        value = selectedGender, onValueChange = {}, readOnly = true,
                        placeholder = { Text("เลือกเพศ") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = genderExpanded) },
                        modifier = Modifier.menuAnchor().fillMaxWidth(),
                        colors = TextFieldDefaults.colors(focusedContainerColor = BgGray, unfocusedContainerColor = BgGray)
                    )
                    ExposedDropdownMenu(expanded = genderExpanded, onDismissRequest = { genderExpanded = false }) {
                        genderOptions.forEach { option ->
                            DropdownMenuItem(text = { Text(option) }, onClick = { selectedGender = option; genderExpanded = false })
                        }
                    }
                }

                Spacer(Modifier.height(20.dp))
                OutlinedButton(onClick = { showDatePicker = true }, modifier = Modifier.fillMaxWidth().height(60.dp), shape = RoundedCornerShape(12.dp)) {
                    Icon(Icons.Default.DateRange, null)
                    Spacer(Modifier.width(8.dp))
                    Text(selectedDateText)
                }

                Spacer(Modifier.height(20.dp))
                LoginTextField(allergy, { allergy = it }, "ประวัติการแพ้ยา/โรคประจำตัว")

                Spacer(Modifier.height(40.dp))

                Button(
                    onClick = {
                        val regRequest = RegisterRequest(
                            username = RegisterDataState.firstName,
                            password = RegisterDataState.password,
                            firstname = RegisterDataState.firstName,
                            lastname = RegisterDataState.lastName,
                            phone = phone,
                            email = RegisterDataState.email,
                            birthdate = if (selectedDateText == "วันเกิด") "2000-01-01" else selectedDateText,
                            gender = if (selectedGender.isEmpty()) "Other" else selectedGender,
                            bloodType = "",
                            rhFactor = "",
                            chronicAllergy = "",
                            drugAllergy = allergy,
                            citizenId = RegisterDataState.citizenId
                        )

                        scope.launch {
                            try {
                                // ยิง API ไปที่ Node.js
                                val response = RetrofitClient.instance.registerUser(regRequest)
                                if (response.isSuccessful && response.body()?.success == true) {
                                    // ถ้าสำเร็จ (DB อัปเดตแล้ว) ให้กลับไปหน้า Login
                                    navController.navigate("login") {
                                        popUpTo("register1") { inclusive = true }
                                    }
                                } else {
                                    // จัดการกรณีสมัครไม่สำเร็จ เช่น citizen_id ซ้ำ
                                    Log.e("Register", "Failed: ${response.errorBody()?.string()}")
                                }
                            } catch (e: Exception) {
                                Log.e("Register", "Error: ${e.message}")
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(60.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = NavyButton)
                ) { Text("สมัครสมาชิก", fontSize = 20.sp, fontWeight = FontWeight.Bold) }
            }
        }
    }
}

// ... (ส่วนบนคงเดิม) ...

@Composable
fun LoginScreen(navController: NavHostController) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorText by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    Column(modifier = Modifier.fillMaxSize().background(BlueGradient)) {
        Column(modifier = Modifier.fillMaxWidth().padding(start = 35.dp, top = 100.dp, bottom = 50.dp)) {
            Text("หมอ prompt", color = Color.White, fontSize = 48.sp, fontWeight = FontWeight.Bold)
            Text("เข้าสู่ระบบ", color = Color.White, fontSize = 24.sp)
        }
        Surface(modifier = Modifier.fillMaxSize(), color = Color.White, shape = RoundedCornerShape(topStart = 45.dp, topEnd = 45.dp)) {
            Column(modifier = Modifier.padding(horizontal = 35.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Spacer(Modifier.height(60.dp))
                LoginTextField(username, { username = it }, "เลขบัตรประชาชน / Username")
                Spacer(Modifier.height(20.dp))
                LoginTextField(password, { password = it }, "รหัสผ่าน", isPassword = true)
                if(errorText.isNotEmpty()) Text(errorText, color = Color.Red, fontSize = 12.sp)

                Spacer(Modifier.height(60.dp))

                Button(
                    onClick = {
                        scope.launch {
                            try {
                                val response = RetrofitClient.instance.loginUser(
                                    LoginRequest(citizenId = username, password = password)
                                )

                                if (response.isSuccessful && response.body()?.success == true) {
                                    val user = response.body()?.user

                                    // 1. บันทึกข้อมูลลง Session รวมถึง Role ที่ได้จาก Database
                                    UserSession.iduser = user?.iduser ?: 0
                                    UserSession.firstName = user?.firstname ?: ""
                                    UserSession.lastName = user?.lastname ?: ""
                                    UserSession.citizenId = user?.citizen_id ?: ""
                                    UserSession.role = user?.role ?: "Patient" // เก็บ Role

                                    // 2. แยกหน้าตาม Role
                                    val destination = when (UserSession.role) {
                                        "Doctor" -> "doctor_home"
                                        "Nurse" -> "nurse_home"
                                        else -> "home/${UserSession.firstName}/${UserSession.lastName}"
                                    }

                                    navController.navigate(destination) {
                                        popUpTo("login") { inclusive = true }
                                    }
                                } else {
                                    errorText = "เลขบัตรหรือรหัสผ่านไม่ถูกต้อง"
                                }
                            } catch (e: Exception) {
                                errorText = "เชื่อมต่อเซิร์ฟเวอร์ไม่ได้"
                                Log.e("LoginError", e.message ?: "Unknown Error")
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(60.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = NavyButton)
                ) {
                    Text("เข้าสู่ระบบ", fontSize = 22.sp, fontWeight = FontWeight.Bold)
                }
                Spacer(Modifier.height(20.dp))
                TextButton(onClick = { navController.navigate("register1") }) {
                    Text("ยังไม่มีบัญชี? สมัครสมาชิก", color = MainBlue)
                }
            }
        }
    }
}