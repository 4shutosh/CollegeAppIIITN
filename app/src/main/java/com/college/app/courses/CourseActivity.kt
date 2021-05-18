package com.college.app.courses

import android.content.Intent
import android.content.res.ColorStateList
import android.content.res.Configuration
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.transition.Slide
import android.util.Log
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.view.animation.DecelerateInterpolator
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.college.app.R
import com.college.app.databinding.ActivityCourseBinding
import com.college.app.utils.AppUtils
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import org.greenrobot.eventbus.EventBus

class CourseActivity : AppCompatActivity() {
    private lateinit var activityCourseBinding: ActivityCourseBinding
    var courseMain: Course? = null
    var courseCodeFromIntent: String? = null
    var your: Boolean? = null
    var id: Long = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityCourseBinding = ActivityCourseBinding.inflate(
            layoutInflater
        )
        setContentView(activityCourseBinding.root)
        setAnimation()
        // getting intent information
        your = intent.extras!!.getBoolean("your")
        courseCodeFromIntent = intent.extras!!.getString("courseCode")
        Log.d(TAG, "onCreate: course code obtained" + courseCodeFromIntent + "your status " + your)
        //        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        activityCourseBinding.collapsingToolbarLayout.title = courseCodeFromIntent
        activityCourseBinding.toolbar.title = courseCodeFromIntent
        activityCourseBinding.scrollLayout.yourCourse.setOnClickListener { v: View? -> checkBoxClicked() }
        if (AppUtils.getScreenWidthDp(this) >= 600) {
            val collapsing_toolbar_layout =
                findViewById<CollapsingToolbarLayout>(R.id.collapsing_toolbar_layout)
            collapsing_toolbar_layout.setExpandedTitleTextColor(ColorStateList.valueOf(Color.TRANSPARENT))
        }
        activityCourseBinding.collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBar)
        activityCourseBinding.collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar)
        courseFromFB
    }

    fun setAnimation() {
        val slide = Slide()
        slide.slideEdge = Gravity.START
        slide.duration = 400
        slide.interpolator = DecelerateInterpolator()
        window.exitTransition = slide
        window.enterTransition = slide
    }

    private fun checkBoxClicked() {
        if (activityCourseBinding.scrollLayout.yourCourse.isChecked) {
            Log.d(TAG, "checkBoxClicked: true event pushed")
            activityCourseBinding.scrollLayout.yesNo.setText(R.string.yes)
            val c = Course(0, courseMain?.courseName, courseMain?.courseCode, true)
            EventBus.getDefault().post(c)
        } else if (!activityCourseBinding.scrollLayout.yourCourse.isChecked) {
            Log.d(TAG, "checkBoxClicked: false event pushed")
            activityCourseBinding.scrollLayout.yesNo.setText(R.string.no)
            val c = Course(0, courseMain?.courseName, courseMain?.courseCode, false)
            EventBus.getDefault().post(c)
        }
    }

    override fun onResume() {
        super.onResume()
        val configuration = resources.configuration
        if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
        } else {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private val courseFromFB: Unit
        get() {
            val myRef = FirebaseDatabase.getInstance().getReference("Courses")
            myRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (data in dataSnapshot.children) {
                        val course = data.getValue(Course::class.java)
                        if (course != null) {
                            if (course.courseCode == courseCodeFromIntent) {
                                setUpUi(course)
                                courseMain = course
                            }
                        }
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {}
            })
        }

    fun setUpUi(course: Course) {
        val image_scrolling_top = activityCourseBinding.imageScrollingTop
        Picasso.get()
            .load(course.photoUrl)
            .placeholder(R.drawable.material_design_3)
            .fit()
            .into(image_scrolling_top)
        Log.d(TAG, "setUpUi: classRoomLink" + course.classroomLink)
        activityCourseBinding.scrollLayout.classRoomButton.setOnClickListener { v: View? ->
            if (course.classroomLink?.isNotEmpty() == true) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(course.classroomLink))
                startActivity(intent)
            } else {
                Toast.makeText(this, "Oops, Unavailable!", Toast.LENGTH_SHORT).show()
            }
        }
        activityCourseBinding.scrollLayout.videoPlaylistButton.setOnClickListener { v: View? ->
            if (course.videoPlaylistLink!!.isNotEmpty()) {
                val intent =
                    Intent(Intent.ACTION_VIEW, Uri.parse(course.videoPlaylistLink))
                startActivity(intent)
            } else {
                Toast.makeText(this, "Oops, Unavailable!", Toast.LENGTH_SHORT).show()
            }
        }
        activityCourseBinding.scrollLayout.booksDriveButton.setOnClickListener {
            if (course.driveLink!!.isNotEmpty()) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(course.driveLink))
                startActivity(intent)
            } else {
                Toast.makeText(this, "Oops, Unavailable!", Toast.LENGTH_SHORT).show()
            }
        }
        activityCourseBinding.scrollLayout.courseName.text = course.courseName
        activityCourseBinding.scrollLayout.courseCode.text = course.courseAbout
        activityCourseBinding.scrollLayout.courseAbout.text = course.courseAbout
        activityCourseBinding.scrollLayout.courseFacultyName.text = course.faculty
        activityCourseBinding.scrollLayout.facultyContactTextView.text =
            course.facultyContact
        if (your!!) {
            activityCourseBinding.scrollLayout.yourCourse.isChecked = true
            activityCourseBinding.scrollLayout.yesNo.setText(R.string.yes)
        }
    }

    companion object {
        private const val TAG = "courseActivity"
    }
}