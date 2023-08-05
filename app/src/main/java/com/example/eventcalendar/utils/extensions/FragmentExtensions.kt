package com.example.eventcalendar.utils.extensions

import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

fun Fragment.getActionBar(): ActionBar? {
    if (activity == null) return null
    val appCompatActivity = activity as AppCompatActivity
    return appCompatActivity.supportActionBar
}