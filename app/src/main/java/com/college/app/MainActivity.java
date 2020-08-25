package com.college.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import com.college.app.Notification.Notification;
import com.college.app.Notification.NotificationAdapter;
import com.college.app.Notification.NotificationsFragment;
import com.college.app.adapter.ViewPagerAdapter;
import com.college.app.courses.CoursesActivity;
import com.college.app.databinding.ActivityMainBinding;
import com.college.app.fragment.EventsFragment;
import com.college.app.fragment.HolderActivity;
import com.college.app.fragment.HomeFragment;
import com.college.app.fragment.ServiceFragment;
import com.college.app.fragment.SettingsFragment;
import com.college.app.profile.Profile;
import com.college.app.todo.TodoFragment;
import com.college.app.utils.CollegeAppViewPager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.List;

import io.objectbox.Box;
import io.objectbox.BoxStore;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding activityMainBinding;
    BottomNavigationView bottomNavigationView;
    private FirebaseAuth auth;//
    CollegeAppViewPager viewPager;
    ViewPagerAdapter adapter;
    ConstraintLayout bsNotifications;
    BottomSheetBehavior bottomSheetBehaviorNotifications;
    MenuItem menuItem;
    BoxStore profileBoxStore;
    Box<Profile> profileBox;
    Bitmap imageBitmap;
    TextView username;
    TextView emailNav;
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String SKIP_PROFILE_STEP = "SkipProfileStep";
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: ");
        super.onCreate(savedInstanceState);
        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = activityMainBinding.getRoot();

//        applyTheme();
        setContentView(view);

        setSupportActionBar(activityMainBinding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        NavigationViewDrawer(); // navigationDrawer + toolbar

//        allFirebase();

        viewPager = activityMainBinding.fragmentContainer;
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new HomeFragment());
        adapter.addFragment(new NotificationsFragment());
        adapter.addFragment(new TodoFragment());
        viewPager.setPagingEnabled(false);
        viewPager.setAdapter(adapter);

        bottomNavigationView = activityMainBinding.bottomNavigation;
        bottomNavigationView.setOnNavigationItemSelectedListener(bottomNavigationItemSelectedListener); // bottomNavigation View
        // fragment controller
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (menuItem != null) {
                    menuItem.setChecked(false);
                } else {
                    bottomNavigationView.getMenu().getItem(0).setChecked(false);
                }
                bottomNavigationView.getMenu().getItem(position).setChecked(true);
                menuItem = bottomNavigationView.getMenu().getItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
