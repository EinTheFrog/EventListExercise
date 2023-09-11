package com.example.eventcalendar.ui.viewmodels.createEvent

import com.example.eventcalendar.data.model.EventType
import com.example.eventcalendar.data.model.domain.CityDomain
import java.util.Calendar

sealed class CreateEventIntent {
    data class SelectDate(val date: Calendar): CreateEventIntent()

    data class GetCitySuggestions(val citySearchQuery: String): CreateEventIntent()

    data class SelectCity(val city: CityDomain): CreateEventIntent()

    data class SaveEvent(
        val title: String,
        val address: String,
        val description: String
    ): CreateEventIntent()
}