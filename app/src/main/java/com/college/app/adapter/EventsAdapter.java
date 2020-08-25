package com.college.app.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.college.app.databinding.EventItemBinding;
import com.college.app.utils.Event;

import java.util.List;

public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.Item> {

    List<Event> eventList;

    public EventsAdapter(List<Event> eventList) {
        this.eventList = eventList;
    }

    @NonNull
    @Override
    public Item onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        EventItemBinding eventItemBinding = EventItemBinding.inflate(layoutInflater, parent, false);
        return new Item(eventItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull Item holder, int position) {
        holder.eventItemBinding.eventName.setText(eventList.get(position).getEventName());
        holder.eventItemBinding.eventDesc.setText(eventList.get(position).getEventDesc());
        holder.eventItemBinding.eventDate.setText(eventList.get(position).getEventDate());
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    public static class Item extends RecyclerView.ViewHolder {
        EventItemBinding eventItemBinding;

        public Item(EventItemBinding eventItemBinding) {
            super(eventItemBinding.getRoot());
            this.eventItemBinding = eventItemBinding;
        }
    }
}
