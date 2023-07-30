package com.example.eventcalendar.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eventcalendar.domain.EventRepository
import com.example.eventcalendar.model.EventType
import com.example.eventcalendar.model.domain.EventDomain
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class EventListViewModel @Inject constructor(
    private val eventRepository: EventRepository
): ViewModel() {
    private val _state = MutableStateFlow<EventListState>(EventListState.Default(
        isLoading = true,
        eventList = emptyList()
    ))
    val state: StateFlow<EventListState> = _state

    fun getEventsByType(type: EventType) {
        viewModelScope.launch(Dispatchers.IO) {
            _state.apply {
                value = EventListState.Default(isLoading = true, eventList = emptyList())
                val result = eventRepository.getEvents()
                value = if (result.isSuccess) {
                    val newEventList = result.getOrDefault(emptyList())
                    val filteredEventList = newEventList.filter { it.eventType == type }
                    EventListState.Default(isLoading = false, eventList = filteredEventList)
                } else {
                    EventListState.Error(result.exceptionOrNull())
                }
            }
        }
    }

}