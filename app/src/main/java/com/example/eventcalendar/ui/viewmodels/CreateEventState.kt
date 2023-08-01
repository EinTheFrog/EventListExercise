package com.example.eventcalendar.ui.viewmodels

import java.util.Calendar

sealed class CreateEventState {

    data class Default(
        val isLoading: Boolean,
        val selectedDate: Calendar?,
    ): CreateEventState()

    data class Error(
        val exception: Throwable?
    ): CreateEventState()

}