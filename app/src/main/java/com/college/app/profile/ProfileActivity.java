package com.college.app.profile;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.college.app.AppClass;
import com.college.app.MainActivity;
import com.college.app.databinding.ActivityProfileBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import io.objectbox.Box;
import io.objectbox.BoxStore;

public class ProfileActivity extends AppCompatActivity {

    ActivityProfileBinding activityProfileBinding;
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String SKIP_PROFILE_STEP = "SkipProfileStep";
    public int profileSem;
    FirebaseAuth auth;
    public int profileYear;
    String profileName;
    String profileEmail;
    String encodedProfilePhoto;
    BoxStore profileBoxStore;
    Box<Profile> profileBox;
    private static final String TAG = "ProfileActivity";
    Profile profile;
    private DatabaseReference myRef;
    FirebaseDatabase database;
    FirebaseUser firebaseUser;
    List<String> sem = new ArrayList<String>(Arrays.asList("", "First", "Second", "Third", "Fourth", "Fifth", "Sixth", "Seventh", "Eighth"));
    List<String> year = new ArrayList<String>(Arrays.asList("", "Freshman/First", "Sophomore/Second", "Junior/Third", "Senior/Final"));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityProfileBinding = ActivityProfileBinding.inflate(getLayoutInflater());
        activityProfileBinding.progressCircular.setVisibility(View.VISIBLE);

//        database = FirebaseDatabase.getInstance();

        setUpEditText();
        setContentView(activityProfileBinding.getRoot());
        initiateObjectBox();

        Log.d(TAG, "onCreate: " + getIntent().getExtras().get("mode"));
        Log.d(TAG, "onCreate: " + getIntent().getExtras().get("action"));


