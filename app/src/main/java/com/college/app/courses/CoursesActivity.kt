package com.college.app.courses

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.college.app.R
import com.college.app.adapter.ViewPagerAdapter
import com.college.app.databinding.ActivityCoursesBinding
import com.college.app.utils.CollegeAppViewPager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import java.util.*

class CoursesActivity : AppCompatActivity() {
    var adapter: ViewPagerAdapter? = null
    var viewPager: CollegeAppViewPager? = null
    var bsYourCoursesInfo: ConstraintLayout? = null
    var bottomSheetBehavior: BottomSheetBehavior<*>? = null
    var activityCoursesBinding: ActivityCoursesBinding? = null
    private val mFragmentTitleList: List<String> = ArrayList(Arrays.asList("All", "Your"))
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityCoursesBinding = ActivityCoursesBinding.inflate(layoutInflater)
        setContentView(activityCoursesBinding!!.root)
        setSupportActionBar(activityCoursesBinding!!.toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeButtonEnabled(true)
        supportActionBar!!.title = getString(R.string.courses)
        adapter = ViewPagerAdapter(supportFragmentManager, mFragmentTitleList)
        adapter!!.addFragment(AllCourseFragment())
        adapter!!.addFragment(YourCoursesFragment())
        viewPager = activityCoursesBinding!!.courseFragment
        viewPager!!.setPagingEnabled(true)
        viewPager!!.adapter = adapter
        activityCoursesBinding!!.tabLayout.setupWithViewPager(viewPager)
        activityCoursesBinding!!.tabLayout.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager!!.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
        //        adapter.addFragment(new NotificationsFragment());
        bsYourCoursesInfo = findViewById(R.id.bottom_sheet_yourCourses)
        bottomSheetBehavior = BottomSheetBehavior.from(bsYourCoursesInfo as ConstraintLayout)
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onBackPressed() {
        if (bottomSheetBehavior!!.state == BottomSheetBehavior.STATE_EXPANDED) {
            bottomSheetBehavior!!.setState(BottomSheetBehavior.STATE_HIDDEN)
        } else {
            super.onBackPressed()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.infoYourCourses -> {
                bottomSheetInfo()
                return true
            }
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun bottomSheetInfo() {
        if (bottomSheetBehavior!!.state != BottomSheetBehavior.STATE_EXPANDED) {
            bottomSheetBehavior!!.setState(BottomSheetBehavior.STATE_EXPANDED)
        } else {
            bottomSheetBehavior!!.setState(BottomSheetBehavior.STATE_COLLAPSED)
        }
    }

    companion object {
        private const val TAG = "CoursesActivity"
    }
}