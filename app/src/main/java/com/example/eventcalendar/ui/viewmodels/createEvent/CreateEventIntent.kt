package com.example.eventcalendar.ui.viewmodels.createEvent

import com.example.eventcalendar.model.domain.CityDomain
import java.util.Calendar

sealed class CreateEventIntent {
    data class UpdateTextInputs(
        val newName: String,
        val newAddress: String,
        val newDescription: String
    ): CreateEventIntent()

    data class UpdateName(val newName: String): CreateEventIntent()
    data class UpdateDate(val newDate: Calendar): CreateEventIntent()

    data class GetCitySuggestions(val citySearchQuery: String): CreateEventIntent()

    data class UpdateCity(val newCityName: String): CreateEventIntent()

    data class UpdateAddress(val newAddress: String): CreateEventIntent()

    data class UpdateDescription(val newDescription: String): CreateEventIntent()
}