        if (getIntent().getExtras() != null) {
//            if (getIntent().getExtras().get("action").equals("skip")) {
//                Intent intent = new Intent(this, MainActivity.class);
//                this.startActivity(intent);
//                this.finish();
//            } else if (getIntent().getExtras().get("action").equals("stay")) {
//
//            }

            Log.d(TAG, "onCreate: just a step away : stay received from login");
            if (getIntent().getExtras() != null) {
                if (getIntent().getExtras().get("mode").equals("Login")) {
                    setUpUiFromLogin();
                } else if (getIntent().getExtras().get("mode").equals("Main")) {
                    setUpUiFromMain();
                }
            } else {
                Toast.makeText(this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                finish();
            }
        }

        SharedPreferences sharedPreferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        activityProfileBinding.proceed.setOnClickListener(v -> {
            editor.putBoolean(SKIP_PROFILE_STEP, true);
            editor.apply();
            Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
            startActivity(intent);
            ProfileActivity.this.finish();
            Toast.makeText(getApplicationContext(), "Welcome!", Toast.LENGTH_SHORT).show();
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    private void initiateObjectBox() {
        profileBoxStore = ((AppClass) this.getApplication()).getBoxStore();
        profileBox = profileBoxStore.boxFor(Profile.class);
    }

    private void setUpEditText() {
        activityProfileBinding.progressCircular.setVisibility(View.VISIBLE);

//        auth = FirebaseAuth.getInstance();
//        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
//        database = FirebaseDatabase.getInstance();
        myRef = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    profileName = dataSnapshot.child("displayName").getValue().toString();
                    activityProfileBinding.profileName.setText(profileName);

                    profileEmail = dataSnapshot.child("email").getValue().toString();
                    activityProfileBinding.profileEmail.setText(profileEmail);
                    if (dataSnapshot.child("photoUrl").exists()) {
                        String userImageUri = dataSnapshot.child("photoUrl").getValue().toString();
                        updateImageDrawable(userImageUri);
                    }
                    //               ImageView navImage = navigationView.findViewById(R.id.navigation_drawer_userImage);
//               Picasso.with(MainActivity.this).load(userImageUri).into(navImage);

                    activityProfileBinding.progressCircular.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void setUpUiFromMain() {
        Profile profileFromMain = profileBox.get(0);
        /// textViews
        activityProfileBinding.profileName.setText(profileFromMain.getName());
        activityProfileBinding.profileEmail.setText(profileFromMain.getEmail());
        activityProfileBinding.rollNumber.setText(profileFromMain.getRollNumber());
        activityProfileBinding.profileYear.setText(profileFromMain.getYearStudying());
        activityProfileBinding.profileSemester.setText(profileFromMain.getSemesterStudying());
        activityProfileBinding.profileBranch.setText(profileFromMain.getBranch());

        // image View

    }

    private void setUpUiFromLogin() {
        Log.d(TAG, "setUpUiFromLogin: Started");

        activityProfileBinding.rollNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count == 10) {
                    hideKeyboard(ProfileActivity.this);
                    rollNumberDecoder(activityProfileBinding.rollNumber.getText().toString().toUpperCase());
                } else {
                    activityProfileBinding.proceed.setVisibility(View.GONE);
                    activityProfileBinding.rollNumber.setError("Invalid Roll Number");
                    activityProfileBinding.profileYear.setText("");
                    activityProfileBinding.profileSemester.setText("");
                    activityProfileBinding.profileBranch.setText("");
                    activityProfileBinding.rollNumber.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        activityProfileBinding.rollNumber.findFocus().clearFocus();

    }

    public void rollNumberDecoder(String rollNumberFromEt) {
        char[] tempList = rollNumberFromEt.toCharArray();

        String a = String.valueOf(tempList[2]) + String.valueOf(tempList[3]);
        String branch = String.valueOf(tempList[4]) + String.valueOf(tempList[5]) + String.valueOf(tempList[6]);
        int rollYear = Integer.parseInt(a);

        int currentYear = (Calendar.getInstance().get(Calendar.YEAR)) % 100; // to get the last two digits
        int currentMonth = Calendar.getInstance().get(Calendar.MONTH) + 1;  // calendar is zero based in java

        //////////////////////////////
        int decidingMonth = 4; // start of new academic year is considered as April

        if (rollYear == currentYear && currentMonth < decidingMonth) {
            Toast.makeText(this, "Invalid RollNumber", Toast.LENGTH_SHORT).show();
        } else {
            if (currentMonth > decidingMonth) {
                profileYear = currentYear - rollYear + 1;
                profileSem = 2 * profileYear - 1;
            } else if (currentMonth < decidingMonth) {
                profileYear = currentYear - rollYear;
                profileSem = 2 * (profileYear);
            }
            if (profileYear < 5) {
                // set text here
                activityProfileBinding.profileSemester.setText(sem.get(profileSem));
                activityProfileBinding.profileYear.setText(year.get(profileYear));
                activityProfileBinding.profileBranch.setText(branch);
                System.out.println("student year " + profileYear + " student semester " + profileSem);
                Toast.makeText(this, "Please Proceed", Toast.LENGTH_SHORT).show();
                activityProfileBinding.proceed.setVisibility(View.VISIBLE);
                /////////////////////
                Log.d(TAG, "saving profile of name " + profileName + " " + profileEmail);
                if (encodedProfilePhoto != null) {
                    profileBox.put(new Profile(0, profileName, profileEmail, rollNumberFromEt, profileYear, profileSem, branch, encodedProfilePhoto));
                } else {
                    profileBox.put(new Profile(0, profileName, profileEmail, rollNumberFromEt, profileYear, profileSem, branch, null));

                }

                /////////////////////
            } else {
                activityProfileBinding.proceed.setVisibility(View.GONE);
                activityProfileBinding.rollNumber.setError("Invalid Roll Number");
                activityProfileBinding.rollNumber.requestFocus();
            }
        }

    }

    public void updateImageDrawable(String uri) {
        final Target target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                saveImageBitmapToDB(bitmap);
                activityProfileBinding.profileImage.setImageBitmap(bitmap);

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
        activityProfileBinding.profileImage.setTag(target);
        Picasso.get()
                .load(uri)
                .into(target);

        // save image into box
    }

    void saveImageBitmapToDB(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        encodedProfilePhoto = Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    void decodeImageBitmap(String encoded) {
        byte[] decodedString = Base64.decode(encoded, Base64.DEFAULT);
        Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }


}