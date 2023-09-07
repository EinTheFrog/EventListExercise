package com.example.eventcalendar.di

import com.example.eventcalendar.data.network.EventApi
import com.example.eventcalendar.data.storage.EventDao
import com.example.eventcalendar.domain.EventRepository
import com.example.eventcalendar.domain.EventRepositoryImpl
import com.example.eventcalendar.utils.mappers.CityMapper
import com.example.eventcalendar.utils.mappers.EventMapper
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.Dispatchers

@Module
class RepositoryModule {
    @Provides
    fun provideEventRepository(
        eventApi: EventApi,
        eventDao: EventDao,
        eventMapper: EventMapper,
        cityMapper: CityMapper
    ): EventRepository = EventRepositoryImpl(
        coroutineContext = Dispatchers.IO,
        eventApi = eventApi,
        eventDao = eventDao,
        eventMapper = eventMapper,
        cityMapper = cityMapper
    )
}