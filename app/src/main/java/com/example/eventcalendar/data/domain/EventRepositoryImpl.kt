package com.example.eventcalendar.data.domain

import com.example.eventcalendar.data.model.EventType
import com.example.eventcalendar.data.model.domain.CityDomain
import com.example.eventcalendar.data.model.domain.EventDomain
import com.example.eventcalendar.data.network.EventApi
import com.example.eventcalendar.data.storage.EventDao
import com.example.eventcalendar.utils.annotations.IO
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
    @IO private val coroutineContext: CoroutineContext,
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