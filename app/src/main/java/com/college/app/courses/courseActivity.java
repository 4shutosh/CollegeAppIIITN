package com.college.app.courses;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.transition.Slide;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.college.app.R;
import com.college.app.databinding.ActivityCourseBinding;
import com.college.app.utils.AppUtils;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;

public class courseActivity extends AppCompatActivity {
    ActivityCourseBinding activityCourseBinding;
    private static final String TAG = "courseActivity";
    Course courseMain;
    String courseCodeFromIntent;
    Boolean your;
    long id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityCourseBinding = ActivityCourseBinding.inflate(getLayoutInflater());
        setContentView(activityCourseBinding.getRoot());
        setAnimation();
        // getting intent information
        your = getIntent().getExtras().getBoolean("your");
        courseCodeFromIntent = getIntent().getExtras().getString("courseCode");
        Log.d(TAG, "onCreate: course code obtained" + courseCodeFromIntent + "your status " + your);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        activityCourseBinding.collapsingToolbarLayout.setTitle(courseCodeFromIntent);
        activityCourseBinding.toolbar.setTitle(courseCodeFromIntent);
        activityCourseBinding.scrollLayout.yourCourse.setOnClickListener(v -> checkBoxClicked());

        if (AppUtils.getScreenWidthDp(this) >= 600) {
            CollapsingToolbarLayout collapsing_toolbar_layout = findViewById(R.id.collapsing_toolbar_layout);
            collapsing_toolbar_layout.setExpandedTitleTextColor(ColorStateList.valueOf(Color.TRANSPARENT));
        }
        activityCourseBinding.collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
        activityCourseBinding.collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);
        getCourseFromFB();
    }


    public void setAnimation() {
        Slide slide = new Slide();
        slide.setSlideEdge(Gravity.START);
        slide.setDuration(400);
        slide.setInterpolator(new DecelerateInterpolator());
        getWindow().setExitTransition(slide);
        getWindow().setEnterTransition(slide);
    }

    public void checkBoxClicked() {
        if (activityCourseBinding.scrollLayout.yourCourse.isChecked()) {
            Log.d(TAG, "checkBoxClicked: true event pushed");
            activityCourseBinding.scrollLayout.yesNo.setText(R.string.yes);
            Course c = new Course(0, courseMain.getCourseName(), courseMain.getCourseCode(), true);
            EventBus.getDefault().post(c);
        } else if (!activityCourseBinding.scrollLayout.yourCourse.isChecked()) {
            Log.d(TAG, "checkBoxClicked: false event pushed");
            activityCourseBinding.scrollLayout.yesNo.setText(R.string.no);
            Course c = new Course(0, courseMain.getCourseName(), courseMain.getCourseCode(), false);
            EventBus.getDefault().post(c);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Configuration configuration = getResources().getConfiguration();
        if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        } else {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    public void getCourseFromFB() {
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("Courses");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Course course = data.getValue(Course.class);
                    if (course != null) {
                        if (course.getCourseCode().equals(courseCodeFromIntent)) {
                            course.setYour(your);
                            course.setCourseCode(course.getCourseCode());
                            course.setCourseName(course.getCourseName());
                            course.setFaculty(course.getFaculty());
                            course.setFacultyContact(course.getFacultyContact());
                            course.setCourseAbout(course.getCourseAbout());
                            course.setPhotoUrl(course.getPhotoUrl());
                            course.setDriveLink(course.getDriveLink());
                            courseMain = course;
                        }
                    }
                }
                Log.d(TAG, "course details fetched " + courseMain.getCourseName() + " " + courseMain.getYour());
                if (courseMain != null) {
                    setUpUi();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
    }

    public void setUpUi() {
        ImageView image_scrolling_top = activityCourseBinding.imageScrollingTop;
        Picasso.get()
                .load(courseMain.getPhotoUrl())
                .placeholder(R.drawable.material_design_3)
                .fit()
                .into(image_scrolling_top);

        Log.d(TAG, "setUpUi: classRoomLink" + courseMain.getClassroomLink());
        activityCourseBinding.scrollLayout.classRoomButton.setOnClickListener(v -> {
            if (!courseMain.getClassroomLink().isEmpty()) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(courseMain.getClassroomLink()));
                startActivity(intent);
            } else {
                Toast.makeText(this, "Oops, Unavailable!", Toast.LENGTH_SHORT).show();
            }
        });

        activityCourseBinding.scrollLayout.videoPlaylistButton.setOnClickListener(v -> {
            if (!courseMain.getVideoPlaylistLink().isEmpty()) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(courseMain.getVideoPlaylistLink()));
                startActivity(intent);
            } else {
                Toast.makeText(this, "Oops, Unavailable!", Toast.LENGTH_SHORT).show();
            }
        });

        activityCourseBinding.scrollLayout.booksDriveButton.setOnClickListener(v -> {
            if (!courseMain.getDriveLink().isEmpty()) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(courseMain.getDriveLink()));
                startActivity(intent);
            } else {
                Toast.makeText(this, "Oops, Unavailable!", Toast.LENGTH_SHORT).show();
            }
        });

        activityCourseBinding.scrollLayout.courseName.setText(courseMain.getCourseName());
        activityCourseBinding.scrollLayout.courseCode.setText(courseMain.getCourseCode());
        activityCourseBinding.scrollLayout.courseAbout.setText(courseMain.getCourseAbout());
        activityCourseBinding.scrollLayout.courseFacultyName.setText(courseMain.getFaculty());
        activityCourseBinding.scrollLayout.facultyContactTextView.setText(courseMain.getFacultyContact());

        if (your) {
            activityCourseBinding.scrollLayout.yourCourse.setChecked(true);
            activityCourseBinding.scrollLayout.yesNo.setText(R.string.yes);
        }
    }
}
