package com.example.eventcalendar.ui.viewmodels.createEvent

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

class CreateEventViewModel @Inject constructor(
    private val eventRepository: EventRepository
): ViewModel() {
    private val _state = MutableStateFlow<CreateEventState>(CreateEventState.Loading)
    val state: StateFlow<CreateEventState> = _state

    fun initState(eventId: Int, eventType: EventType) {
        viewModelScope.launch(Dispatchers.IO) {
            _state.value = CreateEventState.Loading
            val eventResult = eventRepository.getEventById(eventId)
            if (eventResult.isSuccess) {
                val event = eventResult.getOrThrow()
                _state.value = CreateEventState.Default(
                    eventId = event.id,
                    eventName = event.title,
                    eventDate = event.date,
                    eventCity = event.city.name,
                    eventAddress = event.address,
                    eventDescription = event.description,
                    eventType = event.eventType
                )
            } else {
                _state.value = CreateEventState.Default(
                    eventId = eventId,
                    eventName = "",
                    eventDate = null,
                    eventCity = "",
                    eventAddress = "",
                    eventDescription = "",
                    eventType = eventType
                )
            }
        }
    }

    fun handleUserIntent(intent: CreateEventIntent) {
        viewModelScope.launch(Dispatchers.Default) {
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
        viewModelScope.launch(Dispatchers.IO) {
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

    private fun createNewState(intent: CreateEventIntent, oldState: CreateEventState.Default): CreateEventState.Default {
        val newState = when(intent) {
            is CreateEventIntent.UpdateTextInputs -> oldState.copy(
                eventName = intent.newName,
                eventCity = intent.newCity,
                eventAddress = intent.newAddress,
                eventDescription = intent.newDescription
            )
            is CreateEventIntent.UpdateName -> oldState.copy(
                eventName = intent.newName
            )
            is CreateEventIntent.UpdateDate -> oldState.copy(eventDate = intent.newDate)
            is CreateEventIntent.UpdateCity -> oldState.copy(
                eventCity = intent.newCity
            )
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

        val id = state.eventId
        val title = state.eventName
        val city = CityDomain(0, state.eventCity, 0.0, 0.0, "")
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