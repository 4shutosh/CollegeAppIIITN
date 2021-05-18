package com.college.app.courses

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.college.app.databinding.AllCourseFragmentBinding
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.*
import kotlin.collections.ArrayList

class AllCourseFragment : Fragment() {
    private var courseList: ArrayList<Course> = arrayListOf()
    private lateinit var allCourseFragmentBinding: AllCourseFragmentBinding
    private lateinit var shimmerFrameLayout: ShimmerFrameLayout
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        allCourseFragmentBinding = AllCourseFragmentBinding.inflate(inflater, container, false)
        shimmerFrameLayout = allCourseFragmentBinding.shimmerLayout
        shimmerFrameLayout.startShimmerAnimation()
        courses
        allCourseFragmentBinding.swipeLayout.setOnRefreshListener {
            allCourseFragmentBinding.coursesRecyclerView.visibility = View.GONE
            shimmerFrameLayout.visibility = View.VISIBLE
            shimmerFrameLayout.startShimmerAnimation()
            Timer().schedule(
                object : TimerTask() {
                    override fun run() {
                        // you cannot touch the UI from another thread. This thread now calls a function on the main thread
                        courses
                    }
                }, 1000
            )
        }
        allCourseFragmentBinding.coursesRecyclerView.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                val topRowVerticalPosition =
                    if (recyclerView.childCount == 0) 0 else recyclerView.getChildAt(0).top
                allCourseFragmentBinding.swipeLayout.isEnabled = topRowVerticalPosition >= 0
            }
        })
        return allCourseFragmentBinding.root
    }

    private val courses: Unit
        get() {
            val database = FirebaseDatabase.getInstance()
            val myRef = database.getReference("Courses")
            courseList = ArrayList()
            myRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (data in dataSnapshot.children) {
                        val course = data.getValue(Course::class.java)
                        if (course != null) {
                            courseList.add(course)
                        }
                    }
                    Log.d(TAG, "number of courses found: " + courseList.size)
                    recyclerLayout()
                }

                override fun onCancelled(databaseError: DatabaseError) {}
            })
        }

    fun recyclerLayout() {
        val coursesAdapter = CoursesAdapter(context, courseList)
        val linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.orientation = RecyclerView.VERTICAL
        allCourseFragmentBinding.coursesRecyclerView.layoutManager = linearLayoutManager
        allCourseFragmentBinding.coursesRecyclerView.adapter = coursesAdapter
        shimmerFrameLayout.stopShimmerAnimation()
        shimmerFrameLayout.visibility = View.GONE
        allCourseFragmentBinding.coursesRecyclerView.visibility = View.VISIBLE
        allCourseFragmentBinding.swipeLayout.isRefreshing = false
    }

    companion object {
        private const val TAG = "AllCourseFragment"
    }
}