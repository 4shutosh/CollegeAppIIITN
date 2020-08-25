package com.college.app.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.college.app.databinding.ServicesFragmentBinding
import com.college.app.services.Service
import com.college.app.services.ServiceAdapter
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlin.collections.ArrayList

class ServiceFragment : Fragment() {
    private var servicesFragmentBinding: ServicesFragmentBinding? = null
    private var services: MutableList<Service>? = null
    lateinit var bottomSheetBehaviorServices: BottomSheetBehavior<*>
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        servicesFragmentBinding = ServicesFragmentBinding.inflate(inflater)
        val h = activity as HolderActivity?
        if (h != null) {
            h.setSupportActionBar(servicesFragmentBinding!!.toolbar)
            h.supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            h.supportActionBar!!.title = "Services"
            h.onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (bottomSheetBehaviorServices.state == BottomSheetBehavior.STATE_EXPANDED) {
                        bottomSheetBehaviorServices.setState(BottomSheetBehavior.STATE_HIDDEN)
                    } else {
                        isEnabled = false
                        h.onBackPressed()
                    }
                }
            })
            setHasOptionsMenu(true)
        }
        getServices()
        bottomSheetBehaviorServices = BottomSheetBehavior.from(servicesFragmentBinding!!.bottomSheetServiceLayout.bottomSheetServices)
        servicesFragmentBinding!!.swipeLayout.setOnRefreshListener { getServices() }
        servicesFragmentBinding!!.cardViewTodoInfo.setOnClickListener {
            if (bottomSheetBehaviorServices.state != BottomSheetBehavior.STATE_EXPANDED) {
                bottomSheetBehaviorServices.setState(BottomSheetBehavior.STATE_EXPANDED)
            } else {
                bottomSheetBehaviorServices.setState(BottomSheetBehavior.STATE_COLLAPSED)
            }
        }
        return servicesFragmentBinding!!.root
    }

    private fun getServices() {
        servicesFragmentBinding!!.swipeLayout.isRefreshing = true
        services = ArrayList()
        val listTemp: MutableList<Service> = ArrayList()
        val databaseReference = FirebaseDatabase.getInstance().reference.child("Services")
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (data in dataSnapshot.children) {
                    val service = data.getValue(Service::class.java)
                    if (service != null) {
                        service.link = service.link
                        service.serviceName = service.serviceName
                        service.serviceShortDesc = service.serviceShortDesc
                        listTemp.add(service)
                    }
                }
                services = listTemp
                Log.d(TAG, "services obtained! size :" + services?.size)
                recyclerAttach()
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    private fun recyclerAttach() {
        val adapter = services?.let { context?.let { it1 -> ServiceAdapter(it, it1) } }
        val linearLayoutManager = LinearLayoutManager(context)
        servicesFragmentBinding!!.serviceRecycler.layoutManager = linearLayoutManager
        servicesFragmentBinding!!.serviceRecycler.adapter = adapter
        servicesFragmentBinding!!.swipeLayout.isRefreshing = false
    }

    companion object {
        private const val TAG = "ServiceFragment"
    }
}