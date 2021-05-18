package com.college.app.fragment

import android.content.ContentValues
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.college.app.R
import com.college.app.club.Club
import com.college.app.databinding.ClubFragmentDetailsBinding
import com.college.app.utils.AppUtils
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso

class ClubFragmentDetails : Fragment() {
    private lateinit var clubFragmentDetailsBinding: ClubFragmentDetailsBinding
    var fetchedClub: Club? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        clubFragmentDetailsBinding = ClubFragmentDetailsBinding.inflate(inflater)
        fetchClubDetails()
        return clubFragmentDetailsBinding.root
    }

    override fun onResume() {
        super.onResume()
    }

    private fun fetchClubDetails() {
        val clubName = arguments!!.getStringArray(FRAGMENT_DATA)
        if (clubName != null) {
            val name = clubName[0]
            val reference = FirebaseDatabase.getInstance().reference.child("Clubs")
            reference.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (data in dataSnapshot.children) {
                        val club = data.getValue(Club::class.java)
                        if (club != null) {
                            if (club.clubName == name) {
                                club.clubName = club.clubName
                                club.clubField = club.clubField
                                club.head = club.head
                                club.photoUrlLandscape = club.photoUrlLandscape
                                club.photoUrl = club.photoUrl
                                club.about = club.about
                                club.upComingEvents = club.upComingEvents
                                fetchedClub = club
                                break
                            }
                        }
                    }
                    setViews()
                }

                override fun onCancelled(databaseError: DatabaseError) {}
            })
        }
    }

    fun setViews() {
        clubFragmentDetailsBinding.collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBar)
        clubFragmentDetailsBinding.collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar)
        Log.d(ContentValues.TAG, "photoLandscape" + fetchedClub?.photoUrlLandscape)
        Picasso.get()
            .load(fetchedClub?.photoUrlLandscape)
            .fit()
            .into(clubFragmentDetailsBinding.imageScrollingTop)
        if (AppUtils.getScreenWidthDp(context) >= 600) {
            val collapsing_toolbar_layout = clubFragmentDetailsBinding!!.collapsingToolbarLayout
            collapsing_toolbar_layout.setExpandedTitleTextColor(ColorStateList.valueOf(Color.TRANSPARENT))
        }
        val a = activity as HolderActivity?
        a!!.setSupportActionBar(clubFragmentDetailsBinding!!.toolbar)
        a.supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        clubFragmentDetailsBinding.toolbar.title = fetchedClub?.clubName
        clubFragmentDetailsBinding.scrollLayout.clubName.text = fetchedClub?.clubName
        clubFragmentDetailsBinding.scrollLayout.clubHeads.text = fetchedClub?.head
        //        clubFragmentDetailsBinding.scrollLayout.clubAbout.setText(fetchedClub.getAbout());
        clubFragmentDetailsBinding.scrollLayout.applicationStatus.text =
            fetchedClub?.applicationStatus
        val applicationLink = fetchedClub?.applicationLink
        clubFragmentDetailsBinding.scrollLayout.clubApplyButton.setOnClickListener { v: View? ->
            if (applicationLink != null) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(applicationLink))
                startActivity(intent)
            } else {
                Toast.makeText(context, "Sorry, Unavailable", Toast.LENGTH_SHORT).show()
            }
        }
        clubFragmentDetailsBinding.scrollLayout.clubField.text = fetchedClub?.clubField
        clubFragmentDetailsBinding.scrollLayout.clubAbout.text = fetchedClub?.about
        clubFragmentDetailsBinding.scrollLayout.upComingEvents.text =
            fetchedClub?.upComingEvents
        clubFragmentDetailsBinding.scrollLayout.clubPerks.text = fetchedClub?.perks
        clubFragmentDetailsBinding.scrollLayout.clubPerks.text = fetchedClub?.perks
        if (fetchedClub?.usefulLinks!!.isNotEmpty()) {
            clubFragmentDetailsBinding.scrollLayout.usefulLinksClub.text =
                fetchedClub?.usefulLinks
        }
        clubFragmentDetailsBinding.scrollLayout.usefulLinksClub.setOnClickListener { v: View? ->
            if (fetchedClub!!.usefulLinks!!.isNotEmpty()) {
                val i = Intent(Intent.ACTION_VIEW, Uri.parse(fetchedClub!!.usefulLinks))
                startActivity(i)
            } else {
                Toast.makeText(a, "Unavailable!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        var FRAGMENT_DATA = "transaction_data"
    }
}