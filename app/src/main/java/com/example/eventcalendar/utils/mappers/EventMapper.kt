package com.example.eventcalendar.utils.mappers

import com.example.eventcalendar.data.storage.EventDao
import com.example.eventcalendar.data.model.domain.CityDomain
import com.example.eventcalendar.data.model.domain.EventDomain
import com.example.eventcalendar.data.model.storage.EventStorage
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EventMapper @Inject constructor(
    private val weatherMapper: WeatherMapper,
    private val eventDao: EventDao
) {
    fun domainToStorage(eventDomain: EventDomain): EventStorage {
        return EventStorage(
            id = eventDomain.id,
            title = eventDomain.title,
            cityName = eventDomain.city.name,
            latitude = eventDomain.city.latitude,
            longitude = eventDomain.city.longitude,
            country = eventDomain.city.country,
            address = eventDomain.address,
            weatherId = eventDomain.weather.id,
            date = eventDomain.date,
            description = eventDomain.description,
            eventType = eventDomain.eventType
        )
    }

    suspend fun storageToDomain(eventStorage: EventStorage): EventDomain {
        return EventDomain(
            id = eventStorage.id,
            title = eventStorage.title,
            city = CityDomain(
                name = eventStorage.cityName,
                longitude = eventStorage.longitude,
                latitude = eventStorage.latitude,
                country = eventStorage.country
            ),
            address = eventStorage.address,
            weather = weatherMapper.storageToDomain(eventDao.getWeatherById(eventStorage.weatherId)),
            date = eventStorage.date,
            description = eventStorage.description,
            eventType = eventStorage.eventType
        )
    }
}