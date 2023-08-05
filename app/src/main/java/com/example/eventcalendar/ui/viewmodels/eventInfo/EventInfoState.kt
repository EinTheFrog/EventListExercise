package com.example.eventcalendar.ui.viewmodels.eventInfo

import com.example.eventcalendar.model.domain.EventDomain

sealed class EventInfoState {
    object Loading: EventInfoState()

    data class Default(
        val event: EventDomain
    ): EventInfoState()

    data class Error(
        val exception: Throwable?
    ): EventInfoState()

}