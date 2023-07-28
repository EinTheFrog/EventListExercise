package com.example.eventcalendar.ui.screens.eventList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.eventcalendar.databinding.FragmentEventBinding
import com.example.eventcalendar.utils.constants.eventTypeKey

class EventFragment: Fragment() {
    lateinit var binding: FragmentEventBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEventBinding.inflate(inflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val arguments = arguments.takeIf { it?.containsKey(eventTypeKey) ?: false } ?: return
        val eventType = arguments.getInt(eventTypeKey)
        binding.screenNumberText.text = eventType.toString()
    }
}