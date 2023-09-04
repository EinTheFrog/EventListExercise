package com.example.eventcalendar.domain

import com.example.eventcalendar.data.network.EventApi
import com.example.eventcalendar.model.domain.EventDomain
import com.example.eventcalendar.data.storage.EventDao
import com.example.eventcalendar.model.EventType
import com.example.eventcalendar.model.domain.CityDomain
import com.example.eventcalendar.utils.mappers.EventMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EventRepositoryImpl @Inject constructor(
    private val eventApi: EventApi,
    private val eventDao: EventDao,
    private val eventMapper: EventMapper
): EventRepository {

    override suspend fun getEvents(): Result<List<EventDomain>> = withContext(Dispatchers.IO) {
        try {
            val storageEvents = eventDao.getAllEvents()
            val domainEvents = storageEvents.map { eventMapper.storageToDomain(it) }
            return@withContext Result.success(domainEvents)
        } catch (e: Throwable) {
            return@withContext Result.failure(e)
        }
    }

    override suspend fun getEventById(eventId: Int): Result<EventDomain> {
        TODO("Not yet implemented")
    }

    override suspend fun saveEvent(event: EventDomain): Result<Boolean> = withContext(Dispatchers.IO) {
        try {
            eventDao.insertEvent(eventMapper.domainToStorage(event))
            return@withContext Result.success(true)
        } catch (e: Throwable) {
            return@withContext Result.failure(e)
        }
    }

    override suspend fun updateEvent(event: EventDomain): Result<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun generateEventId(): Result<Int> {
        TODO("Not yet implemented")
    }

    override suspend fun changeEventType(eventId: Int, eventType: EventType): Result<EventDomain> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteEvent(eventId: Int): Result<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun getCitySuggestions(searchQuery: String): Result<List<CityDomain>> {
        TODO("Not yet implemented")
    }
}