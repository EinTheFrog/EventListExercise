package com.example.eventcalendar.data.model.storage

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Calendar

@Entity(tableName = WeatherStorage.TABLE_NAME)
data class WeatherStorage(
    @PrimaryKey val id: Int,
    @ColumnInfo val temp: Double,
    @ColumnInfo val icon: String,
    @ColumnInfo val date: Calendar
) {
    companion object {
        const val TABLE_NAME = "weather"
    }
}