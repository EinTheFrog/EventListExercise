package com.example.eventcalendar.di

import android.content.Context
import com.example.eventcalendar.domain.EventRepository
import com.example.eventcalendar.ui.screens.createEvent.CreateEventFragment
import com.example.eventcalendar.ui.screens.eventList.EventTypeFragment
import com.example.eventcalendar.ui.viewmodels.ViewModelFactory
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [NetworkModule::class, RoomModule::class, RepositoryModule::class])
interface AppComponent {

    fun inject(createEventFragment: CreateEventFragment)

    fun inject(eventListFragment: EventTypeFragment)

    fun viewModelFactory(): ViewModelFactory

    @Component.Builder
    interface Builder {
        fun build(): AppComponent

        @BindsInstance
        fun applicationContext(applicationContext: Context): Builder
    }

}