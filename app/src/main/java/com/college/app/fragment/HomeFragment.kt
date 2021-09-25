package com.college.app.fragment

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.college.app.CollegeApplication
import com.college.app.R
import com.college.app.attendance.AttendanceFragment
import com.college.app.attendance.TimeTableActivity
import com.college.app.courses.CoursesActivity
import com.college.app.databinding.FragmentHomeBinding
import com.college.app.profile.Profile
import de.hdodenhof.circleimageview.CircleImageView
import io.objectbox.Box
import io.objectbox.BoxStore

class HomeFragment : Fragment() {
    private var fragmentHomeBinding: FragmentHomeBinding? = null
    private val COLLEGE_WEBSITE = "http://www.iiitn.ac.in"
    private var profileBoxStore: BoxStore? = null
    private var profileBox: Box<Profile>? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentHomeBinding = FragmentHomeBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        cardViews()
        return fragmentHomeBinding!!.root
    }

    private fun cardViews() {
        fragmentHomeBinding!!.websiteCardView.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(COLLEGE_WEBSITE))
            startActivity(browserIntent)
        }
        fragmentHomeBinding!!.coursesCardView.setOnClickListener {
            val i = Intent(activity, CoursesActivity::class.java)
            startActivity(i)
        }
        context?.let { context ->
            fragmentHomeBinding!!.attendanceCard.setOnClickListener {
                val extra = arrayOf("A", "B", "C") // just for example
                HolderActivity.startActivity(
                    context,
                    AttendanceFragment::class.java,
                    "settings",
                    extra
                )
            }
            fragmentHomeBinding!!.clubsCard.setOnClickListener {
                val extra = arrayOf("A", "B", "C") // just for example
                HolderActivity.startActivity(context, ClubFragment::class.java, "clubs", extra)
            }
            fragmentHomeBinding!!.timeTableCardView.setOnClickListener {
                val intent = Intent(activity, TimeTableActivity::class.java)
                startActivity(intent)
            }
            fragmentHomeBinding!!.eLibraryCard.setOnClickListener {
                val extra = arrayOf("A", "B", "C") // redundant
                HolderActivity.startActivity(
                    context,
                    ELibraryFragment::class.java,
                    "ELibrary",
                    extra
                )
            }
            fragmentHomeBinding!!.servicesCard.setOnClickListener {
                val extra = arrayOf("A", "B", "C") // redundant
                HolderActivity.startActivity(
                    context,
                    ServiceFragment::class.java,
                    "ELibrary",
                    extra
                )
            }
            fragmentHomeBinding!!.eventsCard.setOnClickListener {
                val extra = arrayOf("A", "B", "C") // redundant
                HolderActivity.startActivity(context, EventsFragment::class.java, "Events", extra)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentHomeBinding = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.home_fragment_menu, menu)
        val item = menu.findItem(R.id.profilePhoto)
        val img: CircleImageView = item.actionView.findViewById(R.id.profile_photo_toolbar)
        profileBoxStore = (activity!!.application as CollegeApplication).boxStore
        profileBox = profileBoxStore!!.boxFor(Profile::class.java)
        val list = (profileBox)?.all
        val profile = list?.get(0)
        if (profile != null) {
            if (profile.imageBitmapEncoded != null) img.setImageBitmap(decodeImageBitmap(profile.imageBitmapEncoded))
        }
        img.setOnClickListener {
            Toast.makeText(context, "Profile Editing options not available", Toast.LENGTH_SHORT)
                .show()
        }
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun decodeImageBitmap(encoded: String?): Bitmap {
        val decodedString = Base64.decode(encoded, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
    }

}