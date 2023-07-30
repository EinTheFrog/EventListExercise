package com.example.eventcalendar.data.network

import com.example.eventcalendar.model.network.CityNetwork
import com.example.eventcalendar.model.network.WeatherNetwork
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EventApiMock @Inject constructor(): EventApi {
    override fun getGeoPosition(cityName: String, limit: Int, apiKey: String): CityNetwork {
        return CityNetwork(
            name = "Dubai",
            latitude = 25.16,
            longitude = 55.17,
            country = "UAE"
        )
    }

    override fun getWeather(
        latitude: Double,
        longitude: Double,
        timeSplitCount: Int,
        apiKey: String,
        units: String
    ): WeatherNetwork {
        return WeatherNetwork(
            temp = 40.1,
            icon = "",
            date = "2023-07-25"
        )
    }
}