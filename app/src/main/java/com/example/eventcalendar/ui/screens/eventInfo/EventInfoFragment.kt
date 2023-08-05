package com.example.eventcalendar.ui.screens.eventInfo

import android.content.Context
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.eventcalendar.R
import com.example.eventcalendar.databinding.FragmentEventInfoBinding
import com.example.eventcalendar.model.EventType
import com.example.eventcalendar.model.domain.EventDomain
import com.example.eventcalendar.ui.EventCalendarApplication
import com.example.eventcalendar.ui.viewmodels.eventInfo.EventInfoState
import com.example.eventcalendar.ui.viewmodels.eventInfo.EventInfoViewModel
import com.example.eventcalendar.utils.extensions.getActionBar
import com.example.eventcalendar.utils.extensions.toShortString
import kotlinx.coroutines.launch

class EventInfoFragment: Fragment() {
    private lateinit var binding: FragmentEventInfoBinding
    private lateinit var viewModel: EventInfoViewModel
    private val args by navArgs<EventInfoFragmentArgs>()
    private val menuProvider = object: MenuProvider {
        override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
            menuInflater.inflate(R.menu.event_info, menu)
        }

        override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
            return when(menuItem.itemId) {
                R.id.edit_icon -> false
                R.id.delete_icon -> {
                    viewModel.deleteEvent()
                    true
                }
                else -> false
            }
        }

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity?.let {
            val appComponent = (it.application as EventCalendarApplication).appComponent
            val vm by it.viewModels<EventInfoViewModel> { appComponent.viewModelFactory() }
            viewModel = vm
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEventInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initActionBar()
        activity?.addMenuProvider(menuProvider)
        subscribeToStateUpdates()
        viewModel.getEventById(args.eventId)
        setButtonListeners()
    }

    override fun onDestroyView() {
        activity?.removeMenuProvider(menuProvider)
        super.onDestroyView()
    }

    private fun subscribeToStateUpdates() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    when(state) {
                        is EventInfoState.Default -> {
                            updateViews(state.event)
                        }
                        is EventInfoState.Finished -> {
                            findNavController().navigate(R.id.action_eventInfoFragment_to_eventListFragment)
                        }
                        is EventInfoState.Loading, is EventInfoState.Error -> {}
                    }
                }
            }
        }
    }

    private fun setButtonListeners() {
        binding.typeComingChip.setOnClickListener { viewModel.changeEventType(EventType.COMING) }
        binding.typeVisitedChip.setOnClickListener { viewModel.changeEventType(EventType.VISITED) }
        binding.typeMissedChip.setOnClickListener { viewModel.changeEventType(EventType.MISSED) }
    }

    private fun updateViews(event: EventDomain) {
        binding.eventDateText.text = event.date.toShortString()
        binding.eventCityText.text = event.city.name
        getActionBar()?.title = event.title
        when (event.eventType) {
            EventType.COMING -> {
                binding.chipGroup.check(binding.typeComingChip.id)
            }
            EventType.VISITED -> {
                binding.chipGroup.check(binding.typeVisitedChip.id)
            }
            EventType.MISSED -> {
                binding.chipGroup.check(binding.typeMissedChip.id)
            }
        }
    }

    private fun initActionBar() {
        val actionBar = getActionBar() ?: return
        actionBar.setDisplayHomeAsUpEnabled(true)
        actionBar.setHomeAsUpIndicator(R.drawable.baseline_arrow_back_24)
    }

}