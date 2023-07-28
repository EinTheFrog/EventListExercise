package com.example.eventcalendar.model

import androidx.annotation.StringRes
import com.example.eventcalendar.R

enum class EventType(@StringRes val titleResource: Int) {
    COMING(R.string.coming_event_title),
    VISITED(R.string.visited_event_title),
    MISSED(R.string.missed_event_title)
}