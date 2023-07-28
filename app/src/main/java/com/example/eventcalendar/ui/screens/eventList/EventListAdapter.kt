package com.example.eventcalendar.ui.screens.eventList

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.eventcalendar.utils.constants.eventTypeKey

class EventListAdapter(fragment: Fragment): FragmentStateAdapter(fragment) {
    private val screensCount = 3

    override fun getItemCount(): Int = screensCount

    override fun createFragment(position: Int): Fragment {
        val fragment = EventFragment()
        val bundle = Bundle()
        bundle.putInt(eventTypeKey, position)
        return fragment
    }

}