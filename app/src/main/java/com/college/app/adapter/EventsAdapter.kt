package com.college.app.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.college.app.databinding.EventItemBinding
import com.college.app.utils.Event

class EventsAdapter(var eventList: List<Event>?) : RecyclerView.Adapter<EventsAdapter.Item>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Item {
        val layoutInflater = LayoutInflater.from(parent.context)
        val eventItemBinding = EventItemBinding.inflate(layoutInflater, parent, false)
        return Item(eventItemBinding)
    }

    override fun onBindViewHolder(holder: Item, position: Int) {
        holder.eventItemBinding.eventName.text = eventList!![position].eventName
        holder.eventItemBinding.eventDesc.text = eventList!![position].eventDesc
        holder.eventItemBinding.eventDate.text = eventList!![position].eventDate
    }

    override fun getItemCount(): Int {
        return eventList!!.size
    }

    class Item(var eventItemBinding: EventItemBinding) : RecyclerView.ViewHolder(
        eventItemBinding.root
    )
}