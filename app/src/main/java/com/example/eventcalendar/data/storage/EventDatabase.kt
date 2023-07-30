package com.example.eventcalendar.data.storage

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.eventcalendar.model.storage.CityStorage
import com.example.eventcalendar.model.storage.EventStorage
import com.example.eventcalendar.model.storage.WeatherStorage

@Database(
    entities = [EventStorage::class,  CityStorage::class, WeatherStorage::class],
    version = 1
)
@TypeConverters(CalendarConverter::class)
abstract class EventDatabase: RoomDatabase() {
    abstract fun eventDao(): EventDao
}