package com.example.eventcalendar.domain

import com.example.eventcalendar.data.network.EventApi
import com.example.eventcalendar.model.domain.EventDomain
import com.example.eventcalendar.data.storage.EventDao
import com.example.eventcalendar.model.EventType
import com.example.eventcalendar.model.domain.CityDomain
import com.example.eventcalendar.utils.constants.API_KEY
import com.example.eventcalendar.utils.constants.CITY_SUGGESTIONS_SIZE
import com.example.eventcalendar.utils.mappers.CityMapper
import com.example.eventcalendar.utils.mappers.EventMapper
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.CoroutineContext

@Singleton
class EventRepositoryImpl @Inject constructor(
    private val coroutineContext: CoroutineContext,
    private val eventApi: EventApi,
    private val eventDao: EventDao,
    private val eventMapper: EventMapper,
    private val cityMapper: CityMapper
): EventRepository {

    override suspend fun getEvents(): Result<List<EventDomain>> = withContext(coroutineContext) {
        try {
            val storageEvents = eventDao.getAllEvents()
            val domainEvents = storageEvents.map { eventMapper.storageToDomain(it) }
            return@withContext Result.success(domainEvents)
        } catch (e: Throwable) {
            return@withContext Result.failure(e)
        }
    }

    override suspend fun getEventById(eventId: Int): Result<EventDomain> = withContext(coroutineContext) {
        try {
            val storageEvent = eventDao.getEventById(eventId)
            val domainEvent = eventMapper.storageToDomain(storageEvent)
            return@withContext Result.success(domainEvent)
        } catch (e: Throwable) {
            return@withContext Result.failure(e)
        }
    }

    override suspend fun saveEvent(event: EventDomain): Result<Unit> = withContext(coroutineContext) {
        try {
            eventDao.insertEvent(eventMapper.domainToStorage(event))
            return@withContext Result.success(Unit)
        } catch (e: Throwable) {
            return@withContext Result.failure(e)
        }
    }

    override suspend fun updateEvent(event: EventDomain): Result<Unit> = withContext(coroutineContext) {
        try {
            val oldEvent = eventDao.getEventById(event.id)
            eventDao.deleteEvent(oldEvent)
            val newEvent = eventMapper.domainToStorage(event)
            eventDao.insertEvent(newEvent)
            return@withContext Result.success(Unit)
        } catch (e: Throwable) {
            return@withContext Result.failure(e)
        }
    }

    override suspend fun generateEventId(): Result<Int> = withContext(coroutineContext) {
        try {
            var maxId = 0
            val storageEvents = eventDao.getAllEvents()
            for (storageEvent in storageEvents) {
                if (storageEvent.id > maxId) {
                    maxId = storageEvent.id
                }
            }
            val result = maxId + 1
            return@withContext Result.success(result)
        } catch (e: Throwable) {
            return@withContext Result.failure(e)
        }
    }

    override suspend fun changeEventType(eventId: Int, eventType: EventType): Result<EventDomain> = withContext(coroutineContext) {
        try {
            val oldEvent = eventDao.getEventById(eventId)
            eventDao.deleteEvent(oldEvent)
            val newEvent = oldEvent.copy(eventType = eventType)
            eventDao.insertEvent(newEvent)
            val result = eventMapper.storageToDomain(newEvent)
            return@withContext Result.success(result)
        } catch (e: Throwable) {
            return@withContext Result.failure(e)
        }
    }

    override suspend fun deleteEventById(eventId: Int): Result<Unit> = withContext(coroutineContext) {
        try {
            val oldEvent = eventDao.getEventById(eventId)
            eventDao.deleteEvent(oldEvent)
            return@withContext Result.success(Unit)
        } catch (e: Throwable) {
            return@withContext Result.failure(e)
        }
    }

    override suspend fun getCitySuggestions(searchQuery: String): Result<List<CityDomain>> = withContext(coroutineContext) {
        try {
            val cityNetworkList = eventApi.getGeoPosition(
                cityName = searchQuery,
                limit = CITY_SUGGESTIONS_SIZE,
                apiKey = API_KEY
            )
            val cityDomainList = cityNetworkList.map { cityMapper.networkToDomain(it) }
            return@withContext Result.success(cityDomainList)
        } catch (e: Throwable) {
            return@withContext Result.failure(e)
        }
    }
}