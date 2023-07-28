package com.example.eventcalendar.data.network

import com.example.eventcalendar.model.network.CityNetwork
import com.example.eventcalendar.model.network.WeatherNetwork

interface EventApi {

    fun getGeoPosition(
        cityName: String,
        limit: Int,
        apiKey: String
    ): CityNetwork

    fun getWeather(
        latitude: Double,
        longitude: Double,
        timeSplitCount: Int = 40,
        apiKey: String,
        units: String = "metric"
    ): WeatherNetwork

}