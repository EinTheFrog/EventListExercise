package com.example.eventcalendar.data.model.domain

import com.example.eventcalendar.data.model.EventType
import java.util.Calendar

data class EventDomain(
    val id: Int,
    val title: String,
    val city: CityDomain,
    val address: String,
    val weather: WeatherDomain,
    val date: Calendar,
    val description: String,
    val eventType: EventType
)