package com.college.app.courses;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.college.app.AppClass;
import com.college.app.databinding.CourseItemBinding;

import java.util.List;

import io.objectbox.Box;
import io.objectbox.BoxStore;
import io.objectbox.exception.UniqueViolationException;

import static android.content.ContentValues.TAG;

public class YourCoursesAdapter extends RecyclerView.Adapter<YourCoursesAdapter.Item> implements View.OnCreateContextMenuListener {

    private List<Course> courseList;
    private BoxStore boxStore;
    private Box<Course> courseBox;
    Context context;

    public YourCoursesAdapter(Context context, List<Course> courseList) {
        this.context = context;
        this.courseList = courseList;
        boxStore = ((AppClass) ((CoursesActivity) context).getApplication()).getBoxStore();
        courseBox = boxStore.boxFor(Course.class);
    }

    @NonNull
    @Override
    public Item onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        CourseItemBinding itemBinding = CourseItemBinding.inflate(layoutInflater, parent, false);
        return new YourCoursesAdapter.Item(itemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull YourCoursesAdapter.Item holder, int position) {
        holder.courseItemBinding.courseName.setText(courseList.get(position).getCourseName());
        holder.courseItemBinding.courseCode.setText(courseList.get(position).getCourseCode());

    }

    @Override
    public int getItemCount() {
        return courseList.size();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

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
            Log.d(TAG, "onClick: " + courseList.get(getAdapterPosition()).getCourseCode());
            Intent intent = new Intent(context, courseActivity.class);
            intent.putExtra("courseCode", courseList.get(getAdapterPosition()).getCourseCode());
            intent.putExtra("courseName", courseList.get(getAdapterPosition()).getCourseName());
            intent.putExtra("your", courseList.get(getAdapterPosition()).getYour());
            intent.putExtra("id", Long.valueOf(getAdapterPosition()));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }

    public void addCourse(Course course) {
        // remove duplicate addition
        Log.d(TAG, "onEvent: add course reached with id " + course.getId() + "code: " + course.getCourseCode());
        if (!courseBox.getAll().contains(course.getCourseCode())) {
            try {
                courseList.add(0, course);
                courseBox.put(course);
                updateYourCoursesView();
                notifyItemInserted(0);
                Toast.makeText(context, "Added to your courses", Toast.LENGTH_SHORT).show();
            } catch (UniqueViolationException e) {
                Log.e(TAG, "addCourse: ", e);
                Toast.makeText(context, "Already in your courses", Toast.LENGTH_SHORT).show();

            }
        }

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

    public void updateYourCoursesView() {
        List<Course> nList = courseBox.query().build().find();
        this.setCourses(nList);
        notifyDataSetChanged();
    }

    private void setCourses(List<Course> list) {
        courseList = list;
        notifyDataSetChanged();
    }

    public void removeItem(Course course) {
        // some times id is not
        Log.d(TAG, "onEvent: remove course reached + with id " + course.getId());

        Course c = getCourseByCode(course.getCourseCode());
        if (c != null) {
            Log.d(TAG, "onEvent: deletion started for id " + course.getId());
            courseBox.remove(c);
            Toast.makeText(context, "Removed from your courses", Toast.LENGTH_SHORT).show();
        }
        courseList.remove(course);

        updateYourCoursesView();
//        notifyItemRemoved((int) course.getId());
    }

    private Course getCourseByCode(String code) {
        return courseBox.query().equal(Course_.courseCode, code).build().findUnique();
    }

    public boolean isBoxEmpty() {
        return courseBox.isEmpty();
    }
}
