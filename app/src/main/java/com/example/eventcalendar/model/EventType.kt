package com.example.eventcalendar.model

import androidx.annotation.StringRes
import com.example.eventcalendar.R
import java.lang.IllegalArgumentException

enum class EventType(val id: Int, @StringRes val titleResource: Int) {
    COMING(0, R.string.coming_event_title),
    VISITED(1, R.string.visited_event_title),
    MISSED(2, R.string.missed_event_title);

    companion object {
        fun getEventTypeById(id: Int): EventType {
            return when(id) {
                0 -> COMING
                1 -> VISITED
                2 -> MISSED
                else -> throw IllegalArgumentException("Doesn't have event type with such id")
            }
        }
    }
}