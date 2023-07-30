package com.example.eventcalendar.di

import com.example.eventcalendar.domain.EventRepository
import com.example.eventcalendar.domain.EventRepositoryImpl
import com.example.eventcalendar.domain.EventRepositoryMock
import dagger.Binds
import dagger.Module

@Module
interface RepositoryModule {
    @Binds
    fun provideEventRepository(eventRepository: EventRepositoryMock): EventRepository
}