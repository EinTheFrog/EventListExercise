package com.example.eventcalendar.ui.screens.createEvent

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import com.example.eventcalendar.R
import com.example.eventcalendar.databinding.FragmentCreateEventBinding
import com.example.eventcalendar.ui.EventCalendarApplication
import com.example.eventcalendar.utils.extensions.attachActionBarMenuProvider
import com.example.eventcalendar.utils.extensions.detachActionBarMenuProvider

class CreateEventFragment: Fragment() {
    private val menuProvider = object: MenuProvider {
        override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
            menuInflater.inflate(R.menu.create_event, menu)
        }

        override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
            return false
        }
    }

    override fun onAttach(context: Context) {
        activity?.let {
            (it.application as EventCalendarApplication).appComponent.inject(this)
        }
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentCreateEventBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initActionBar()
        attachActionBarMenuProvider(menuProvider)
    }

    override fun onDestroyView() {
        detachActionBarMenuProvider(menuProvider)
        super.onDestroyView()
    }

    private fun initActionBar() {
        if (activity == null) return
        val appCompatActivity = activity as AppCompatActivity
        val actionBar = appCompatActivity.supportActionBar ?: return
        actionBar.title = getString(R.string.event_list_title)
        actionBar.setDisplayHomeAsUpEnabled(true)
        actionBar.setHomeAsUpIndicator(R.drawable.baseline_close_24)
    }
}