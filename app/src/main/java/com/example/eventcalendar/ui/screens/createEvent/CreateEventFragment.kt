package com.example.eventcalendar.ui.screens.createEvent

import android.app.SearchManager
import android.content.Context
import android.database.Cursor
import android.database.MatrixCursor
import android.os.Bundle
import android.provider.BaseColumns
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuProvider
import androidx.cursoradapter.widget.CursorAdapter
import androidx.cursoradapter.widget.SimpleCursorAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.eventcalendar.R
import com.example.eventcalendar.databinding.FragmentCreateEventBinding
import com.example.eventcalendar.data.model.EventType
import com.example.eventcalendar.data.model.domain.EventDomain
import com.example.eventcalendar.ui.EventCalendarApplication
import com.example.eventcalendar.ui.viewmodels.createEvent.CreateEventIntent
import com.example.eventcalendar.ui.viewmodels.createEvent.CreateEventState
import com.example.eventcalendar.ui.viewmodels.createEvent.CreateEventViewModel
import com.example.eventcalendar.utils.extensions.toReadableString
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

class CreateEventFragment: Fragment() {
    private lateinit var binding: FragmentCreateEventBinding
    private lateinit var viewModel: CreateEventViewModel
    private val args by navArgs<CreateEventFragmentArgs>()
    private val cursorAdapter by lazy { createCursorAdapter() }
    private var cityQuery = ""

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
        initCitySearchView()
        activity?.addMenuProvider(menuProvider)
        binding.eventDateButton.setOnClickListener { showPickDateDialog() }
        viewModel.initState(args.eventId, EventType.getEventTypeById(args.eventTypeId))
        subscribeToStateUpdates()
    }

    override fun onDestroyView() {
        activity?.removeMenuProvider(menuProvider)
        super.onDestroyView()
    }

    private fun createCursorAdapter(): SimpleCursorAdapter {
        val ca = SimpleCursorAdapter(
            context,
            R.layout.item_search_suggestion,
            null,
            arrayOf(SearchManager.SUGGEST_COLUMN_TEXT_1),
            intArrayOf(R.id.search_suggestion_text),
            CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER
        )
        binding.eventCitySearchView.suggestionsAdapter = ca
        return ca
    }

    private fun initActionBar() {
        if (activity == null) return
        val appCompatActivity = activity as AppCompatActivity
        val actionBar = appCompatActivity.supportActionBar ?: return
        actionBar.title = getString(R.string.event_list_title)
        actionBar.setDisplayHomeAsUpEnabled(true)
        actionBar.setHomeAsUpIndicator(R.drawable.baseline_close_24)
    }

    private fun initCitySearchView() {
        binding.eventCitySearchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                cityQuery = newText ?: ""
                viewModel.handleUserIntent(CreateEventIntent.GetCitySuggestions(newText ?: ""))
                return true
            }
        })
        binding.eventCitySearchView.setOnSuggestionListener(object: SearchView.OnSuggestionListener {
            override fun onSuggestionSelect(position: Int): Boolean {
                return false
            }

            override fun onSuggestionClick(position: Int): Boolean {
                val cursor = binding.eventCitySearchView.suggestionsAdapter.getItem(position) as Cursor
                val columnIndex = cursor.getColumnIndex(SearchManager.SUGGEST_COLUMN_TEXT_1)
                if (columnIndex < 0) return true
                val suggestion = cursor.getString(columnIndex)
                viewModel.handleUserIntent(CreateEventIntent.UpdateCity(suggestion))
                binding.eventCitySearchView.clearFocus()
                return true
            }
        })
    }

    private fun showPickDateDialog() {
        val calendarFragment = DatePickerFragment()
        if (activity == null) return
        calendarFragment.show(childFragmentManager, "datePicker")
    }

    private fun subscribeToStateUpdates() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    when(state) {
                        is CreateEventState.Initial -> {
                            if (state.event != null) {
                                updateInputs(state.event)
                            }
                        }
                        is CreateEventState.Editing -> {
                            val suggestions = state.suggestedCities.map { it.name }
                            updateCitySuggestions(suggestions)
                        }
                        is CreateEventState.IncorrectInput -> {
                            val toast = Toast(requireContext())
                            toast.setText(getString(R.string.incorrect_input))
                            toast.show()
                        }
                        is CreateEventState.Finished -> {
                            findNavController().navigate(R.id.action_createEventFragment_to_eventListFragment)
                        }
                        is CreateEventState.Error -> {
                            val toast = Toast(requireContext())
                            toast.setText(state.exception.toString())
                            toast.show()
                            findNavController().navigate(R.id.action_createEventFragment_to_eventListFragment)
                        }
                        is CreateEventState.Loading -> {}
                    }
                }
            }
        }
    }

    private fun updateInputs(event: EventDomain) {
        binding.eventNameInput.setText(event.title)
        binding.eventDateButton.text = event.date.toReadableString() ?: getString(R.string.event_date_hint)
        binding.eventCitySearchView.setQuery(event.city.name, true)
        binding.eventAddressInput.setText(event.address)
        binding.eventDescriptionInput.setText(event.description)
    }

    private fun updateCitySuggestions(suggestions: List<String>) {
        val cursor = MatrixCursor(arrayOf(BaseColumns._ID, SearchManager.SUGGEST_COLUMN_TEXT_1))
        suggestions.forEachIndexed { index, suggestion ->
            if (suggestions[index].contains(cityQuery, false)) {
                cursor.addRow(arrayOf(index, suggestion))
            }
        }
        cursorAdapter.changeCursor(cursor)
    }

    private fun saveEvent() {
        updateInputState()
        viewModel.saveEvent()
    }

    private fun updateInputState() {
        viewModel.handleUserIntent(
            CreateEventIntent.UpdateTextInputs(
            newName = binding.eventNameInput.text.toString(),
            newAddress = binding.eventAddressInput.text.toString(),
            newDescription = binding.eventDescriptionInput.text.toString()
        ))
    }

}