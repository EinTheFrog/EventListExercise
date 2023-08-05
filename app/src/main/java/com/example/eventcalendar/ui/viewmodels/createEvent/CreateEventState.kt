package com.example.eventcalendar.ui.viewmodels.createEvent

import java.util.Calendar

sealed class CreateEventState {

    data class Default(
        val eventName: String,
        val eventDate: Calendar?,
        val eventCity: String,
        val eventAddress: String,
        val eventDescription: String
    ): CreateEventState() {
        fun toIncorrectInput(): IncorrectInput = IncorrectInput(
            numberOfAttempts = 0,
            eventName = eventName,
            eventDate = eventDate,
            eventCity = eventCity,
            eventAddress = eventAddress,
            eventDescription = eventDescription
        )
    }

    data class IncorrectInput(
        val numberOfAttempts: Int,
        val eventName: String,
        val eventDate: Calendar?,
        val eventCity: String,
        val eventAddress: String,
        val eventDescription: String
    ): CreateEventState() {
        fun toDefault(): Default = Default(
            eventName = eventName,
            eventDate = eventDate,
            eventCity = eventCity,
            eventAddress = eventAddress,
            eventDescription = eventDescription
        )
    }

    object Loading: CreateEventState()

    object Finished: CreateEventState()

    data class Error(
        val exception: Throwable?
    ): CreateEventState()

}