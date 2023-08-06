package com.example.eventcalendar.ui.viewmodels.eventList

import com.example.eventcalendar.model.EventType
import com.example.eventcalendar.model.domain.EventDomain

sealed class EventListState {

    data class Default(
        val newEventType: EventType
    ): EventListState()

    data class NewEvent(
        val newEventId: Int,
        val newEventType: EventType
    ): EventListState()

    data class Error(
        val exception: Throwable?
    ): EventListState()

    object Loading: EventListState()

}