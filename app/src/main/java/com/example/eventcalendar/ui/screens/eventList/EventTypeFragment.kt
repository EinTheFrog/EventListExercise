package com.example.eventcalendar.ui.screens.eventList

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.eventcalendar.databinding.FragmentEventTypeBinding
import com.example.eventcalendar.model.EventType
import com.example.eventcalendar.model.domain.EventDomain
import com.example.eventcalendar.ui.EventCalendarApplication
import com.example.eventcalendar.ui.viewmodels.eventList.EventListState
import com.example.eventcalendar.ui.viewmodels.eventList.EventListViewModel
import com.example.eventcalendar.ui.viewmodels.eventList.EventTypeState
import com.example.eventcalendar.ui.viewmodels.eventList.EventTypeViewModel
import com.example.eventcalendar.utils.constants.eventTypeKey
import kotlinx.coroutines.launch

class EventTypeFragment: Fragment() {
    private lateinit var binding: FragmentEventTypeBinding
    private lateinit var adapter: EventListAdapter
    private val eventList: MutableList<EventDomain> = mutableListOf()
    lateinit var viewModel: EventTypeViewModel

    override fun onAttach(context: Context) {
        activity?.let {
            val appComponent = (it.application as EventCalendarApplication).appComponent
            appComponent.inject(this)
            val vm by viewModels<EventTypeViewModel> {
                appComponent.viewModelFactory()
            }
            viewModel = vm
        }

        super.onAttach(context)
    }

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
        subscribeToStateUpdates()
        getEvents()
    }

    private fun initRecyclerView(recyclerView: RecyclerView) {
        adapter = EventListAdapter(eventList, requireContext()) { eventId ->
            val action = EventListFragmentDirections.actionEventListFragmentToEventInfoFragment(eventId)
            findNavController().navigate(action)
        }
        recyclerView.adapter = adapter
        val layoutManager = GridLayoutManager(requireContext(), 2)
        recyclerView.layoutManager = layoutManager
    }

    private fun subscribeToStateUpdates() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    when(state) {
                        is EventTypeState.Default -> {
                            eventList.clear()
                            eventList.addAll(state.eventList)
                            adapter.notifyDataSetChanged()
                        }
                        is EventTypeState.Error, is EventTypeState.Loading -> {}
                    }
                }
            }
        }
    }

    private fun getEvents() {
        val eventTypeId = arguments?.getInt(eventTypeKey) ?: return
        val eventType = EventType.getEventTypeById(eventTypeId)
        viewModel.getEventsByType(eventType)
    }
}