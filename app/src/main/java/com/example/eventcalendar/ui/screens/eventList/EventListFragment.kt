package com.example.eventcalendar.ui.screens.eventList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.eventcalendar.R
import com.example.eventcalendar.databinding.FragmentEventListBinding
import com.example.eventcalendar.model.EventType
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class EventListFragment: Fragment() {
    private lateinit var binding: FragmentEventListBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEventListBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val viewPager = binding.viewPager
        viewPager.adapter = EventTypeListAdapter(this)

        val tabLayout = binding.tabLayout
        attachTabLayoutMediator(tabLayout = tabLayout, viewPager = viewPager)
        initActionBar()
    }

    private fun attachTabLayoutMediator(tabLayout: TabLayout, viewPager: ViewPager2) {
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = getString(EventType.getEventTypeById(position).titleResource)
        }.attach()
    }

    private fun initActionBar() {
        val actionBar = activity?.actionBar ?: return
        actionBar.title = getString(R.string.event_list_title)
        actionBar.setHomeButtonEnabled(false)
    }
}