package com.example.eventcalendar.utils.mappers

import com.example.eventcalendar.model.domain.WeatherDomain
import com.example.eventcalendar.model.storage.WeatherStorage
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeatherMapper @Inject constructor()  {
    fun domainToStorage(weatherDomain: WeatherDomain): WeatherStorage {
        return WeatherStorage(
            id = weatherDomain.id,
            temp = weatherDomain.temp,
            icon = weatherDomain.icon,
            date = weatherDomain.date
        )
    }

    fun storageToDomain(weatherStorage: WeatherStorage): WeatherDomain {
        return WeatherDomain(
            id = weatherStorage.id,
            temp = weatherStorage.temp,
            icon = weatherStorage.icon,
            date = weatherStorage.date
        )
    }
}