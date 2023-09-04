package com.example.eventcalendar.ui.viewmodels.createEvent

import com.example.eventcalendar.model.EventType
import com.example.eventcalendar.model.domain.CityDomain
import java.util.Calendar

sealed class CreateEventState {

    data class Default(
        val eventId: Int,
        val eventName: String,
        val eventDate: Calendar?,
        val eventCity: CityDomain?,
        val eventAddress: String,
        val eventDescription: String,
        val eventType: EventType,
        val suggestedCities: List<CityDomain>
    ): CreateEventState() {
        fun toIncorrectInput(): IncorrectInput = IncorrectInput(
            eventId = eventId,
            numberOfAttempts = 0,
            eventName = eventName,
            eventDate = eventDate,
            eventCity = eventCity,
            eventAddress = eventAddress,
            eventDescription = eventDescription,
            eventType = eventType,
            suggestedCities = suggestedCities
        )
    }

    data class IncorrectInput(
        val eventId: Int,
        val numberOfAttempts: Int,
        val eventName: String,
        val eventDate: Calendar?,
        val eventCity: CityDomain?,
        val eventAddress: String,
        val eventDescription: String,
        val eventType: EventType,
        val suggestedCities: List<CityDomain>
    ): CreateEventState() {
        fun toDefault(): Default = Default(
            eventId = eventId,
            eventName = eventName,
            eventDate = eventDate,
            eventCity = eventCity,
            eventAddress = eventAddress,
            eventDescription = eventDescription,
            eventType = eventType,
            suggestedCities = suggestedCities
        )
    }

    object Loading: CreateEventState()

    object Finished: CreateEventState()

    data class Error(
        val exception: Throwable?
    ): CreateEventState()

}