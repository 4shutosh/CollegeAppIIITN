package com.college.app.courses

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.college.app.databinding.CourseItemBinding

class CoursesAdapter(var context: Context?, private val list: List<Course>) :
    RecyclerView.Adapter<CoursesAdapter.Item>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Item {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemBinding = CourseItemBinding.inflate(layoutInflater, parent, false)
        return Item(itemBinding)
    }

    override fun onBindViewHolder(holder: Item, position: Int) {
        holder.courseItemBinding.courseName.text = list[position].courseName
        holder.courseItemBinding.courseCode.text = list[position].courseCode
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class Item(var courseItemBinding: CourseItemBinding) : RecyclerView.ViewHolder(
        courseItemBinding.root
    ), View.OnClickListener {
        override fun onClick(v: View) {
            Log.d(
                TAG,
                "onClick: " + list[adapterPosition].courseCode + "your status" + list[adapterPosition].your
            )
            val intent = Intent(context, CourseActivity::class.java)
            intent.putExtra("courseCode", list[adapterPosition].courseCode)
            intent.putExtra("your", list[adapterPosition].your)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context!!.startActivity(intent)
        }

        init {
            courseItemBinding.courseCard.setOnClickListener(this)
        }
    }

    companion object {
        private const val TAG = "CoursesAdapter"
    }
}