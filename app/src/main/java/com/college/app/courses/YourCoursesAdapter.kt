package com.college.app.courses

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.ContextMenu
import android.view.ContextMenu.ContextMenuInfo
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnCreateContextMenuListener
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.college.app.AppClass
import com.college.app.databinding.CourseItemBinding
import io.objectbox.Box
import io.objectbox.BoxStore
import io.objectbox.exception.UniqueViolationException

class YourCoursesAdapter(var context: Context?, private var courseList: MutableList<Course>) :
    RecyclerView.Adapter<YourCoursesAdapter.Item>(), OnCreateContextMenuListener {
    private val boxStore: BoxStore?
    private val courseBox: Box<Course>
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Item {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemBinding = CourseItemBinding.inflate(layoutInflater, parent, false)
        return Item(itemBinding)
    }

    override fun onBindViewHolder(holder: Item, position: Int) {
        holder.courseItemBinding.courseName.text = courseList[position].courseName
        holder.courseItemBinding.courseCode.text = courseList[position].courseCode
    }

    override fun getItemCount(): Int {
        return courseList.size
    }

    override fun onCreateContextMenu(menu: ContextMenu, v: View, menuInfo: ContextMenuInfo) {}
    inner class Item(var courseItemBinding: CourseItemBinding) : RecyclerView.ViewHolder(
        courseItemBinding.root
    ), View.OnClickListener {
        override fun onClick(v: View) {
            Log.d(ContentValues.TAG, "onClick: " + courseList[adapterPosition].courseCode)
            val intent = Intent(context, CourseActivity::class.java)
            intent.putExtra("courseCode", courseList[adapterPosition].courseCode)
            intent.putExtra("courseName", courseList[adapterPosition].courseName)
            intent.putExtra("your", courseList[adapterPosition].your)
            intent.putExtra("id", java.lang.Long.valueOf(adapterPosition.toLong()))
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context!!.startActivity(intent)
        }

        init {
            courseItemBinding.courseCard.setOnClickListener(this)
        }
    }

    fun addCourse(course: Course) {
        // remove duplicate addition
        Log.d(
            ContentValues.TAG,
            "onEvent: add course reached with id " + course.id + "code: " + course.courseCode
        )
//        if (!courseBox.all.contains(course.courseCode)) { fixme
            try {
                courseList.add(0, course)
                courseBox.put(course)
                updateYourCoursesView()
                notifyItemInserted(0)
                Toast.makeText(context, "Added to your courses", Toast.LENGTH_SHORT).show()
            } catch (e: UniqueViolationException) {
                Log.e(ContentValues.TAG, "addCourse: ", e)
                Toast.makeText(context, "Already in your courses", Toast.LENGTH_SHORT).show()
            }
//        }

//        try {
//            courseList.add(0, course);
//            courseBox.put(course);
//            updateYourCoursesView();
//            notifyItemInserted(0);
//            Toast.makeText(context, "Added to your courses", Toast.LENGTH_SHORT).show();
//        } catch (IllegalArgumentException e) {
//            Toast.makeText(context, "Already in your courses", Toast.LENGTH_SHORT).show();
//        }
    }

    fun updateYourCoursesView() {
        val nList = courseBox.query().build().find()
        setCourses(nList)
        notifyDataSetChanged()
    }

    private fun setCourses(list: MutableList<Course>) {
        courseList = list
        notifyDataSetChanged()
    }

    fun removeItem(course: Course) {
        // some times id is not
        Log.d(ContentValues.TAG, "onEvent: remove course reached + with id " + course.id)
        val c = getCourseByCode(course.courseCode)
        if (c != null) {
            Log.d(ContentValues.TAG, "onEvent: deletion started for id " + course.id)
            courseBox.remove(c)
            Toast.makeText(context, "Removed from your courses", Toast.LENGTH_SHORT).show()
        }
        courseList.remove(course)
        updateYourCoursesView()
        //        notifyItemRemoved((int) course.getId());
    }

    private fun getCourseByCode(code: String?): Course? {
        return courseBox.query().equal(Course_.courseCode, code).build().findUnique()
    }

    val isBoxEmpty: Boolean
        get() = courseBox.isEmpty

    init {
        boxStore = ((context as CoursesActivity?)!!.application as AppClass).boxStore
        courseBox = boxStore!!.boxFor(Course::class.java)
    }
}