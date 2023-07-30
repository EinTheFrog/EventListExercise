package com.example.eventcalendar.ui

import android.app.Application
import com.example.eventcalendar.di.AppComponent
import com.example.eventcalendar.di.DaggerAppComponent

class EventCalendarApplication: Application() {
    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent.builder().applicationContext(this).build()
    }
}