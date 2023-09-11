package com.example.eventcalendar.data.model.domain

import java.util.Calendar

data class WeatherDomain(
    val id: Int,
    val temp: Double,
    val icon: String,
    val date: Calendar
)