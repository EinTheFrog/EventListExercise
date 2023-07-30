package com.example.eventcalendar.domain

import com.example.eventcalendar.model.domain.EventDomain

interface EventRepository {
    suspend fun getEvents(): Result<List<EventDomain>>
    suspend fun saveEvent(event: EventDomain): Result<Boolean>
}