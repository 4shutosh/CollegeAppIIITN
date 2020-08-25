package com.college.app.courses;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.college.app.databinding.CourseItemBinding;

import java.util.List;

public class CoursesAdapter extends RecyclerView.Adapter<CoursesAdapter.Item> {

    private List<Course> list;
    private static final String TAG = "CoursesAdapter";
    Context context;

    public CoursesAdapter(Context context, List<Course> courseList) {
        this.context = context;
        this.list = courseList;
    }

    @NonNull
    @Override
    public Item onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        CourseItemBinding itemBinding = CourseItemBinding.inflate(layoutInflater, parent, false);
        return new Item(itemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull Item holder, int position) {
        holder.courseItemBinding.courseName.setText(list.get(position).getCourseName());
        holder.courseItemBinding.courseCode.setText(list.get(position).getCourseCode());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Item extends RecyclerView.ViewHolder implements View.OnClickListener {

        CourseItemBinding courseItemBinding;

        public Item(CourseItemBinding courseItemBinding) {
            super(courseItemBinding.getRoot());
            this.courseItemBinding = courseItemBinding;
            courseItemBinding.courseCard.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Log.d(TAG, "onClick: " + list.get(getAdapterPosition()).getCourseCode() + "your status" + list.get(getAdapterPosition()).getYour());
            Intent intent = new Intent(context, courseActivity.class);
            intent.putExtra("courseCode", list.get(getAdapterPosition()).getCourseCode());
            intent.putExtra("your", list.get(getAdapterPosition()).getYour());
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }
}