//        getSupportFragmentManage().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
        String extraFragment = "a";
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            extraFragment = extras.getString("fragmentInformation");
            FragmentManager fm = getSupportFragmentManager();
            for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                fm.popBackStack();
            }
            switch (extraFragment) {
                case "1":
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
                    break;
                case "2":
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new NotificationsFragment()).commit();
                    break;
                case "3":
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new TodoFragment()).commit();
                    break;
                default:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
                    break;
            }
        }
        bsNotifications = findViewById(R.id.bottom_sheet_notifications);
        bottomSheetBehaviorNotifications = BottomSheetBehavior.from(bsNotifications);
        updateUserDetails();
        handleFCMData();
    }

    private void handleFCMData() {
        FirebaseMessaging.getInstance().subscribeToTopic("all");
        Intent intent = getIntent();
        if (intent != null && intent.getExtras() != null) {
            Log.d(TAG, "handleFCMData: data found bruh");
            Toast.makeText(this, "Data found", Toast.LENGTH_SHORT).show();
            Bundle extras = intent.getExtras();
            String title = extras.getString("Title");
            String body = extras.getString("Message");
            NotificationAdapter.notificationAdapterBox.put(new Notification(0, title, body));
            NotificationsFragment.notificationAdapter.updateNotificationView();
        } else {
            Log.d(TAG, "handleFCMData: no data found");
//            Toast.makeText(this, "no data found", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        if (activityMainBinding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            activityMainBinding.drawerLayout.closeDrawer(GravityCompat.START);
        } else if (bottomSheetBehaviorNotifications.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            bottomSheetBehaviorNotifications.setState(BottomSheetBehavior.STATE_HIDDEN);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume: ");
        closeDrawer();
        super.onResume();

    }

    public void closeDrawer() {
        if (activityMainBinding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            activityMainBinding.drawerLayout.closeDrawer(GravityCompat.START);
        }
        activityMainBinding.navigationView.setCheckedItem(R.id.homeDrawer);
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String message = intent.getStringExtra("body");
        }
    };


    private BottomNavigationView.OnNavigationItemSelectedListener bottomNavigationItemSelectedListener =
            item -> {
//                Fragment selectedFragment = null;

                switch (item.getItemId()) {
                    case R.id.bottom_navigation_home:
//                        selectedFragment = new HomeFragment();
                        viewPager.setCurrentItem(0);
                        activityMainBinding.toolbar.setTitle(getText(R.string.home));
                        break;

                    case R.id.bottom_navigation_notifications:
//                        selectedFragment = new NotificationsFragment();
                        viewPager.setCurrentItem(1);
                        activityMainBinding.toolbar.setTitle(getText(R.string.notifications));
                        break;

                    case R.id.bottom_navigation_todo:
//                        selectedFragment = new TodoFragment();
                        viewPager.setCurrentItem(2);
                        activityMainBinding.toolbar.setTitle(getText(R.string.todo));
                        break;
                }
//                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
                return false;
            };

    private NavigationView.OnNavigationItemSelectedListener drawerNavigationItemSelectedListener =
            item -> {
                switch (item.getItemId()) {
                    case R.id.exitApp:
                        finish();
                        break;
                    case R.id.logOut:
                        signOut();
                        break;
                    case R.id.settings:
                        HolderActivity.startActivity(this, SettingsFragment.class, null, null);
                        break;
                    case R.id.coursesDrawer:
                        Intent intent = new Intent(this, CoursesActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.eventsDrawer:
                        HolderActivity.startActivity(this, EventsFragment.class, null, null);
                        break;

                    case R.id.servicesDrawer:
                        HolderActivity.startActivity(this, ServiceFragment.class, null, null);
                        break;
                }
                closeDrawer();
                return true;
            };


    private void updateUserDetails() {
        profileBoxStore = ((AppClass) ((MainActivity) this).getApplication()).getBoxStore();
        profileBox = profileBoxStore.boxFor(Profile.class);
        List<Profile> list = profileBox.getAll();
        Profile profile = list.get(0);
        Log.d(TAG, "updateUserDetails: " + profile.getName());
        username.setText(profile.getName());
        emailNav.setText(profile.getEmail());
        if (profile.getImageBitmapEncoded() != null) {
            imageBitmap = decodeImageBitmap(profile.getImageBitmapEncoded());
            View view = activityMainBinding.navigationView.getHeaderView(0);
            ImageView profileImage = view.findViewById(R.id.navigation_drawer_userImage);
            profileImage.setImageBitmap(imageBitmap);
        }

        // use
    }

    Uri getImageUriFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        String path = MediaStore.Images.Media.insertImage(this.getContentResolver(), bitmap, "Title", null);
        return Uri.parse(path.toString());
    }

    public Bitmap decodeImageBitmap(String encoded) {
        byte[] decodedString = Base64.decode(encoded, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }

    private void NavigationViewDrawer() {

        activityMainBinding.navigationView.setNavigationItemSelectedListener(drawerNavigationItemSelectedListener);

        View view = activityMainBinding.navigationView.inflateHeaderView(R.layout.navigation_header);
        username = view.findViewById(R.id.navigation_drawer_username);
        emailNav = view.findViewById(R.id.navigation_drawer_email);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, activityMainBinding.drawerLayout, activityMainBinding.toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        activityMainBinding.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        activityMainBinding.navigationView.setCheckedItem(R.id.homeDrawer);
//        getSupportActionBar().setHomeAsUpIndicator(R.drawable.user_image);
        // should be after adding toggle listener
    }

    private void signOut() {
        auth = FirebaseAuth.getInstance();
        SharedPreferences sharedPreferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        new MaterialAlertDialogBuilder(this)
                .setTitle(this.getString(R.string.sureLogOut))
                .setMessage(this.getString(R.string.logOutText))
                .setPositiveButton(this.getString(R.string.yes), (dialog, which) -> {
                    if (auth != null) {
                        editor.putBoolean(SKIP_PROFILE_STEP, false);
                        editor.apply();
                        auth.signOut();
                        clearApplicationData();
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        MainActivity.this.startActivity(intent);
                        MainActivity.this.finish();
                        Toast.makeText(this, "Logged Out!", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton(this.getString(R.string.cancel), (dialog, which) -> {
                })
                .show();

    }

    private void updateImageDrawable(String uriString) {
////        ImageView profileImage = navigationHeaderBinding.navigationDrawerUserImage;
//        View view = activityMainBinding.navigationView.getHeaderView(0);
//        ImageView profileImage = view.findViewById(R.id.navigation_drawer_userImage);
        ImageView profImageToolbar = findViewById(R.id.profile_photo_toolbar);


        final Target target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
//                profileImage.setImageBitmap(bitmap);
                profImageToolbar.setImageBitmap(bitmap);

                Drawable drawable = new BitmapDrawable(getResources(), bitmap);
//                getSupportActionBar().setHomeAsUpIndicator(drawable);
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };
        profImageToolbar.setTag(target);
        Picasso.get()
                .load(uriString)
                .into(target);
    }


    public void allFirebase() {
        // authentication
        auth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        // database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Users").child(firebaseUser.getUid());

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String userName = dataSnapshot.child("DisplayName").getValue().toString();
                username.setText(userName);

                String email = dataSnapshot.child("email").getValue().toString();
                emailNav.setText(email);

                String userImageUri = dataSnapshot.child("photoUrl").getValue().toString();
                //               ImageView navImage = navigationView.findViewById(R.id.navigation_drawer_userImage);
//               Picasso.with(MainActivity.this).load(userImageUri).into(navImage);
//                updateImageDrawable(userImageUri);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void clearApplicationData() {
        File cache = getCacheDir();
        File appDir = new File(cache.getParent());
        if (appDir.exists()) {
            String[] children = appDir.list();
            for (String s : children) {
                if (!s.equals("lib")) {
                    deleteDir(new File(appDir, s));
                    Log.i("TAG", "File /data/data/APP_PACKAGE/" + s + " DELETED");
                }
            }
        }
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }

        return dir.delete();
    }
}
