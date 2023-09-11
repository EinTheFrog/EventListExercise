package com.example.eventcalendar.ui.screens.eventList

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.eventcalendar.R
import com.example.eventcalendar.databinding.FragmentEventListBinding
import com.example.eventcalendar.data.model.EventType
import com.example.eventcalendar.ui.EventCalendarApplication
import com.example.eventcalendar.ui.viewmodels.eventList.EventListState
import com.example.eventcalendar.ui.viewmodels.eventList.EventListViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.launch

class EventListFragment: Fragment() {
    private lateinit var binding: FragmentEventListBinding
    private lateinit var viewModel: EventListViewModel
    private val menuProvider = object: MenuProvider {
        override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
            menuInflater.inflate(R.menu.event_list, menu)
        }

        override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
            return false
        }
    }

    override fun onAttach(context: Context) {
        activity?.let {
            val appComponent = (it.application as EventCalendarApplication).appComponent
            val vm by it.viewModels<EventListViewModel> {
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
        binding = FragmentEventListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val viewPager = binding.viewPager
        viewPager.adapter = EventTypeListAdapter(this)
        viewPager.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                viewModel.selectEventType(EventType.getEventTypeById(position))
            }
        })

        val tabLayout = binding.tabLayout
        attachTabLayoutMediator(tabLayout = tabLayout, viewPager = viewPager)
        initActionBar()
        activity?.addMenuProvider(menuProvider)
        subscribeToStateUpdates()
        initFab(binding.createEventButton)
    }

    override fun onDestroyView() {
        activity?.removeMenuProvider(menuProvider)
        super.onDestroyView()
    }

    private fun attachTabLayoutMediator(tabLayout: TabLayout, viewPager: ViewPager2) {
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.setText(EventType.getEventTypeById(position).titleResource)
            tab.setIcon(EventType.getEventTypeById(position).iconResource)
        }.attach()
    }

    private fun initActionBar() {
        if (activity == null) return
        val appCompatActivity = activity as AppCompatActivity
        val actionBar = appCompatActivity.supportActionBar ?: return
        actionBar.title = getString(R.string.event_list_title)
        actionBar.setDisplayHomeAsUpEnabled(false)
    }

    private fun subscribeToStateUpdates() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.toLoadingState()
                viewModel.state.collect { state ->
                    when(state) {
                        is EventListState.NewEvent -> {
                            val action = EventListFragmentDirections.actionEventListFragmentToCreateEventFragment(
                                state.newEventId,
                                state.newEventType.ordinal
                            )
                            findNavController().navigate(action)
                        }
                        else -> {}
                    }
                }
            }
        }
    }

    private fun initFab(fabButton: Button) {
        fabButton.setOnClickListener {
            viewModel.startNewEventCreation()
        }
    }
}