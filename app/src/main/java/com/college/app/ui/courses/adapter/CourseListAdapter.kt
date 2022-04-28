package com.college.app.ui.courses.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.college.app.databinding.ListItemCourseBinding
import com.college.app.utils.extensions.executeAfter


class CourseListAdapter constructor(
    private val itemClickListener: CourseListItemClickListener,
) :
    ListAdapter<CourseListAdapter.CourseViewState, CourseListAdapter.CourseListViewHolder>(
        CourseListDiffCallback) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = CourseListViewHolder(
        ListItemCourseBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: CourseListViewHolder, position: Int) {
        val item = getItem(position)
        holder.binding.executeAfter {
            viewState = item
            llRoot.setOnClickListener {
                itemClickListener.courseItemClicked(item)
            }
            checkboxStar.setOnCheckedChangeListener { _, boolean ->
                itemClickListener.courseEnrollStateClicked(boolean, item)
            }
        }
    }

    interface CourseListItemClickListener {
        fun courseItemClicked(item: CourseViewState)
        fun courseEnrollStateClicked(enroll: Boolean, item: CourseViewState)
    }

    inner class CourseListViewHolder(
        val binding: ListItemCourseBinding,
    ) : RecyclerView.ViewHolder(binding.root)

    data class CourseViewState(
        val id: Long,
        val courseId: String,
        val name: String,
        val code: String,
        val description: String,
        val facultyName: String,
        var userEnrolled: Boolean,
    )

    object CourseListDiffCallback : DiffUtil.ItemCallback<CourseViewState>() {
        override fun areItemsTheSame(oldItem: CourseViewState, newItem: CourseViewState) =
            (oldItem.name == newItem.name && oldItem.code == newItem.code && oldItem.userEnrolled == newItem.userEnrolled)

        override fun areContentsTheSame(
            oldItem: CourseViewState,
            newItem: CourseViewState,
        ) = (oldItem.name == newItem.name && oldItem.code == newItem.code)
    }

}