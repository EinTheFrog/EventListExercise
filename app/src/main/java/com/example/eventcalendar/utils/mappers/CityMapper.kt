package com.example.eventcalendar.utils.mappers

import com.example.eventcalendar.model.domain.CityDomain
import com.example.eventcalendar.model.storage.CityStorage
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CityMapper @Inject constructor() {
    fun domainToStorage(cityDomain: CityDomain): CityStorage {
        return CityStorage(
            id = cityDomain.id,
            name = cityDomain.name,
            latitude = cityDomain.latitude,
            longitude = cityDomain.longitude,
            country = cityDomain.country
        )
    }

    fun storageToDomain(cityStorage: CityStorage): CityDomain {
        return CityDomain(
            id = cityStorage.id,
            name = cityStorage.name,
            latitude = cityStorage.latitude,
            longitude = cityStorage.longitude,
            country = cityStorage.country
        )
    }
}