package com.example.eventcalendar.utils.mappers

import com.example.eventcalendar.data.storage.EventDao
import com.example.eventcalendar.model.domain.EventDomain
import com.example.eventcalendar.model.storage.EventStorage
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EventMapper @Inject constructor(
    private val cityMapper: CityMapper,
    private val weatherMapper: WeatherMapper,
    private val eventDao: EventDao
) {
    fun domainToStorage(eventDomain: EventDomain): EventStorage {
        return EventStorage(
            id = eventDomain.id,
            title = eventDomain.title,
            cityId = eventDomain.city.id,
            weatherId = eventDomain.weather.id,
            date = eventDomain.date,
            eventType = eventDomain.eventType
        )
    }

    fun storageToDomain(eventStorage: EventStorage): EventDomain {
        return EventDomain(
            id = eventStorage.id,
            title = eventStorage.title,
            city = cityMapper.storageToDomain(eventDao.getCityById(eventStorage.cityId)),
            weather = weatherMapper.storageToDomain(eventDao.getWeatherById(eventStorage.weatherId)),
            date = eventStorage.date,
            eventType = eventStorage.eventType
        )
    }
}