package com.example.eventcalendar.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eventcalendar.data.storage.EventDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CreateEventViewModel @Inject constructor(
    private val eventDao: EventDao
): ViewModel() {
    private val _state = MutableStateFlow<CreateEventState>(
        CreateEventState.Default(isLoading = false, selectedDate = null)
    )
    val state: StateFlow<CreateEventState> = _state

    fun selectDate(year: Int, month: Int, date: Int) {
        viewModelScope.launch(Dispatchers.Default) {
            _state.value = CreateEventState.Default(isLoading = true, selectedDate = null)
            val newSelectedDate = Calendar.getInstance()
            newSelectedDate.set(year, month, date)
            _state.value = CreateEventState.Default(isLoading = false, selectedDate = newSelectedDate)
        }
    }

}