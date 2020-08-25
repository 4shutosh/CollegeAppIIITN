package com.college.app.attendance;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.college.app.adapter.ViewPagerAdapter;
import com.college.app.databinding.ActivityTimetableBinding;
import com.college.app.fragment.TimeTableFragment;
import com.college.app.fragment.TimeTableFragment2;
import com.college.app.utils.CollegeAppViewPager;
import com.google.android.material.tabs.TabLayout;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TimeTableActivity extends AppCompatActivity {

    ActivityTimetableBinding activityTimetableBinding;
    private static final String TAG = "TimeTableActivity";
    ViewPagerAdapter adapter;
    CollegeAppViewPager viewPager;
    String timeTableLinkCSE = "CSE", timeTableLinkECE = "ECE";
    private final List<String> fragmentTitleList = new ArrayList<String>(Arrays.asList("ECE", "CSE"));


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityTimetableBinding = ActivityTimetableBinding.inflate(getLayoutInflater());
        setContentView(activityTimetableBinding.getRoot());
        setSupportActionBar(activityTimetableBinding.toolbar);
        getSupportActionBar().setTitle("TimeTable");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (!isNetworkConnected()) {
            if (!isInternetAvailable()) {
                Toast.makeText(this, "Please connect to internet", Toast.LENGTH_LONG).show();
            }
        }
        adapter = new ViewPagerAdapter(getSupportFragmentManager(), fragmentTitleList);

        adapter.addFragment(new TimeTableFragment());
        adapter.addFragment(new TimeTableFragment2());

        viewPager = activityTimetableBinding.fragmentContainer;
        viewPager.setPagingEnabled(false);
        viewPager.setAdapter(adapter);
        activityTimetableBinding.tabLayout.setupWithViewPager(viewPager);
        activityTimetableBinding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
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
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    public boolean isInternetAvailable() {
        try {
            InetAddress ipAddr = InetAddress.getByName("google.com");
            //You can replace it with your name
            return !ipAddr.equals("");

        } catch (Exception e) {
            return false;
        }
    }
}