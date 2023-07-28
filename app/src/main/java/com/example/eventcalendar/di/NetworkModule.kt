package com.example.eventcalendar.di

import com.example.eventcalendar.data.network.EventApi
import com.example.eventcalendar.data.network.EventApiMock
import dagger.Binds
import dagger.Module

@Module
interface NetworkModule {
    @Binds
    fun bindsEventApi(eventApi: EventApiMock): EventApi
}