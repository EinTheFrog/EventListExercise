package com.example.eventcalendar.ui.viewmodels

import com.example.eventcalendar.model.domain.EventDomain

sealed class EventListState {

    data class Default(
        val isLoading: Boolean,
        val eventList: List<EventDomain>
    ): EventListState()

    data class Error(
        val exception: Throwable?
    ): EventListState()

}