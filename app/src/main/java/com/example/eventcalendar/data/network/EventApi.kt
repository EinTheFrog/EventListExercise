package com.example.eventcalendar.data.network

import com.example.eventcalendar.data.model.network.CityNetwork
import com.example.eventcalendar.data.model.network.WeatherNetwork
import com.example.eventcalendar.utils.constants.API_KEY
import retrofit2.http.GET
import retrofit2.http.Query


interface EventApi {
    @GET("geo/1.0/direct?")
    suspend fun getGeoPosition(
        @Query("q") cityName: String,
        @Query("limit") limit: Int,
        @Query("appid") apiKey: String = API_KEY
    ): List<CityNetwork>

    @GET("data/2.5/forecast?")
    suspend fun getWeather(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("cnt") timeSplitCount: Int,
        @Query("appid") apiKey: String,
        @Query("units") units: String
    ): WeatherNetwork
}