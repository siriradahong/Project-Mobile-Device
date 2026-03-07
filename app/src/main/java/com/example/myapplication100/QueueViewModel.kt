package com.example.myapplication100

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

class QueueViewModel : ViewModel() {

    var queueList by mutableStateOf<List<Appointment>>(emptyList())
        private set

    fun fetchQueue() {

        viewModelScope.launch {

            try {

                val result = RetrofitClient.api.getTodayQueue()

                queueList = result

            } catch (e: Exception) {

                Log.e("QUEUE", "Error", e)

            }

        }

    }

}