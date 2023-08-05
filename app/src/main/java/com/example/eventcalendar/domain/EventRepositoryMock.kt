package com.example.eventcalendar.domain

import android.content.res.Resources.NotFoundException
import com.example.eventcalendar.model.EventType
import com.example.eventcalendar.model.domain.CityDomain
import com.example.eventcalendar.model.domain.EventDomain
import com.example.eventcalendar.model.domain.WeatherDomain
import java.util.Calendar
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EventRepositoryMock @Inject constructor(): EventRepository {
    private val eventList = mutableListOf(
        EventDomain(0, "Event 0", CityDomain(0, "Dubai", 0.0, 0.0, "UAE"), WeatherDomain(0, 43.0, "", Calendar.getInstance()), Calendar.getInstance(), "", EventType.VISITED),
        EventDomain(1, "Event 1", CityDomain(1, "Saint Petersburg", 0.0, 0.0, "Russia"), WeatherDomain(0, 23.0, "", Calendar.getInstance()), Calendar.getInstance(), "", EventType.VISITED),
        EventDomain(2, "Event 2", CityDomain(2, "Tbilisi", 0.0, 0.0, "Georgia"), WeatherDomain(0, 31.5, "", Calendar.getInstance()), Calendar.getInstance(), "", EventType.COMING),
        EventDomain(3, "Event 3", CityDomain(3, "Erevan", 0.0, 0.0, "Armenia"), WeatherDomain(0, 34.0, "", Calendar.getInstance()), Calendar.getInstance(), "", EventType.COMING),
        EventDomain(4, "Event 4", CityDomain(4, "Moscow", 0.0, 0.0, "Russia"), WeatherDomain(0, 28.0, "", Calendar.getInstance()), Calendar.getInstance(), "", EventType.COMING),
        EventDomain(0, "Event 5", CityDomain(5, "Tel-Aviv", 0.0, 0.0, "Israel"), WeatherDomain(0, 39.0, "", Calendar.getInstance()), Calendar.getInstance(), "", EventType.MISSED)
    )

    override suspend fun getEvents(): Result<List<EventDomain>> {
        return Result.success(eventList)
    }

    override suspend fun getEventById(eventId: Int): Result<EventDomain> {
        for (event in eventList) {
            if (event.id == eventId) return Result.success(event)
        }
        return Result.failure(NotFoundException("Event not found"))
    }

    override suspend fun saveEvent(event: EventDomain): Result<Boolean> {
        eventList.add(event)
        return Result.success(true)
    }

    override suspend fun generateEventId(): Result<Int> {
        val lastId = eventList.last().id
        return Result.success(lastId + 1)
    }
}