package com.example.eventcalendar.ui.viewmodels.createEvent

import com.example.eventcalendar.model.EventType
import java.util.Calendar

sealed class CreateEventState {

    data class Default(
        val eventId: Int,
        val eventName: String,
        val eventDate: Calendar?,
        val eventCity: String,
        val eventAddress: String,
        val eventDescription: String,
        val eventType: EventType
    ): CreateEventState() {
        fun toIncorrectInput(): IncorrectInput = IncorrectInput(
            eventId = eventId,
            numberOfAttempts = 0,
            eventName = eventName,
            eventDate = eventDate,
            eventCity = eventCity,
            eventAddress = eventAddress,
            eventDescription = eventDescription,
            eventType = eventType
        )
    }

    data class IncorrectInput(
        val eventId: Int,
        val numberOfAttempts: Int,
        val eventName: String,
        val eventDate: Calendar?,
        val eventCity: String,
        val eventAddress: String,
        val eventDescription: String,
        val eventType: EventType
    ): CreateEventState() {
        fun toDefault(): Default = Default(
            eventId = eventId,
            eventName = eventName,
            eventDate = eventDate,
            eventCity = eventCity,
            eventAddress = eventAddress,
            eventDescription = eventDescription,
            eventType = eventType
        )
    }

    object Loading: CreateEventState()

    object Finished: CreateEventState()

    data class Error(
        val exception: Throwable?
    ): CreateEventState()

}