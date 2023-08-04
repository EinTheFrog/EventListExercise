package com.example.eventcalendar.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eventcalendar.domain.EventRepository
import com.example.eventcalendar.model.EventType
import com.example.eventcalendar.model.domain.CityDomain
import com.example.eventcalendar.model.domain.EventDomain
import com.example.eventcalendar.model.domain.WeatherDomain
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CreateEventViewModel @Inject constructor(
    private val eventRepository: EventRepository
): ViewModel() {
    private val _state = MutableStateFlow<CreateEventState>(
        CreateEventState.Default(
            isLoading = false,
            eventName = "",
            eventDate = null,
            eventCity = "",
            eventAddress = "",
            eventDescription = ""
        )
    )
    val state: StateFlow<CreateEventState> = _state

    fun handleUserIntent(intent: CreateEventIntent) {
        viewModelScope.launch(Dispatchers.Default) {
            when(val oldState = _state.value) {
                is CreateEventState.Default -> {
                    _state.value = oldState.copy(isLoading = true)
                    val newState = createNewState(intent, oldState)
                    _state.value = newState.copy(isLoading = false)
                }
                is CreateEventState.IncorrectInput -> {
                    _state.value = oldState.copy(isLoading = false)
                    val newState = createNewState(intent, oldState.toDefault())
                    _state.value = newState.copy(isLoading = false)
                }
                is CreateEventState.Finished -> {
                    throw IllegalStateException("Cannot create new intents when state is ${oldState::class.simpleName}")
                }
                is CreateEventState.Error -> {
                    _state.value = oldState.copy(isLoading = true)
                    val newState = createNewState(intent)
                    _state.value = newState.copy(isLoading = false)
                }
            }
        }
    }

    fun saveEvent() {
        viewModelScope.launch(Dispatchers.IO) {
            when(val oldState = _state.value) {
                is CreateEventState.Default -> {
                    saveEventWhenDefault(oldState)
                }
                is CreateEventState.IncorrectInput -> {
                    saveEventWhenIncorrectInput(oldState)
                }
                is CreateEventState.Finished -> {}
                is CreateEventState.Error -> {
                    _state.value = CreateEventState.Default(
                        isLoading = false,
                        eventName = "",
                        eventDate = null,
                        eventCity = "",
                        eventAddress = "",
                        eventDescription = ""
                    )
                }
            }
        }
    }

    private suspend fun saveEventWhenDefault(
        oldState: CreateEventState.Default
    ) {
        _state.value = oldState.copy(isLoading = true)
        val event = createEvent(oldState)
        if (event == null) {
            _state.value = oldState.toIncorrectInput().copy(isLoading = false)
        } else {
            saveAndFinish(event)
        }
    }

    private suspend fun saveEventWhenIncorrectInput(
        oldState: CreateEventState.IncorrectInput,
    ) {
        _state.value = oldState.copy(isLoading = true)
        val event = createEvent(oldState.toDefault())
        if (event == null) {
            _state.value = oldState.copy(
                isLoading = false,
                numberOfAttempts = oldState.numberOfAttempts + 1
            )
        } else {
            saveAndFinish(event)
        }
    }

    private suspend fun saveAndFinish(event: EventDomain) {
        eventRepository.saveEvent(event)
        _state.value = CreateEventState.Finished
    }

    private fun createNewState(intent: CreateEventIntent): CreateEventState.Default {
        return createNewState(
            intent = intent,
            oldState = CreateEventState.Default(
                isLoading = true,
                eventName = "",
                eventDate = null,
                eventCity = "",
                eventAddress = "",
                eventDescription = ""
            )
        )
    }

    private fun createNewState(intent: CreateEventIntent, oldState: CreateEventState.Default): CreateEventState.Default {
        val newState = when(intent) {
            is CreateEventIntent.UpdateTextInputs -> oldState.copy(
                eventName = intent.newName,
                eventCity = intent.newCity,
                eventAddress = intent.newAddress,
                eventDescription = intent.newDescription
            )
            is CreateEventIntent.UpdateDate -> oldState.copy(eventDate = intent.newDate)
        }
        return newState
    }

    private suspend fun createEvent(state: CreateEventState.Default): EventDomain? {
        if (state.eventName.isEmpty()) return null
        if (state.eventDate == null) return null
        if (state.eventCity.isEmpty()) return null
        if (state.eventAddress.isEmpty()) return null

        val idResult = eventRepository.generateEventId()
        val id = idResult.getOrThrow()
        val title = state.eventName
        val city = CityDomain(0, state.eventCity, 0.0, 0.0, "")
        val weather = WeatherDomain(0, 0.0, "", Calendar.getInstance())
        val date = state.eventDate
        val eventType = EventType.COMING

        return EventDomain(
            id = id,
            title = title,
            city = city,
            weather = weather,
            date = date,
            eventType = eventType
        )
    }

}