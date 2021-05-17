package com.college.app.attendance

import android.net.ConnectivityManager
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.college.app.adapter.ViewPagerAdapter
import com.college.app.databinding.ActivityTimetableBinding
import com.college.app.fragment.TimeTableFragment
import com.college.app.fragment.TimeTableFragment2
import com.college.app.utils.CollegeAppViewPager
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import java.net.InetAddress
import java.util.*

class TimeTableActivity : AppCompatActivity() {
    private lateinit var activityTimetableBinding: ActivityTimetableBinding
    var adapter: ViewPagerAdapter? = null
    var viewPager: CollegeAppViewPager? = null
    var timeTableLinkCSE = "CSE"
    var timeTableLinkECE = "ECE"
    private val fragmentTitleList: List<String> = ArrayList(listOf("ECE", "CSE"))
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityTimetableBinding = ActivityTimetableBinding.inflate(
            layoutInflater
        )
        setContentView(activityTimetableBinding.root)
        setSupportActionBar(activityTimetableBinding.toolbar)
        supportActionBar!!.title = "TimeTable"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        if (!isNetworkConnected) {
            if (!isInternetAvailable) {
                Toast.makeText(this, "Please connect to internet", Toast.LENGTH_LONG).show()
            }
        }
        adapter = ViewPagerAdapter(supportFragmentManager, fragmentTitleList)
        adapter!!.addFragment(TimeTableFragment())
        adapter!!.addFragment(TimeTableFragment2())
        viewPager = activityTimetableBinding.fragmentContainer
        viewPager!!.setPagingEnabled(false)
        viewPager!!.adapter = adapter
        activityTimetableBinding.tabLayout.setupWithViewPager(viewPager)
        activityTimetableBinding.tabLayout.addOnTabSelectedListener(object :
            OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager!!.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    private val isNetworkConnected: Boolean
        get() {
            val cm = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
            return cm.activeNetworkInfo != null && cm.activeNetworkInfo.isConnected
        }

    //You can replace it with your name
    private val isInternetAvailable: Boolean
        get() = try {
            val ipAddr = InetAddress.getByName("google.com").toString()
            //You can replace it with your name
            ipAddr != ""
        } catch (e: Exception) {
            false
        }

    companion object {
        private const val TAG = "TimeTableActivity"
    }
}