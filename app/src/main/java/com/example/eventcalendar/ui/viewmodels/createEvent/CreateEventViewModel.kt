package com.example.eventcalendar.ui.viewmodels.createEvent

import android.content.res.Resources.NotFoundException
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eventcalendar.data.domain.EventRepository
import com.example.eventcalendar.data.model.EventType
import com.example.eventcalendar.data.model.domain.CityDomain
import com.example.eventcalendar.data.model.domain.EventDomain
import com.example.eventcalendar.data.model.domain.WeatherDomain
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

class CreateEventViewModel @Inject constructor(
    private val eventRepository: EventRepository
): ViewModel() {
    private val _state = MutableStateFlow<CreateEventState>(CreateEventState.Loading)
    val state: StateFlow<CreateEventState> = _state
    lateinit var eventType: EventType

    fun initState(eventId: Int, eventType: EventType) {
        viewModelScope.launch(Dispatchers.Main) {
            _state.value = CreateEventState.Loading
            this@CreateEventViewModel.eventType = eventType
            val eventResult = eventRepository.getEventById(eventId)
            if (eventResult.isSuccess) {
                val event = eventResult.getOrThrow()
                _state.value = CreateEventState.Initial(
                    event = event
                )
            } else {
                _state.value = CreateEventState.Editing(
                    suggestedCities = emptyList(),
                    selectedCity = null,
                    selectedDate = null
                )
            }
        }
    }

    fun handleUserIntent(intent: CreateEventIntent) {
        viewModelScope.launch(Dispatchers.Main) {
            when(val oldState = _state.value) {
                is CreateEventState.Initial -> {
                    _state.value = CreateEventState.Loading
                    _state.value = createNewState(intent, CreateEventState.Editing.createDefault())
                }
                is CreateEventState.Editing -> {
                    _state.value = CreateEventState.Loading
                    _state.value = createNewState(intent, oldState)
                }
                is CreateEventState.IncorrectInput -> {
                    _state.value = CreateEventState.Loading
                    _state.value = createNewState(intent, oldState.toEditing())
                }
                is CreateEventState.Finished -> {}
                is CreateEventState.Error -> {}
                is CreateEventState.Loading -> {}
            }
        }
    }

    private suspend fun eventExistsInStorage(event: EventDomain): Boolean {
        val eventResult = eventRepository.getEventById(event.id)
        return eventResult.isSuccess
    }

    private suspend fun createNewState(
        intent: CreateEventIntent,
        oldState: CreateEventState.Editing
    ): CreateEventState {
        val newState = when(intent) {
            is CreateEventIntent.SelectDate -> oldState.copy(
                selectedDate = intent.date
            )
            is CreateEventIntent.GetCitySuggestions -> {
                if (intent.citySearchQuery.isEmpty()) {
                    oldState
                } else {
                    val suggestionsResult = eventRepository.getCitySuggestions(intent.citySearchQuery)
                    if (suggestionsResult.isSuccess) {
                        val suggestions = suggestionsResult.getOrThrow()
                        oldState.copy(suggestedCities = suggestions)
                    } else {
                        CreateEventState.Error(suggestionsResult.exceptionOrNull())
                    }
                }
            }
            is CreateEventIntent.SelectCity -> {
                val city = oldState.suggestedCities.find { it. }
                oldState.copy(selectedCity = intent.city)
            }
            is CreateEventIntent.SaveEvent -> {
                if (oldState.selectedDate == null && oldState.selectedCity == null) {
                    CreateEventState.IncorrectInput()
                }
                val event = createEvent(
                    title = intent.title,
                    date = oldState.selectedDate,
                    city = oldState.selectedCity,
                    address = intent.address,
                    description = intent.description,
                    eventType = eventType
                )
                eventRepository.saveEvent(event)
                CreateEventState.Finished
            }
        }
        return newState
    }

    private fun createEvent(
        title: String,
        date: Calendar,
        city: CityDomain,
        address: String,
        description: String,
        eventType: EventType
    ): EventDomain {
        val id = 0
        val weather = WeatherDomain(0, 0.0, "", Calendar.getInstance())
        return EventDomain(
            id = id,
            title = title,
            city = city,
            address = address,
            weather = weather,
            date = date,
            description = description,
            eventType = eventType
        )
    }
}