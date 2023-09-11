package com.example.eventcalendar.data.model.network

import com.google.gson.annotations.SerializedName

data class CityNetwork(
    @SerializedName("name") val name: String,
    @SerializedName("lat") val latitude: Double,
    @SerializedName("lon") val longitude: Double,
    @SerializedName("country") val country: String
)