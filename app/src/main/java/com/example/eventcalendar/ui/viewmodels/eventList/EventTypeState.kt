package com.example.eventcalendar.ui.viewmodels.eventList

import com.example.eventcalendar.model.EventType
import com.example.eventcalendar.model.domain.EventDomain

sealed class EventTypeState {

    data class Default(
        val eventList: List<EventDomain>
    ): EventTypeState()

    data class Error(
        val exception: Throwable?
    ): EventTypeState()

    object Loading: EventTypeState()

}