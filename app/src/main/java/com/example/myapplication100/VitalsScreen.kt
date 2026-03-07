package com.example.myapplication100

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController


//modifier: Modifier = Modifier
@Composable
fun VitalsScreen(modifier: Modifier = Modifier,
                 nav: NavHostController?,
                 appointmentId: Int,
                 queueNumber: String,
                 patientName: String

) {
    val viewModel: VitalsViewModel = viewModel()
    var sys by remember { mutableStateOf("") }
    var dia by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    var height by remember { mutableStateOf("") }
    var temp by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }
    var heartRate by remember { mutableStateOf("") }

    val context = LocalContext.current

    Column(
        Modifier
            .fillMaxSize()
            .background(Color(0xFF21539D))
    ) {

        // ===== Header Section =====
        Box(
            Modifier
                .fillMaxWidth()
                .height(120.dp)
                .statusBarsPadding()
                .padding(top = 12.dp),
            contentAlignment = Alignment.Center
        ) {


            Text(
                "แบบฟอร์มคัดกรอง",
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
        }

        // ===== Content Container (Pattern เดียวกับ HomeScreen) =====
        Column(
            Modifier
                .fillMaxSize()
                .background(
                    Color(0xFFF4F4F4),
                    RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp)
                )
                .padding(20.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "$patientName ($queueNumber)",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(10.dp))

            // ความดันตัวบน
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ){
                Column(modifier = Modifier.weight(1f)) {
                    Text("ความดันโลหิต (ตัวบน)")
                    Spacer(Modifier.height(8.dp))
                    OutlinedTextField(
                        value = sys,
                        onValueChange = { sys = it },
                        placeholder = { Text("กรอกความดัน") },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )}

                Column(modifier = Modifier.weight(1f)) {

                    // ความดันตัวล่าง
                    Text("ความดันโลหิต (ตัวล่าง)")
                    Spacer(Modifier.height(8.dp))
                    OutlinedTextField(
                        value = dia,
                        onValueChange = { dia = it },
                        placeholder = { Text("กรอกความดัน") },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
                    Text("อัตราการเต้นของหัวใจ (bpm)")
                    OutlinedTextField(
                        value = heartRate,
                        onValueChange = { heartRate = it },
                        placeholder = { Text("กรอกอัตราการเต้นของหัวใจ") },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    // น้ำหนัก + อุณหภูมิ
                    // ===== ส่วนที่แก้ไข: น้ำหนัก + ส่วนสูง ให้แบ่งกันคนละครึ่งใน Row =====
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // ฝั่งน้ำหนัก
                        Column(modifier = Modifier.weight(1f)) {
                            Text("น้ำหนัก (kg)")
                            Spacer(Modifier.height(8.dp))
                            OutlinedTextField(
                                value = weight,
                                onValueChange = { weight = it },
                                placeholder = { Text("กรอกน้ำหนัก") },
                                trailingIcon = { Text("kg") },
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Decimal
                                ),
                                modifier = Modifier.fillMaxWidth() // ขยายให้เต็มในสัดส่วน weight(1f)
                            )
                        }

                        // ฝั่งส่วนสูง
                        Column(modifier = Modifier.weight(1f)) {
                            Text("ส่วนสูง (cm)")
                            Spacer(Modifier.height(8.dp))
                            OutlinedTextField(
                                value = height,
                                onValueChange = { height = it },
                                placeholder = { Text("กรอกส่วนสูง") },
                                trailingIcon = { Text("cm") },
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Decimal
                                ),
                                modifier = Modifier.fillMaxWidth() // ขยายให้เต็มในสัดส่วน weight(1f)
                            )
                        }
                    }



                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ){
                        Column(modifier = Modifier.weight(1f)) {
                            Text("อุณหภูมิ")
                            Spacer(Modifier.height(8.dp))
                            OutlinedTextField(
                                value = temp,
                                onValueChange = { temp = it },
                                placeholder = { Text("เช่น 36.5") },
                                trailingIcon = { Text("°C") },
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Decimal
                                ),
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }



                    // โน้ตถึงหมอ
                    Text("โน้ตถึงหมอ")
                    OutlinedTextField(
                        value = note,
                        onValueChange = { note = it },
                        placeholder = { Text("เช่น คนไข้มีประวัติแพ้ยา") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp)
                    )

                    Spacer(Modifier.height(8.dp))

                    Button(
                        onClick = {

                            val vitals = VitalsRequest(
                                appointment_id = appointmentId,
                                blood_pressure_sys = sys.toIntOrNull() ?: 0,
                                blood_pressure_dia = dia.toIntOrNull() ?: 0,
                                heart_rate = heartRate.toIntOrNull() ?: 0,
                                weight = weight.toDoubleOrNull() ?: 0.0,
                                height = height.toDoubleOrNull() ?: 0.0,
                                temperature = temp.toDoubleOrNull() ?: 0.0,
                                note = note
                            )

                            viewModel.saveVitals(context, vitals) {
                                nav?.navigate("queue") {
                                    popUpTo("queue") { inclusive = true }
                                }
                            }
                            viewModel.saveVitals(context, vitals) {

                                nav?.navigate("queue") {
                                    popUpTo("queue") { inclusive = true }
                                }

                            }

                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(24.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF3F7F8B)
                        )
                    ) {
                        Text("ส่งข้อมูล")
                    }
                }
            }
    }
