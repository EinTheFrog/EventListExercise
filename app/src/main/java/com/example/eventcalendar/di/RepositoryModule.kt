package com.example.eventcalendar.di

import com.example.eventcalendar.data.domain.EventRepository
import com.example.eventcalendar.data.domain.EventRepositoryImpl
import dagger.Binds
import dagger.Module

@Module
interface RepositoryModule {
    @Binds
    fun bindEventRepository(eventRepository: EventRepositoryImpl): EventRepository
}