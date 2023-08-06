package com.example.eventcalendar.ui.viewmodels.eventList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eventcalendar.domain.EventRepository
import com.example.eventcalendar.model.EventType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class EventTypeViewModel @Inject constructor(
    private val eventRepository: EventRepository
): ViewModel() {
    private val _state = MutableStateFlow<EventTypeState>(EventTypeState.Loading)
    val state: StateFlow<EventTypeState> = _state

    fun getEventsByType(type: EventType) {
        viewModelScope.launch(Dispatchers.IO) {
            _state.value = EventTypeState.Loading
            val result = eventRepository.getEvents()
            if (result.isSuccess) {
                val newEventList = result.getOrThrow()
                val filteredEventList = newEventList.filter { it.eventType == type }
                _state.value = EventTypeState.Default(
                    eventList = filteredEventList
                )
            } else {
                _state.value = EventTypeState.Error(exception = result.exceptionOrNull())
            }
        }
    }

}