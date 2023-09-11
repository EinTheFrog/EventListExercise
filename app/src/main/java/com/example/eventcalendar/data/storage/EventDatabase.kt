package com.example.eventcalendar.data.storage

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.eventcalendar.data.model.storage.EventStorage
import com.example.eventcalendar.data.model.storage.WeatherStorage

@Database(
    entities = [EventStorage::class, WeatherStorage::class],
    version = 2
)
@TypeConverters(CalendarConverter::class)
abstract class EventDatabase: RoomDatabase() {
    abstract fun eventDao(): EventDao
}