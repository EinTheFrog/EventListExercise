package com.example.eventcalendar.data.storage

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.eventcalendar.model.storage.CityStorage
import com.example.eventcalendar.model.storage.EventStorage
import com.example.eventcalendar.model.storage.WeatherStorage

@Dao
interface EventDao {
    @Query("SELECT * FROM ${EventStorage.TABLE_NAME}")
    fun getAllEvents(): List<EventStorage>

    @Query("SELECT * from ${CityStorage.TABLE_NAME} WHERE id == :cityId")
    fun getCityById(cityId: Int): CityStorage

    @Query("SELECT * from ${WeatherStorage.TABLE_NAME} WHERE id == :weatherId")
    fun getWeatherById(weatherId: Int): WeatherStorage

    @Insert
    fun insertEvent(eventStorage: EventStorage)
}