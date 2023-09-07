package com.example.eventcalendar.domain

import android.content.res.Resources.NotFoundException
import com.example.eventcalendar.model.EventType
import com.example.eventcalendar.model.domain.CityDomain
import com.example.eventcalendar.model.domain.EventDomain
import com.example.eventcalendar.model.domain.WeatherDomain
import kotlinx.coroutines.withContext
import java.util.Calendar
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.CoroutineContext

@Singleton
class EventRepositoryMock @Inject constructor(
    private val coroutineContext: CoroutineContext
): EventRepository {
    private val cityList = listOf(
        CityDomain("Dubai", 0.0, 0.0, "UAE"),
        CityDomain( "Saint Petersburg", 0.0, 0.0, "Russia"),
        CityDomain( "Tbilisi", 0.0, 0.0, "Georgia"),
        CityDomain( "Yerevan", 0.0, 0.0, "Armenia"),
        CityDomain( "Moscow", 0.0, 0.0, "Russia"),
        CityDomain("Tel-Aviv", 0.0, 0.0, "Israel")
    )
    private val eventList = mutableListOf(
        EventDomain(0, "Event 0", cityList[0], "", WeatherDomain(0, 43.0, "", Calendar.getInstance()), Calendar.getInstance(), "", EventType.VISITED),
        EventDomain(1, "Event 1", cityList[1], "", WeatherDomain(0, 23.0, "", Calendar.getInstance()), Calendar.getInstance(), "", EventType.VISITED),
        EventDomain(2, "Event 2", cityList[2], "", WeatherDomain(0, 31.5, "", Calendar.getInstance()), Calendar.getInstance(), "", EventType.COMING),
        EventDomain(3, "Event 3", cityList[3], "", WeatherDomain(0, 34.0, "", Calendar.getInstance()), Calendar.getInstance(), "", EventType.COMING),
        EventDomain(4, "Event 4", cityList[4], "", WeatherDomain(0, 28.0, "", Calendar.getInstance()), Calendar.getInstance(), "", EventType.COMING),
        EventDomain(5, "Event 5", cityList[5], "", WeatherDomain(0, 39.0, "", Calendar.getInstance()), Calendar.getInstance(), "", EventType.MISSED)
    )

    override suspend fun getEvents() = withContext(coroutineContext) {
        return@withContext Result.success(eventList)
    }

    override suspend fun getEventById(eventId: Int) = withContext(coroutineContext) {
        for (event in eventList) {
            if (event.id == eventId) return@withContext Result.success(event)
        }
        return@withContext Result.failure(NotFoundException("Event not found"))
    }

    override suspend fun saveEvent(event: EventDomain) = withContext(coroutineContext) {
        eventList.add(event)
        return@withContext Result.success(Unit)
    }

    override suspend fun updateEvent(event: EventDomain) = withContext(coroutineContext) {
        for (i in eventList.indices) {
            val oldEvent = eventList[i]
            if (oldEvent.id == event.id) {
                eventList[i] = event
                return@withContext Result.success(Unit)
            }
        }
        return@withContext Result.failure(NotFoundException("Event not found"))
    }

    override suspend fun generateEventId() = withContext(coroutineContext) {
        val lastId = if (eventList.isEmpty()) 0 else eventList.last().id
        return@withContext Result.success(lastId + 1)
    }

    override suspend fun changeEventType(eventId: Int, eventType: EventType) = withContext(coroutineContext) {
        for (i in eventList.indices) {
            val event = eventList[i]
            if (event.id == eventId) {
                val newEvent = event.copy(eventType = eventType)
                eventList[i] = newEvent
                return@withContext Result.success(newEvent)
            }
        }
        return@withContext Result.failure(NotFoundException("Event not found"))
    }

    override suspend fun deleteEventById(eventId: Int) = withContext(coroutineContext) {
        for (i in eventList.indices) {
            val event = eventList[i]
            if (event.id == eventId) {
                eventList.removeAt(i)
                return@withContext Result.success(Unit)
            }
        }
        return@withContext Result.failure(NotFoundException("Event not found"))
    }

    override suspend fun getCitySuggestions(searchQuery: String) = withContext(coroutineContext) {
        return@withContext Result.success(
            cityList.filter { it.name.contains(searchQuery, false) }
        )
    }
}