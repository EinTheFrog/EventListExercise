package com.example.eventcalendar.ui.viewmodels.eventList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eventcalendar.data.domain.EventRepository
import com.example.eventcalendar.data.model.EventType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

class EventListViewModel @Inject constructor(
    private val eventRepository: EventRepository
): ViewModel() {
    private val _state = MutableStateFlow<EventListState>(EventListState.Loading)
    val state: StateFlow<EventListState> = _state

    fun toLoadingState() {
        _state.value = EventListState.Loading
    }
    fun selectEventType(eventType: EventType) {
        _state.value = EventListState.Default(newEventType = eventType)
    }

    fun startNewEventCreation() {
        viewModelScope.launch(Dispatchers.IO) {
            when(val oldState = _state.value) {
                is EventListState.Default -> {
                    _state.value = EventListState.Loading
                    val newIdResult = eventRepository.generateEventId()
                    if (newIdResult.isSuccess) {
                        val newId = newIdResult.getOrThrow()
                        _state.value = EventListState.NewEvent(
                            newEventId = newId,
                            newEventType = oldState.newEventType
                        )
                    } else {
                        _state.value = EventListState.Error(newIdResult.exceptionOrNull())
                    }
                }
                else -> {}
            }
        }
    }

}