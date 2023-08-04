package com.example.eventcalendar.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.eventcalendar.R
import java.lang.IllegalArgumentException

enum class EventType(
    val id: Int,
    @StringRes val titleResource: Int,
    @DrawableRes val iconResource: Int
    ) {
    COMING(0, R.string.coming_event_title, R.drawable.baseline_access_alarm_24),
    VISITED(1, R.string.visited_event_title, R.drawable.baseline_check_24),
    MISSED(2, R.string.missed_event_title, R.drawable.baseline_close_24);

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