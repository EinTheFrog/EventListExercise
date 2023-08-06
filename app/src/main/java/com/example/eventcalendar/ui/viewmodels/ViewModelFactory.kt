package com.example.eventcalendar.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.eventcalendar.ui.viewmodels.createEvent.CreateEventViewModel
import com.example.eventcalendar.ui.viewmodels.eventInfo.EventInfoViewModel
import com.example.eventcalendar.ui.viewmodels.eventList.EventListViewModel
import com.example.eventcalendar.ui.viewmodels.eventList.EventTypeViewModel
import javax.inject.Inject
import javax.inject.Provider

class ViewModelFactory @Inject constructor(
    eventListViewModelProvider: Provider<EventListViewModel>,
    createEventViewModelProvider: Provider<CreateEventViewModel>,
    eventInfoViewModelProvider: Provider<EventInfoViewModel>,
    eventTypeViewModelProvider: Provider<EventTypeViewModel>
) : ViewModelProvider.Factory {

    private val providers = mapOf<Class<*>, Provider<out ViewModel>>(
        EventListViewModel::class.java to eventListViewModelProvider,
        CreateEventViewModel::class.java to createEventViewModelProvider,
        EventInfoViewModel::class.java to eventInfoViewModelProvider,
        EventTypeViewModel::class.java to eventTypeViewModelProvider
    )

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return providers[modelClass]?.get() as T
    }

}