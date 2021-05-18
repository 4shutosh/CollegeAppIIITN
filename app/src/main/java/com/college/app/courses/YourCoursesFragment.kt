package com.college.app.courses

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.college.app.R
import com.college.app.databinding.YourCoursesFragmentBinding
import com.facebook.shimmer.ShimmerFrameLayout
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import java.util.*

class YourCoursesFragment : Fragment() {
    var yourCourses: MutableList<Course> = ArrayList()
    var yourCoursesFragmentBinding: YourCoursesFragmentBinding? = null
    var shimmerFrameLayout: ShimmerFrameLayout? = null
    var yourCoursesAdapter: YourCoursesAdapter? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        yourCoursesFragmentBinding = YourCoursesFragmentBinding.inflate(inflater, container, false)
        EventBus.getDefault().register(this)
        shimmerFrameLayout = yourCoursesFragmentBinding!!.shimmerLayout
        shimmerFrameLayout!!.startShimmerAnimation()
        setHasOptionsMenu(true)
        attachRecyclerView(context)
        shimmerFrameLayout!!.visibility = View.GONE
        yourCoursesFragmentBinding!!.swipeLayout.setOnRefreshListener { onRefreshSwipe() }
        textDisplay()
        return yourCoursesFragmentBinding!!.root
    }

    fun onRefreshSwipe() {
        yourCoursesFragmentBinding!!.noCoursesText.visibility = View.GONE
        yourCoursesFragmentBinding!!.coursesRecyclerView.visibility = View.GONE
        shimmerFrameLayout!!.visibility = View.VISIBLE
        shimmerFrameLayout!!.startShimmerAnimation()
        yourCoursesAdapter!!.updateYourCoursesView()
        attachRecyclerView(context)
        textDisplay()
    }

    override fun onResume() {
        Log.d(TAG, "onResume: started your courses fragment")
        onRefreshSwipe()
        super.onResume()
    }

    private val courses: Unit
        private get() {}

    @Subscribe
    fun onEvent(course: Course) {
        if (activity != null) {
            activity!!.runOnUiThread {
                if (course.your) {
                    yourCoursesAdapter!!.addCourse(course)
                    yourCoursesAdapter!!.notifyDataSetChanged()
                    //                        attachRecyclerView(getContext());
                    Log.d(
                        TAG,
                        "run: " + yourCourses.size + course.courseCode + "your COde " + course.your
                    )
                } else {
                    Log.d(
                        TAG,
                        "run: " + yourCourses.size + course.courseCode + " your COde " + course.your
                    )
                    yourCoursesAdapter!!.removeItem(course)
                    yourCoursesAdapter!!.notifyDataSetChanged()
                }
                textDisplay()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        Log.d(TAG, "onCreateOptionsMenu: menu formed")
        inflater.inflate(R.menu.your_courses_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    fun recyclerLayout() {
        yourCoursesAdapter = YourCoursesAdapter(context, yourCourses)
        yourCoursesAdapter!!.notifyDataSetChanged()
        if (!yourCourses.isEmpty()) {
            val linearLayoutManager = LinearLayoutManager(context)
            linearLayoutManager.orientation = RecyclerView.VERTICAL
            yourCoursesFragmentBinding!!.coursesRecyclerView.layoutManager = linearLayoutManager
            yourCoursesFragmentBinding!!.coursesRecyclerView.adapter = yourCoursesAdapter
            shimmerFrameLayout!!.stopShimmerAnimation()
            shimmerFrameLayout!!.visibility = View.GONE
            yourCoursesFragmentBinding!!.noCoursesText.visibility = View.GONE
            yourCoursesFragmentBinding!!.coursesRecyclerView.visibility = View.VISIBLE
            yourCoursesFragmentBinding!!.swipeLayout.isRefreshing = false
        } else {
            shimmerFrameLayout!!.visibility = View.GONE
            yourCoursesFragmentBinding!!.noCoursesText.visibility = View.VISIBLE
        }
    }

    fun attachRecyclerView(context: Context?) {
        ////////////////////////Recycler View
        yourCoursesAdapter = YourCoursesAdapter(context, yourCourses)
        yourCoursesAdapter!!.updateYourCoursesView()
        val linearLayoutManager = LinearLayoutManager(getContext())
        linearLayoutManager.orientation = RecyclerView.VERTICAL
        yourCoursesFragmentBinding!!.coursesRecyclerView.layoutManager = linearLayoutManager
        yourCoursesFragmentBinding!!.coursesRecyclerView.adapter = yourCoursesAdapter
        shimmerFrameLayout!!.stopShimmerAnimation()
        shimmerFrameLayout!!.visibility = View.GONE
        yourCoursesFragmentBinding!!.noCoursesText.visibility = View.GONE
        yourCoursesFragmentBinding!!.coursesRecyclerView.visibility = View.VISIBLE
        Log.d(TAG, "attachRecyclerView: " + yourCoursesAdapter!!.itemCount)
        yourCoursesFragmentBinding!!.swipeLayout.isRefreshing = false
    }

    fun textDisplay() {
        if (yourCoursesAdapter!!.isBoxEmpty) {
            Log.d(TAG, "textDisplay: box empty")
            yourCoursesFragmentBinding!!.shimmerLayout.visibility = View.GONE
            yourCoursesFragmentBinding!!.coursesRecyclerView.visibility = View.GONE
            yourCoursesFragmentBinding!!.noCoursesText.visibility = View.VISIBLE
        }
    }

    companion object {
        private const val TAG = "AllCourseFragment"
    }
}