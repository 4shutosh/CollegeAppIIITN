package com.college.app.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.college.app.adapter.EventsAdapter
import com.college.app.databinding.EventsFragmentBinding
import com.college.app.utils.Event
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class EventsFragment : Fragment() {
    var eventList: ArrayList<Event> = arrayListOf()
    private lateinit var eventsFragmentBinding: EventsFragmentBinding
    var bottomSheetBehavior: BottomSheetBehavior<*>? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        eventsFragmentBinding = EventsFragmentBinding.inflate(inflater)
        val h = activity as HolderActivity?
        if (h != null) {
            h.setSupportActionBar(eventsFragmentBinding.toolbar)
            h.supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            h.supportActionBar!!.title = "Events"
            h.onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (bottomSheetBehavior!!.state == BottomSheetBehavior.STATE_EXPANDED) {
                        bottomSheetBehavior!!.setState(BottomSheetBehavior.STATE_HIDDEN)
                    } else {
                        isEnabled = false
                        h.onBackPressed()
                    }
                }
            })
        }
        events()
        bottomSheetBehavior =
            BottomSheetBehavior.from(eventsFragmentBinding.bottomSheetServiceLayout.bottomSheetServices) as BottomSheetBehavior<ConstraintLayout>
        eventsFragmentBinding.cardViewEvents.setOnClickListener { v: View? ->
            if ((bottomSheetBehavior)?.state != BottomSheetBehavior.STATE_EXPANDED) {
                (bottomSheetBehavior as BottomSheetBehavior<ConstraintLayout>).setState(BottomSheetBehavior.STATE_EXPANDED)
            } else {
                (bottomSheetBehavior as BottomSheetBehavior<ConstraintLayout>).setState(BottomSheetBehavior.STATE_HIDDEN)
            }
        }
        eventsFragmentBinding.swipeLayout.setOnRefreshListener { events() }
        return eventsFragmentBinding.root
    }

    private fun events() {
        eventsFragmentBinding.swipeLayout.isRefreshing = true
        eventList = ArrayList()
        val reference = FirebaseDatabase.getInstance().reference.child("Events")
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (data in dataSnapshot.children) {
                    val event = data.getValue(
                        Event::class.java
                    )
                    if (event != null) {
                        event.eventDate = event.eventDate
                        event.eventName = event.eventName
                        event.eventDesc = event.eventDesc
                        eventList.add(event)
                    }
                }
                Log.d(TAG, "Events found" + eventList.size)
                recyclerView()
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    private fun recyclerView() {
        if (eventList.isNotEmpty()) {
            val adapter = EventsAdapter(eventList)
            val linearLayoutManager = LinearLayoutManager(context)
            eventsFragmentBinding.eventsRecycler.adapter = adapter
            eventsFragmentBinding.eventsRecycler.layoutManager = linearLayoutManager
            eventsFragmentBinding.noEventsText.visibility = View.GONE
            eventsFragmentBinding.swipeLayout.isRefreshing = false
        } else {
            eventsFragmentBinding.noEventsText.visibility = View.VISIBLE
        }
    }

    companion object {
        private const val TAG = "EventsFragment"
    }
}