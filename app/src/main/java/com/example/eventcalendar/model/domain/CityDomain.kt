package com.example.eventcalendar.model.domain

data class CityDomain(
    val id: Int,
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val country: String
)