package com.example.eventcalendar.model.domain

import java.util.Calendar

data class WeatherDomain(
    val temp: Double,
    val icon: String,
    val date: Calendar
)