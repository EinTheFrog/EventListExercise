package com.example.eventcalendar.ui.screens.eventList

import android.content.Context
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.eventcalendar.R
import com.example.eventcalendar.databinding.ItemEventBinding
import com.example.eventcalendar.model.domain.EventDomain
import com.example.eventcalendar.utils.extensions.toShortString
import java.util.Calendar

class EventListAdapter(
    private val eventList: List<EventDomain>,
    private val context: Context,
    private val onEventClick: (Int) -> Unit
): RecyclerView.Adapter<EventListAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val card: CardView
        val titleText: TextView
        val temperatureText: TextView
        val cityText: TextView
        val dateText: TextView

        init {
            card = view.findViewById(R.id.event_card)
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
        val event = eventList[position]
        holder.card.setOnClickListener { onEventClick(event.id) }
        holder.titleText.text = event.title
        holder.temperatureText.text = context.getString(R.string.temperature, event.weather.temp)
        holder.cityText.text = event.city.name
        holder.dateText.text = event.date.toShortString()
    }

}