package com.example.myapplication100
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myapplication100.DataClass.Appointment_Examination.Examination
import com.example.myapplication100.DataClass.HospitalApiService

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import com.example.myapplication100.LoginRegis.RetrofitClient

@Composable
fun HistoryScreen(userId: Int) {
    var historyList by remember { mutableStateOf(listOf<Examination>()) }

    LaunchedEffect(Unit) {
        try {
            val response = RetrofitClient.instance.getMedicalHistory(userId)
            if (response.isSuccessful) {
                historyList = response.body() ?: emptyList()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    Column(modifier = Modifier.padding(8.dp)) {
        historyList.forEach { history ->
            HistoryCard(history)
            Spacer(Modifier.height(8.dp))
        }
    }
}