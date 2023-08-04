package com.example.eventcalendar.domain

import com.example.eventcalendar.data.network.EventApi
import com.example.eventcalendar.model.domain.EventDomain
import com.example.eventcalendar.data.storage.EventDao
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

    override suspend fun saveEvent(event: EventDomain): Result<Boolean> = withContext(Dispatchers.IO) {
        try {
            eventDao.insertEvent(eventMapper.domainToStorage(event))
            return@withContext Result.success(true)
        } catch (e: Throwable) {
            return@withContext Result.failure(e)
        }
    }

    override suspend fun generateEventId(): Result<Int> {
        TODO("Not yet implemented")
    }

}