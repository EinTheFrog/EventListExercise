package com.example.eventcalendar.di

import android.content.Context
import androidx.room.Room
import com.example.eventcalendar.data.storage.EventDatabase
import com.example.eventcalendar.utils.constants.DATABASE_NAME
import dagger.Module
import dagger.Provides

@Module
class RoomModule {
    @Provides
    fun provideDatabase(applicationContext: Context) = Room.databaseBuilder(
        applicationContext,
        EventDatabase::class.java,
        DATABASE_NAME
    ).fallbackToDestructiveMigration().build()

    @Provides
    fun provideEventDao(db: EventDatabase) = db.eventDao()

}