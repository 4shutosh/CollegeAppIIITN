package com.college.app.courses;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.college.app.R;
import com.college.app.adapter.ViewPagerAdapter;
import com.college.app.databinding.ActivityCoursesBinding;
import com.college.app.utils.CollegeAppViewPager;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CoursesActivity extends AppCompatActivity {

    private static final String TAG = "CoursesActivity";
    ViewPagerAdapter adapter;
    CollegeAppViewPager viewPager;
    ConstraintLayout bsYourCoursesInfo;
    BottomSheetBehavior bottomSheetBehavior;
    ActivityCoursesBinding activityCoursesBinding;
    private final List<String> mFragmentTitleList = new ArrayList<String>(Arrays.asList("All", "Your"));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityCoursesBinding = ActivityCoursesBinding.inflate(getLayoutInflater());
        setContentView(activityCoursesBinding.getRoot());

        setSupportActionBar(activityCoursesBinding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.courses));

        adapter = new ViewPagerAdapter(getSupportFragmentManager(), mFragmentTitleList);
        adapter.addFragment(new AllCourseFragment());
        adapter.addFragment(new YourCoursesFragment());
        viewPager = activityCoursesBinding.courseFragment;
        viewPager.setPagingEnabled(true);
        viewPager.setAdapter(adapter);
        activityCoursesBinding.tabLayout.setupWithViewPager(viewPager);
        activityCoursesBinding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
//        adapter.addFragment(new NotificationsFragment());
        bsYourCoursesInfo = findViewById(R.id.bottom_sheet_yourCourses);
        bottomSheetBehavior = BottomSheetBehavior.from(bsYourCoursesInfo);
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.infoYourCourses:
                bottomSheetInfo();
                return true;
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void bottomSheetInfo() {
        if (bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        } else {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
    }
}