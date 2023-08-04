package com.example.eventcalendar.ui.viewmodels.eventInfo

import androidx.lifecycle.ViewModel
import com.example.eventcalendar.domain.EventRepository
import javax.inject.Inject

class EventInfoViewModel @Inject constructor(
    private val eventRepository: EventRepository
): ViewModel() {

}