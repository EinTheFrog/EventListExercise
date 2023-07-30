package com.example.eventcalendar.data.storage

import androidx.room.TypeConverter
import com.example.eventcalendar.utils.extensions.allIndicesOf
import com.example.eventcalendar.utils.extensions.toShortString
import java.util.Calendar

class CalendarConverter {
    @TypeConverter
    fun longToCalendar(value: String?): Calendar? {
        return value?.let {
            val dashIndices = it.allIndicesOf('-')
            val yearStart = 0
            val yearEnd = dashIndices[0]
            val year = it.substring(yearStart, yearEnd).toInt()
            val monthStart = dashIndices[0] + 1
            val monthEnd = dashIndices[1]
            val month = it.substring(monthStart, monthEnd).toInt()
            val dateStart = dashIndices[1] + 1
            val dateEnd = it.length
            val date = it.substring(dateStart, dateEnd).toInt()
            val calendar = Calendar.getInstance()
            calendar.set(year, month, date)
            calendar
        }
    }

    @TypeConverter
    fun dateToCalendar(calendar: Calendar?): String? {
        return calendar?.toShortString()
    }
}