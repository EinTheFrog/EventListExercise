package com.example.eventcalendar.data.domain

import com.example.eventcalendar.data.model.EventType
import com.example.eventcalendar.data.model.domain.CityDomain
import com.example.eventcalendar.data.model.domain.EventDomain

interface EventRepository {
    suspend fun getEvents(): Result<List<EventDomain>>

    suspend fun getEventById(eventId: Int): Result<EventDomain>

    suspend fun saveEvent(event: EventDomain): Result<Unit>

    suspend fun deleteEventById(eventId: Int): Result<Unit>

    suspend fun getCitySuggestions(searchQuery: String): Result<List<CityDomain>>
}