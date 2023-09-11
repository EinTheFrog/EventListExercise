package com.example.eventcalendar.di

import com.example.eventcalendar.data.network.EventApi
import com.example.eventcalendar.utils.constants.BASE_URL
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

@Module
class NetworkModule {
    @Provides
    fun provideGsonBuilder(): Gson = GsonBuilder().create()
    @Provides
    fun provideConverterFactory(gson: Gson): GsonConverterFactory = GsonConverterFactory.create(gson)

    @Provides
    fun provideRetrofit(converterFactory: GsonConverterFactory): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(converterFactory)
        .build()

    @Provides
    fun provideEventApi(retrofit: Retrofit): EventApi = retrofit.create()
}