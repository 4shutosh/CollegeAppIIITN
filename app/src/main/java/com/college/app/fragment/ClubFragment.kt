package com.college.app.fragment

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.college.app.adapter.ClubAdapter
import com.college.app.club.Club
import com.college.app.databinding.ClubFragmentBinding
import com.google.firebase.database.*
import java.util.*
import kotlin.collections.ArrayList

class ClubFragment : Fragment() {
    private var clubFragmentBinding: ClubFragmentBinding? = null
    private var clubList: ArrayList<Club> = arrayListOf()
    var databaseReference: DatabaseReference? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        clubFragmentBinding = ClubFragmentBinding.inflate(inflater)
        ///
        val a = activity as HolderActivity?
        if (a != null) {
            a.setSupportActionBar(clubFragmentBinding!!.toolbar)
            a.supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            a.supportActionBar!!.title = "Clubs"
        }
        //        clubFragmentBinding.toolbar.setTitle("Clubs");
        clubFragmentBinding!!.shimmerClubs.startShimmerAnimation()
        clubs
        swipeLayout()
        return clubFragmentBinding!!.root
    }

    // get Clubs from internet
    val clubs: Unit
        get() {
            // get Clubs from internet
            databaseReference = FirebaseDatabase.getInstance().reference.child("Clubs")
            databaseReference!!.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (data in dataSnapshot.children) {
                        val club = data.getValue(Club::class.java)
                        if (club != null) {
                            club.clubName = club.clubName
                            club.head = club.head
                            clubList.add(club)
                        }
                    }
                    Log.d(ContentValues.TAG, "clubs found " + clubList?.size)
                    recyclerView()
                }

                override fun onCancelled(databaseError: DatabaseError) {}
            })
        }

    fun recyclerView() {
        val clubAdapter = ClubAdapter(clubList, context!!)
        val gridLayoutManager = GridLayoutManager(context, 2)
        clubFragmentBinding!!.clubsRecyclerView.layoutManager = gridLayoutManager
        clubFragmentBinding!!.clubsRecyclerView.adapter = clubAdapter
        clubFragmentBinding!!.shimmerClubs.stopShimmerAnimation()
        clubFragmentBinding!!.shimmerClubs.visibility = View.GONE
        clubFragmentBinding!!.swipeLayoutClubs.isRefreshing = false
        clubFragmentBinding!!.swipeLayoutClubs.visibility = View.VISIBLE
        clubFragmentBinding!!.clubsRecyclerView.visibility = View.VISIBLE
    }

    private fun swipeLayout() {
        clubFragmentBinding!!.swipeLayoutClubs.setOnRefreshListener {
            clubFragmentBinding!!.clubsRecyclerView.visibility = View.GONE
            clubFragmentBinding!!.shimmerClubs.visibility = View.VISIBLE
            clubFragmentBinding!!.shimmerClubs.startShimmerAnimation()
            Timer().schedule(
                object : TimerTask() {
                    override fun run() {
                        // you cannot touch the UI from another thread. This thread now calls a function on the main thread
                        clubs
                    }
                }, 1000
            )
        }
    }
}