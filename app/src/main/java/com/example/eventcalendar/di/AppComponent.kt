package com.example.eventcalendar.di

import com.example.eventcalendar.ui.screens.createEvent.CreateEventFragment
import com.example.eventcalendar.ui.screens.eventList.EventListFragment
import dagger.Component

@Component(modules = [NetworkModule::class])
interface AppComponent {

    fun inject(createEventFragment: CreateEventFragment)

    fun inject(eventListFragment: EventListFragment)

}