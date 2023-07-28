package com.example.eventcalendar.ui.screens.eventList

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.eventcalendar.R
import com.example.eventcalendar.databinding.ItemEventBinding
import com.example.eventcalendar.model.domain.EventDomain
import java.util.Calendar

class EventListAdapter(
    private val eventList: List<EventDomain>,
    private val context: Context
): RecyclerView.Adapter<EventListAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titleText: TextView
        val temperatureText: TextView
        val cityText: TextView
        val dateText: TextView

        init {
            titleText = view.findViewById(R.id.title_text)
            temperatureText = view.findViewById(R.id.temperature_text)
            cityText = view.findViewById(R.id.city_text)
            dateText = view.findViewById(R.id.date_text)
        }
    }

    override fun getItemCount() = eventList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.titleText.text = eventList[position].title
        holder.temperatureText.text = context.getString(
            R.string.temperature,
            eventList[position].weather.temp
        )
        holder.cityText.text = eventList[position].city.name
        holder.dateText.text = eventList[position].date.get(Calendar.MONTH).toString()
    }

}