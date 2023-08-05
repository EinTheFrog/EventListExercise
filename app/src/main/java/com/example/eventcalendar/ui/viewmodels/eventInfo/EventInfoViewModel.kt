package com.example.eventcalendar.ui.viewmodels.eventInfo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eventcalendar.domain.EventRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class EventInfoViewModel @Inject constructor(
    private val eventRepository: EventRepository
): ViewModel() {
    private val _state = MutableStateFlow<EventInfoState>(EventInfoState.Loading)
    val state: StateFlow<EventInfoState> = _state

    fun getEventById(eventId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _state.value = EventInfoState.Loading
            val eventResult = eventRepository.getEventById(eventId)
            if (eventResult.isSuccess) {
                val event = eventResult.getOrThrow()
                _state.value = EventInfoState.Default(event)
            } else {
                _state.value = EventInfoState.Error(eventResult.exceptionOrNull())
            }
        }
    }

}