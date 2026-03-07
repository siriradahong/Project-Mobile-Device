package com.example.myapplication100


import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.Response

interface ApiHospitalService {

    @GET("appointments/today")
    suspend fun getTodayQueue(): List<Appointment>

    @GET("appointments/summary")
    suspend fun getQueueSummary(): QueueSummary

    @POST("vitals")
    suspend fun saveVitals(
        @Body vitals: VitalsRequest
    ): Response<Unit>


}