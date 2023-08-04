package com.example.eventcalendar.ui.viewmodels

import java.util.Calendar

sealed class CreateEventIntent {
    data class UpdateTextInputs(
        val newName: String,
        val newCity: String,
        val newAddress: String,
        val newDescription: String
    ): CreateEventIntent()
    data class UpdateDate(val newDate: Calendar): CreateEventIntent()
}