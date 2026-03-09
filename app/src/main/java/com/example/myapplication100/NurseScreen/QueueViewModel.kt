package com.example.myapplication100.NurseScreen

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.example.myapplication100.DataClass.Appointment_Examination.Appointment
import com.example.myapplication100.DataClass.Appointment_Examination.Appointmentnurse
import com.example.myapplication100.LoginRegis.RetrofitClient

class QueueViewModel : ViewModel() {

    var queueList by mutableStateOf<List<Appointmentnurse>>(emptyList())
        private set

    fun fetchQueue() {

        viewModelScope.launch {

            try {

                val result = RetrofitClient.instance.getTodayQueue()

                queueList = result

            } catch (e: Exception) {

                Log.e("QUEUE", "Error", e)

            }

        }

    }

}