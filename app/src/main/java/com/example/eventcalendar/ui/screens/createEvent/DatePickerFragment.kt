package com.example.eventcalendar.ui.screens.createEvent

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.DatePicker
import androidx.activity.viewModels
import androidx.fragment.app.DialogFragment
import com.example.eventcalendar.ui.EventCalendarApplication
import com.example.eventcalendar.ui.viewmodels.createEvent.CreateEventIntent
import com.example.eventcalendar.ui.viewmodels.createEvent.CreateEventViewModel
import java.util.Calendar

class DatePickerFragment: DialogFragment(), DatePickerDialog.OnDateSetListener {
    private lateinit var viewModel: CreateEventViewModel

    override fun onAttach(context: Context) {
        activity?.let {
            val appComponent = (it.application as EventCalendarApplication).appComponent
            val vm by it.viewModels<CreateEventViewModel> {
                appComponent.viewModelFactory()
            }
            viewModel = vm
        }
        super.onAttach(context)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val date = calendar.get(Calendar.DATE)
        return DatePickerDialog(requireContext(), this, year, month, date)
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val newDate = Calendar.getInstance()
        newDate.set(year, month, dayOfMonth)
        viewModel.handleUserIntent(CreateEventIntent.UpdateDate(newDate))
    }
}