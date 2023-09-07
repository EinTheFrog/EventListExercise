package com.example.eventcalendar.domain

import com.example.eventcalendar.model.EventType
import com.example.eventcalendar.model.domain.CityDomain
import com.example.eventcalendar.model.domain.EventDomain

interface EventRepository {
    suspend fun getEvents(): Result<List<EventDomain>>

    suspend fun getEventById(eventId: Int): Result<EventDomain>

    suspend fun saveEvent(event: EventDomain): Result<Unit>

    suspend fun updateEvent(event: EventDomain): Result<Unit>

    suspend fun generateEventId(): Result<Int>

    suspend fun changeEventType(eventId: Int, eventType: EventType): Result<EventDomain>

    suspend fun deleteEventById(eventId: Int): Result<Unit>

    suspend fun getCitySuggestions(searchQuery: String): Result<List<CityDomain>>
}