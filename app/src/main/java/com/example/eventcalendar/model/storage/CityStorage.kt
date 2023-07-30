package com.example.eventcalendar.model.storage

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = CityStorage.TABLE_NAME)
data class CityStorage(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "latitude") val latitude: Double,
    @ColumnInfo(name = "longitude") val longitude: Double,
    @ColumnInfo(name = "country") val country: String
) {
    companion object {
        const val TABLE_NAME = "city"
    }
}