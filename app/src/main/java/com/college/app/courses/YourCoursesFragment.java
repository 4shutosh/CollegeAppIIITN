package com.college.app.courses;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.college.app.R;
import com.college.app.databinding.YourCoursesFragmentBinding;
import com.facebook.shimmer.ShimmerFrameLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

public class YourCoursesFragment extends Fragment {

    public List<Course> yourCourses = new ArrayList<>();
    public YourCoursesFragmentBinding yourCoursesFragmentBinding;
    ShimmerFrameLayout shimmerFrameLayout;
    public YourCoursesAdapter yourCoursesAdapter;
    private static final String TAG = "AllCourseFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        yourCoursesFragmentBinding = YourCoursesFragmentBinding.inflate(inflater, container, false);
        EventBus.getDefault().register(this);
        shimmerFrameLayout = yourCoursesFragmentBinding.shimmerLayout;
        shimmerFrameLayout.startShimmerAnimation();
        setHasOptionsMenu(true);
        attachRecyclerView(getContext());
        shimmerFrameLayout.setVisibility(View.GONE);

        yourCoursesFragmentBinding.swipeLayout.setOnRefreshListener(() -> onRefreshSwipe());
        textDisplay();
        return yourCoursesFragmentBinding.getRoot();
    }

    public void onRefreshSwipe() {
        yourCoursesFragmentBinding.noCoursesText.setVisibility(View.GONE);
        yourCoursesFragmentBinding.coursesRecyclerView.setVisibility(View.GONE);
        shimmerFrameLayout.setVisibility(View.VISIBLE);
        shimmerFrameLayout.startShimmerAnimation();
        yourCoursesAdapter.updateYourCoursesView();
        attachRecyclerView(getContext());
        textDisplay();
    }

    @Override
    public void onResume() {
        Log.d(TAG, "onResume: started your courses fragment");
        onRefreshSwipe();
        super.onResume();
    }

    private void getCourses() {

    }

    @Subscribe
    public void onEvent(Course course) {

        if (getActivity() != null) {
            getActivity().runOnUiThread(() -> {
                if (course.getYour()) {
                    yourCoursesAdapter.addCourse(course);
                    yourCoursesAdapter.notifyDataSetChanged();
//                        attachRecyclerView(getContext());
                    Log.d(TAG, "run: " + yourCourses.size() + course.getCourseCode() + "your COde " + course.getYour());
                } else {
                    Log.d(TAG, "run: " + yourCourses.size() + course.getCourseCode() + " your COde " + course.getYour());
                    yourCoursesAdapter.removeItem(course);
                    yourCoursesAdapter.notifyDataSetChanged();
                }
                textDisplay();
            });

        }

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        Log.d(TAG, "onCreateOptionsMenu: menu formed");
        inflater.inflate(R.menu.your_courses_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    void recyclerLayout() {
        yourCoursesAdapter = new YourCoursesAdapter(getContext(), yourCourses);
        yourCoursesAdapter.notifyDataSetChanged();
        if (!yourCourses.isEmpty()) {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
            linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
            yourCoursesFragmentBinding.coursesRecyclerView.setLayoutManager(linearLayoutManager);
            yourCoursesFragmentBinding.coursesRecyclerView.setAdapter(yourCoursesAdapter);
            shimmerFrameLayout.stopShimmerAnimation();
            shimmerFrameLayout.setVisibility(View.GONE);
            yourCoursesFragmentBinding.noCoursesText.setVisibility(View.GONE);
            yourCoursesFragmentBinding.coursesRecyclerView.setVisibility(View.VISIBLE);
            yourCoursesFragmentBinding.swipeLayout.setRefreshing(false);
        } else {
            shimmerFrameLayout.setVisibility(View.GONE);
            yourCoursesFragmentBinding.noCoursesText.setVisibility(View.VISIBLE);
        }
    }

    public void attachRecyclerView(Context context) {
        ////////////////////////Recycler View
        yourCoursesAdapter = new YourCoursesAdapter(context, yourCourses);
        yourCoursesAdapter.updateYourCoursesView();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        yourCoursesFragmentBinding.coursesRecyclerView.setLayoutManager(linearLayoutManager);
        yourCoursesFragmentBinding.coursesRecyclerView.setAdapter(yourCoursesAdapter);
        shimmerFrameLayout.stopShimmerAnimation();
        shimmerFrameLayout.setVisibility(View.GONE);
        yourCoursesFragmentBinding.noCoursesText.setVisibility(View.GONE);
        yourCoursesFragmentBinding.coursesRecyclerView.setVisibility(View.VISIBLE);
        Log.d(TAG, "attachRecyclerView: " + yourCoursesAdapter.getItemCount());
        yourCoursesFragmentBinding.swipeLayout.setRefreshing(false);
    }

    public void textDisplay() {
        if (yourCoursesAdapter.isBoxEmpty()) {
            Log.d(TAG, "textDisplay: box empty");
            yourCoursesFragmentBinding.shimmerLayout.setVisibility(View.GONE);
            yourCoursesFragmentBinding.coursesRecyclerView.setVisibility(View.GONE);
            yourCoursesFragmentBinding.noCoursesText.setVisibility(View.VISIBLE);
        }
    }
}
