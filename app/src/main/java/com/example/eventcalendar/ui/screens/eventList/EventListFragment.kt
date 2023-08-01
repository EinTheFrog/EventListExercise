package com.example.eventcalendar.ui.screens.eventList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.eventcalendar.R
import com.example.eventcalendar.databinding.FragmentEventListBinding
import com.example.eventcalendar.model.EventType
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class EventListFragment: Fragment() {
    private lateinit var binding: FragmentEventListBinding
    private val menuProvider = object: MenuProvider {
        override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
            menuInflater.inflate(R.menu.event_list, menu)
        }

        override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
            return false
        }
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
        super.onViewCreated(view, savedInstanceState)
        val viewPager = binding.viewPager
        viewPager.adapter = EventTypeListAdapter(this)

        val tabLayout = binding.tabLayout
        attachTabLayoutMediator(tabLayout = tabLayout, viewPager = viewPager)
        initActionBar()
        activity?.addMenuProvider(menuProvider)
        initFab(binding.createEventButton)
    }

    override fun onDestroyView() {
        activity?.removeMenuProvider(menuProvider)
        super.onDestroyView()
    }

    private fun attachTabLayoutMediator(tabLayout: TabLayout, viewPager: ViewPager2) {
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = getString(EventType.getEventTypeById(position).titleResource)
        }.attach()
    }

    private fun initActionBar() {
        if (activity == null) return
        val appCompatActivity = activity as AppCompatActivity
        val actionBar = appCompatActivity.supportActionBar ?: return
        actionBar.title = getString(R.string.event_list_title)
        actionBar.setDisplayHomeAsUpEnabled(false)
    }

    private fun initFab(fabButton: Button) {
        fabButton.setOnClickListener {
            it.findNavController().navigate(R.id.action_eventListFragment_to_createEventFragment)
        }
    }
}