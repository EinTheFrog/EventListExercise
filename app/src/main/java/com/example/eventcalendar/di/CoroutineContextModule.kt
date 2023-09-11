package com.example.eventcalendar.di

import com.example.eventcalendar.utils.annotations.IO
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

@Module
class CoroutineContextModule {
    @IO
    @Provides
    fun provideCoroutineContext(): CoroutineContext = Dispatchers.IO
}