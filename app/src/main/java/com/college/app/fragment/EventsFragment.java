package com.college.app.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.college.app.adapter.EventsAdapter;
import com.college.app.databinding.EventsFragmentBinding;
import com.college.app.utils.Event;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class EventsFragment extends Fragment {

    List<Event> eventList;
    EventsFragmentBinding eventsFragmentBinding;
    BottomSheetBehavior<?> bottomSheetBehavior;
    private static final String TAG = "EventsFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        eventsFragmentBinding = EventsFragmentBinding.inflate(inflater);
        HolderActivity h = (HolderActivity) getActivity();
        if (h != null) {
            h.setSupportActionBar(eventsFragmentBinding.toolbar);
            h.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            h.getSupportActionBar().setTitle("Events");
            h.getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
                @Override
                public void handleOnBackPressed() {
                    if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                    } else {
                        setEnabled(false);
                        h.onBackPressed();
                    }
                }
            });
        }
        getEvents();
        bottomSheetBehavior = BottomSheetBehavior.from(eventsFragmentBinding.bottomSheetServiceLayout.bottomSheetServices);
        eventsFragmentBinding.cardViewEvents.setOnClickListener(v -> {
            if (bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            } else {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            }
        });
        eventsFragmentBinding.swipeLayout.setOnRefreshListener(() -> getEvents());
        return eventsFragmentBinding.getRoot();
    }

    private void getEvents() {
        eventsFragmentBinding.swipeLayout.setRefreshing(true);
        eventList = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Events");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Event event = data.getValue(Event.class);
                    if (event != null) {
                        event.setEventDate(event.getEventDate());
                        event.setEventName(event.getEventName());
                        event.setEventDesc(event.getEventDesc());
                        eventList.add(event);
                    }
                }
                Log.d(TAG, "Events found" + eventList.size());
                recyclerView();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void recyclerView() {
        if (!eventList.isEmpty()) {
            EventsAdapter adapter = new EventsAdapter(eventList);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
            eventsFragmentBinding.eventsRecycler.setAdapter(adapter);
            eventsFragmentBinding.eventsRecycler.setLayoutManager(linearLayoutManager);
            eventsFragmentBinding.noEventsText.setVisibility(View.GONE);
            eventsFragmentBinding.swipeLayout.setRefreshing(false);
        } else {
            eventsFragmentBinding.noEventsText.setVisibility(View.VISIBLE);
        }
    }
}
