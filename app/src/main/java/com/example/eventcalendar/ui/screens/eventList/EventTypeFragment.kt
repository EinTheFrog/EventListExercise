package com.example.eventcalendar.ui.screens.eventList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.example.eventcalendar.databinding.FragmentEventTypeBinding
import com.example.eventcalendar.model.EventType
import com.example.eventcalendar.model.domain.CityDomain
import com.example.eventcalendar.model.domain.EventDomain
import com.example.eventcalendar.model.domain.WeatherDomain
import com.example.eventcalendar.utils.constants.eventTypeKey
import java.util.Calendar

class EventTypeFragment: Fragment() {
    private lateinit var binding: FragmentEventTypeBinding
    private lateinit var adapter: EventListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEventTypeBinding.inflate(inflater)

        initRecyclerView(binding.eventRecyclerView)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val arguments = arguments.takeIf { it?.containsKey(eventTypeKey) ?: false } ?: return
        val eventType = arguments.getInt(eventTypeKey)
    }

    private fun initRecyclerView(recyclerView: RecyclerView) {
        val eventList = listOf(
            EventDomain(0, "Event 1", CityDomain("Dubai", 0.0, 0.0, "UAE"), WeatherDomain(43.0, "", Calendar.getInstance()), Calendar.getInstance(), EventType.COMING),
            EventDomain(1, "Event 2", CityDomain("St. Petersburg", 0.0, 0.0, "Russia"), WeatherDomain(21.0, "", Calendar.getInstance()), Calendar.getInstance(), EventType.COMING),
            EventDomain(2, "Event 3", CityDomain("Tbilisi", 0.0, 0.0, "Georgia"), WeatherDomain(27.0, "", Calendar.getInstance()), Calendar.getInstance(), EventType.COMING),
            EventDomain(3, "Event 4", CityDomain("Erevan", 0.0, 0.0, "Armenia"), WeatherDomain(31.0, "", Calendar.getInstance()), Calendar.getInstance(), EventType.COMING)
        )

        val adapter = EventListAdapter(eventList, requireContext())
        recyclerView.adapter = adapter
        val layoutManager = GridLayoutManager(requireContext(), 2)
        recyclerView.layoutManager = layoutManager
    }
}