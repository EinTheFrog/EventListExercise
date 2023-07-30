package com.example.eventcalendar.model.storage

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.eventcalendar.model.EventType
import java.util.Calendar

@Entity(tableName = EventStorage.TABLE_NAME)
data class EventStorage(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "city_id") val cityId: Int,
    @ColumnInfo(name = "weather_id") val weatherId: Int,
    @ColumnInfo(name = "date") val date: Calendar,
    @ColumnInfo(name = "event_type") val eventType: EventType
) {
    companion object {
        const val TABLE_NAME = "event"
    }
}