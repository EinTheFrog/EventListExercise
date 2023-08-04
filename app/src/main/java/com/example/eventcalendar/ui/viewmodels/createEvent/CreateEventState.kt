package com.example.eventcalendar.ui.viewmodels.createEvent

import java.util.Calendar

sealed class CreateEventState {

    data class Default(
        val isLoading: Boolean,
        val eventName: String,
        val eventDate: Calendar?,
        val eventCity: String,
        val eventAddress: String,
        val eventDescription: String
    ): CreateEventState() {
        fun toIncorrectInput(): IncorrectInput = IncorrectInput(
            isLoading = isLoading,
            numberOfAttempts = 0,
            eventName = eventName,
            eventDate = eventDate,
            eventCity = eventCity,
            eventAddress = eventAddress,
            eventDescription = eventDescription
        )
    }

    data class IncorrectInput(
        val isLoading: Boolean,
        val numberOfAttempts: Int,
        val eventName: String,
        val eventDate: Calendar?,
        val eventCity: String,
        val eventAddress: String,
        val eventDescription: String
    ): CreateEventState() {
        fun toDefault(): Default = Default(
            isLoading = isLoading,
            eventName = eventName,
            eventDate = eventDate,
            eventCity = eventCity,
            eventAddress = eventAddress,
            eventDescription = eventDescription
        )
    }

    object Finished: CreateEventState()

    data class Error(
        val isLoading: Boolean,
        val exception: Throwable?
    ): CreateEventState()

}