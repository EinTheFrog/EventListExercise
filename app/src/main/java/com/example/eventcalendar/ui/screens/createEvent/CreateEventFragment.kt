package com.example.eventcalendar.ui.screens.createEvent

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.eventcalendar.R
import com.example.eventcalendar.databinding.FragmentCreateEventBinding
import com.example.eventcalendar.ui.EventCalendarApplication
import com.example.eventcalendar.ui.viewmodels.createEvent.CreateEventIntent
import com.example.eventcalendar.ui.viewmodels.createEvent.CreateEventState
import com.example.eventcalendar.ui.viewmodels.createEvent.CreateEventViewModel
import com.example.eventcalendar.utils.extensions.toReadableString
import kotlinx.coroutines.launch

class CreateEventFragment: Fragment() {
    private lateinit var binding: FragmentCreateEventBinding
    private lateinit var viewModel: CreateEventViewModel

    private val menuProvider = object: MenuProvider {
        override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
            menuInflater.inflate(R.menu.create_event, menu)
        }

        override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
            return when(menuItem.itemId) {
                R.id.confirm_icon -> {
                    saveEvent()
                    true
                }
                else -> false
            }
        }
    }

    override fun onAttach(context: Context) {
        activity?.let {
            val appComponent = (it.application as EventCalendarApplication).appComponent
            val vm by viewModels<CreateEventViewModel> { appComponent.viewModelFactory() }
            viewModel = vm
            appComponent.inject(this)
        }
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreateEventBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initActionBar()
        activity?.addMenuProvider(menuProvider)
        binding.eventDateButton.setOnClickListener { showPickDateDialog() }
        subscribeToStateUpdates()
    }

    override fun onDestroyView() {
        activity?.removeMenuProvider(menuProvider)
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

    private fun showPickDateDialog() {
        val calendarFragment = DatePickerFragment()
        if (activity == null) return
        calendarFragment.show(childFragmentManager, "datePicker")
    }

    private fun subscribeToStateUpdates() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    when(state) {
                        is CreateEventState.Default -> {
                            updateInputs(state)
                        }
                        is CreateEventState.IncorrectInput -> {
                            val toast = Toast(requireContext())
                            toast.setText("Incorrect input")
                            toast.show()
                        }
                        is CreateEventState.Finished -> {
                            findNavController().navigate(R.id.action_createEventFragment_to_eventListFragment)
                        }
                        is CreateEventState.Error, is CreateEventState.Loading -> {}
                    }
                }
            }
        }
    }

    private fun updateInputs(createEventState: CreateEventState.Default) {
        binding.eventDateButton.text =
            createEventState.eventDate?.toReadableString() ?: getString(R.string.event_date_hint)
    }

    private fun saveEvent() {
        updateInputState()
        viewModel.saveEvent()
    }

    private fun updateInputState() {
        viewModel.handleUserIntent(
            CreateEventIntent.UpdateTextInputs(
            newName = binding.eventNameInput.text.toString(),
            newCity = binding.eventCityInput.text.toString(),
            newAddress = binding.eventAddressInput.text.toString(),
            newDescription = binding.eventDescriptionInput.text.toString()
        ))
    }

}