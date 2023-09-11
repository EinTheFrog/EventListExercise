package com.example.eventcalendar.data.storage

import androidx.room.TypeConverter
import com.example.eventcalendar.utils.extensions.allIndicesOf
import com.example.eventcalendar.utils.extensions.toCalendar
import com.example.eventcalendar.utils.extensions.toShortString
import java.util.Calendar

class CalendarConverter {
    @TypeConverter
    fun stringToCalendar(value: String?): Calendar? {
        return value?.toCalendar()
    }

    @TypeConverter
    fun calendarToString(calendar: Calendar?): String? {
        return calendar?.toShortString()
    }
}