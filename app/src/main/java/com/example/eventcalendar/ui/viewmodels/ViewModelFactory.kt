package com.example.eventcalendar.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.eventcalendar.ui.viewmodels.createEvent.CreateEventViewModel
import com.example.eventcalendar.ui.viewmodels.eventList.EventListViewModel
import javax.inject.Inject
import javax.inject.Provider

class ViewModelFactory @Inject constructor(
    eventListViewModelProvider: Provider<EventListViewModel>,
    createEventViewModelProvider: Provider<CreateEventViewModel>
) : ViewModelProvider.Factory {

    private val providers = mapOf<Class<*>, Provider<out ViewModel>>(
        EventListViewModel::class.java to eventListViewModelProvider,
        CreateEventViewModel::class.java to createEventViewModelProvider
    )

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return providers[modelClass]?.get() as T
    }

}