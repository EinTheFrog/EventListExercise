package com.example.eventcalendar.ui.viewmodels.createEvent

import com.example.eventcalendar.data.model.domain.CityDomain
import com.example.eventcalendar.data.model.domain.EventDomain
import java.util.Calendar

sealed class CreateEventState {
    data class Initial(
        val event: EventDomain
    ): CreateEventState()

    data class Editing(
        val suggestedCities: List<CityDomain>,
        val selectedDate: Calendar?,
        val selectedCity: CityDomain?
    ): CreateEventState() {

        companion object {
            fun createDefault() = Editing(
                suggestedCities = emptyList(),
                selectedDate = null,
                selectedCity = null
            )
        }
        fun toIncorrectInput() = IncorrectInput(
            suggestedCities = suggestedCities,
            selectedDate = selectedDate,
            selectedCity = selectedCity,
            numberOfAttempts = 0
        )
    }

    data class IncorrectInput(
        val suggestedCities: List<CityDomain>,
        val selectedDate: Calendar?,
        val selectedCity: CityDomain?,
        val numberOfAttempts: Int
    ): CreateEventState() {
        fun toEditing() = Editing(
            suggestedCities = suggestedCities,
            selectedDate = selectedDate,
            selectedCity = selectedCity
        )
    }

    object Loading: CreateEventState()

    object Finished: CreateEventState()

    data class Error(
        val exception: Throwable?
    ): CreateEventState()
}