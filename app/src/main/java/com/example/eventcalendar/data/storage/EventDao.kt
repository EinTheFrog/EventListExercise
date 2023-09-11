package com.example.eventcalendar.data.storage

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.eventcalendar.data.model.storage.EventStorage
import com.example.eventcalendar.data.model.storage.WeatherStorage
import com.google.android.material.circularreveal.CircularRevealHelper.Strategy

@Dao
interface EventDao {
    @Query("SELECT * FROM ${EventStorage.TABLE_NAME}")
    suspend fun getAllEvents(): List<EventStorage>

    @Query("SELECT * FROM ${EventStorage.TABLE_NAME} WHERE id == :eventId")
    suspend fun getEventById(eventId: Int): EventStorage

    @Query("SELECT * from ${WeatherStorage.TABLE_NAME} WHERE id == :weatherId")
    suspend fun getWeatherById(weatherId: Int): WeatherStorage

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvent(eventStorage: EventStorage)

    @Delete
    suspend fun deleteEvent(eventStorage: EventStorage)
}