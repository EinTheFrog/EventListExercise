package com.example.eventcalendar.utils.mappers

import com.example.eventcalendar.data.model.domain.CityDomain
import com.example.eventcalendar.data.model.network.CityNetwork
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CityMapper @Inject constructor() {
    fun networkToDomain(cityNetwork: CityNetwork): CityDomain {
        return CityDomain(
            name = cityNetwork.name,
            latitude = cityNetwork.latitude,
            longitude = cityNetwork.longitude,
            country = cityNetwork.country
        )
    }
}