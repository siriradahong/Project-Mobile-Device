package com.example.myapplication100

import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun HeaderTitle(text: String) {
    Text(
        text = text,
        fontSize = 22.sp,
        fontWeight = FontWeight.Bold,
        color = Color.White,
        modifier = Modifier.padding(20.dp)
    )
}

@Composable
fun ProfileScreen() {
    Column(
        Modifier
            .fillMaxSize()
            .background(Color(0xFF21539D))
    ) {
        Box(
            Modifier
                .fillMaxWidth()
                .padding(top = 50.dp, bottom = 20.dp),
            contentAlignment = Alignment.Center
        ) {
            Text("ข้อมูลส่วนตัว", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
        }

        Column(
            Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp))
                .background(Color.White)
                .padding(20.dp)
                .verticalScroll(rememberScrollState())
        ) {
            InfoCard()
            Spacer(Modifier.height(20.dp))
            HistoryCard()
            Spacer(Modifier.height(20.dp))
            MedicineCard()
        }
    }
}