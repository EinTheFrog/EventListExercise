package com.example.eventcalendar.ui.viewmodels.createEvent

import android.content.res.Resources.NotFoundException
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eventcalendar.domain.EventRepository
import com.example.eventcalendar.model.EventType
import com.example.eventcalendar.model.domain.EventDomain
import com.example.eventcalendar.model.domain.WeatherDomain
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

    fun initState(eventId: Int, eventType: EventType) {
        viewModelScope.launch(Dispatchers.Main) {
            _state.value = CreateEventState.Loading
            val eventResult = eventRepository.getEventById(eventId)
            if (eventResult.isSuccess) {
                val event = eventResult.getOrThrow()
                _state.value = CreateEventState.Default(
                    eventId = event.id,
                    eventName = event.title,
                    eventDate = event.date,
                    eventCity = event.city,
                    eventAddress = event.address,
                    eventDescription = event.description,
                    eventType = event.eventType,
                    suggestedCities = emptyList()
                )
            } else {
                _state.value = CreateEventState.Default(
                    eventId = eventId,
                    eventName = "",
                    eventDate = null,
                    eventCity = null,
                    eventAddress = "",
                    eventDescription = "",
                    eventType = eventType,
                    suggestedCities = emptyList()
                )
            }
        }
    }

    fun handleUserIntent(intent: CreateEventIntent) {
        viewModelScope.launch(Dispatchers.Main) {
            when(val oldState = _state.value) {
                is CreateEventState.Default -> {
                    _state.value = CreateEventState.Loading
                    _state.value = createNewState(intent, oldState)
                }
                is CreateEventState.IncorrectInput -> {
                    _state.value = CreateEventState.Loading
                    _state.value = createNewState(intent, oldState.toDefault())
                }
                is CreateEventState.Finished -> {}
                is CreateEventState.Error -> {}
                is CreateEventState.Loading -> {}
            }
        }
    }

    fun saveEvent() {
        viewModelScope.launch(Dispatchers.Main) {
            when(val oldState = _state.value) {
                is CreateEventState.Default -> {
                    saveEventWhenDefault(oldState)
                }
                is CreateEventState.IncorrectInput -> {
                    saveEventWhenIncorrectInput(oldState)
                }
                is CreateEventState.Finished -> {
                    throw IllegalStateException("Cannot create new intents when state is ${oldState::class.simpleName}")
                }
                is CreateEventState.Error -> {}
                is CreateEventState.Loading -> {}
            }
        }
    }

    private suspend fun saveEventWhenDefault(
        oldState: CreateEventState.Default
    ) {
        _state.value = CreateEventState.Loading
        val event = createEvent(oldState)
        if (event == null) {
            _state.value = oldState.toIncorrectInput()
        } else {
            saveAndFinish(event)
        }
    }

    private suspend fun saveEventWhenIncorrectInput(
        oldState: CreateEventState.IncorrectInput,
    ) {
        _state.value = CreateEventState.Loading
        val event = createEvent(oldState.toDefault())
        if (event == null) {
            _state.value = oldState.copy(
                numberOfAttempts = oldState.numberOfAttempts + 1
            )
        } else {
            saveAndFinish(event)
        }
    }

    private suspend fun saveAndFinish(event: EventDomain) {
        if (eventExistsInStorage(event)) {
            eventRepository.updateEvent(event)
        } else {
            eventRepository.saveEvent(event)
        }
        _state.value = CreateEventState.Finished
    }

    private suspend fun eventExistsInStorage(event: EventDomain): Boolean {
        val eventResult = eventRepository.getEventById(event.id)
        return eventResult.isSuccess
    }

    private suspend fun createNewState(
        intent: CreateEventIntent,
        oldState: CreateEventState.Default
    ): CreateEventState {
        val newState = when(intent) {
            is CreateEventIntent.UpdateTextInputs -> oldState.copy(
                eventName = intent.newName,
                eventAddress = intent.newAddress,
                eventDescription = intent.newDescription
            )
            is CreateEventIntent.UpdateName -> oldState.copy(
                eventName = intent.newName
            )
            is CreateEventIntent.UpdateDate -> oldState.copy(
                eventDate = intent.newDate
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
            is CreateEventIntent.UpdateCity -> {
                val newCity = oldState.suggestedCities.find { it.name == intent.newCityName }
                if (newCity != null) {
                    oldState.copy(
                        eventCity = newCity
                    )
                } else {
                    CreateEventState.Error(NotFoundException("City not found in suggestions"))
                }

            }
            is CreateEventIntent.UpdateAddress -> oldState.copy(
                eventAddress = intent.newAddress
            )
            is CreateEventIntent.UpdateDescription -> oldState.copy(
                eventDescription = intent.newDescription
            )
        }
        return newState
    }

    private fun createEvent(state: CreateEventState.Default): EventDomain? {
        if (state.eventName.isEmpty()) return null
        if (state.eventDate == null) return null
        if (state.eventCity == null) return null

        val id = state.eventId
        val title = state.eventName
        val city = state.eventCity
        val address = state.eventAddress
        val weather = WeatherDomain(0, 0.0, "", Calendar.getInstance())
        val date = state.eventDate
        val description = state.eventDescription
        val eventType = state.eventType

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