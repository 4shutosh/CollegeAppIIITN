package com.college.app.courses;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.college.app.databinding.AllCourseFragmentBinding;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class AllCourseFragment extends Fragment {

    List<Course> courseList;
    AllCourseFragmentBinding allCourseFragmentBinding;
    ShimmerFrameLayout shimmerFrameLayout;
    private static final String TAG = "AllCourseFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        allCourseFragmentBinding = AllCourseFragmentBinding.inflate(inflater, container, false);
        shimmerFrameLayout = allCourseFragmentBinding.shimmerLayout;
        shimmerFrameLayout.startShimmerAnimation();
        getCourses();
        allCourseFragmentBinding.swipeLayout.setOnRefreshListener(() -> {

            allCourseFragmentBinding.coursesRecyclerView.setVisibility(View.GONE);
            shimmerFrameLayout.setVisibility(View.VISIBLE);
            shimmerFrameLayout.startShimmerAnimation();
            new Timer().schedule(
                    new TimerTask() {
                        @Override
                        public void run() {
                            // you cannot touch the UI from another thread. This thread now calls a function on the main thread
                            getCourses();
                        }
                    }, 1000);
        });
        LinearLayoutManager lm = (LinearLayoutManager) allCourseFragmentBinding.coursesRecyclerView.getLayoutManager();
        allCourseFragmentBinding.coursesRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                int topRowVerticalPosition =
                        recyclerView.getChildCount() == 0 ? 0 : recyclerView.getChildAt(0).getTop();
                allCourseFragmentBinding.swipeLayout.setEnabled(topRowVerticalPosition >= 0);
            }
        });
        return allCourseFragmentBinding.getRoot();
    }

    private void getCourses() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Courses");
        courseList = new ArrayList<>();
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Course course = data.getValue(Course.class);
                    course.setCourseCode(course.getCourseCode());
                    course.setCourseName(course.getCourseName());
                    course.setId(course.getId());
                    course.setYour(false);
                    courseList.add(course);
                }
                Log.d(TAG, "number of courses found: " + courseList.size());
                recyclerLayout();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });

    }

    void recyclerLayout() {
        CoursesAdapter coursesAdapter = new CoursesAdapter(getContext(), courseList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        allCourseFragmentBinding.coursesRecyclerView.setLayoutManager(linearLayoutManager);
        allCourseFragmentBinding.coursesRecyclerView.setAdapter(coursesAdapter);
        shimmerFrameLayout.stopShimmerAnimation();
        shimmerFrameLayout.setVisibility(View.GONE);
        allCourseFragmentBinding.coursesRecyclerView.setVisibility(View.VISIBLE);
        allCourseFragmentBinding.swipeLayout.setRefreshing(false);
    }
}
