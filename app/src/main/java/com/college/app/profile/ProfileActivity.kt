package com.college.app.profile

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Base64
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.college.app.AppClass
import com.college.app.MainActivity
import com.college.app.databinding.ActivityProfileBinding
import com.college.app.profile.ProfileActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import com.squareup.picasso.Picasso.LoadedFrom
import com.squareup.picasso.Target
import io.objectbox.Box
import io.objectbox.BoxStore
import java.io.ByteArrayOutputStream
import java.util.*

class ProfileActivity : AppCompatActivity() {
    var activityProfileBinding: ActivityProfileBinding? = null
    var profileSem = 0
    var auth: FirebaseAuth? = null
    var profileYear = 0
    var profileName: String? = null
    var profileEmail: String? = null
    var encodedProfilePhoto: String? = null
    var profileBoxStore: BoxStore? = null
    var profileBox: Box<Profile>? = null
    var profile: Profile? = null
    private var myRef: DatabaseReference? = null
    var database: FirebaseDatabase? = null
    var firebaseUser: FirebaseUser? = null
    private var sem: List<String> = ArrayList(
        listOf(
            "",
            "First",
            "Second",
            "Third",
            "Fourth",
            "Fifth",
            "Sixth",
            "Seventh",
            "Eighth"
        )
    )
    var year: List<String> = ArrayList(
        Arrays.asList(
            "",
            "Freshman/First",
            "Sophomore/Second",
            "Junior/Third",
            "Senior/Final"
        )
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityProfileBinding = ActivityProfileBinding.inflate(
            layoutInflater
        )
        activityProfileBinding!!.progressCircular.visibility = View.VISIBLE

//        database = FirebaseDatabase.getInstance();
        setUpEditText()
        setContentView(activityProfileBinding!!.root)
        initiateObjectBox()
        Log.d(TAG, "onCreate: " + intent.extras!!["mode"])
        Log.d(TAG, "onCreate: " + intent.extras!!["action"])
        if (intent.extras != null) {
//            if (getIntent().getExtras().get("action").equals("skip")) {
//                Intent intent = new Intent(this, MainActivity.class);
//                this.startActivity(intent);
//                this.finish();
//            } else if (getIntent().getExtras().get("action").equals("stay")) {
//
//            }
            Log.d(TAG, "onCreate: just a step away : stay received from login")
            if (intent.extras != null) {
                if (intent.extras!!["mode"] == "Login") {
                    setUpUiFromLogin()
                } else if (intent.extras!!["mode"] == "Main") {
                    setUpUiFromMain()
                }
            } else {
                Toast.makeText(this, "Something Went Wrong", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
        val sharedPreferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        activityProfileBinding!!.proceed.setOnClickListener { v: View? ->
            editor.putBoolean(SKIP_PROFILE_STEP, true)
            editor.apply()
            val intent = Intent(this@ProfileActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
            Toast.makeText(applicationContext, "Welcome!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onStart() {
        super.onStart()
    }

    private fun initiateObjectBox() {
        profileBoxStore = (this.application as AppClass).boxStore
        profileBox = profileBoxStore!!.boxFor(Profile::class.java)
    }

    private fun setUpEditText() {
        activityProfileBinding!!.progressCircular.visibility = View.VISIBLE

//        auth = FirebaseAuth.getInstance();
//        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
//        database = FirebaseDatabase.getInstance();
        myRef = FirebaseDatabase.getInstance().getReference("Users").child(
            FirebaseAuth.getInstance().currentUser!!.uid
        )
        myRef!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    profileName = dataSnapshot.child("displayName").value.toString()
                    activityProfileBinding!!.profileName.text = profileName
                    profileEmail = dataSnapshot.child("email").value.toString()
                    activityProfileBinding!!.profileEmail.text = profileEmail
                    if (dataSnapshot.child("photoUrl").exists()) {
                        val userImageUri = dataSnapshot.child("photoUrl").value.toString()
                        updateImageDrawable(userImageUri)
                    }
                    //               ImageView navImage = navigationView.findViewById(R.id.navigation_drawer_userImage);
//               Picasso.with(MainActivity.this).load(userImageUri).into(navImage);
                    activityProfileBinding!!.progressCircular.visibility = View.GONE
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    private fun setUpUiFromMain() {
        val profileFromMain = profileBox!![0]
        /// textViews
        activityProfileBinding!!.profileName.text = profileFromMain.name
        activityProfileBinding!!.profileEmail.text = profileFromMain.email
        activityProfileBinding!!.rollNumber.setText(profileFromMain.rollNumber)
        activityProfileBinding!!.profileYear.setText(profileFromMain.yearStudying)
        activityProfileBinding!!.profileSemester.setText(profileFromMain.semesterStudying)
        activityProfileBinding!!.profileBranch.text = profileFromMain.branch

        // image View
    }

    private fun setUpUiFromLogin() {
        Log.d(TAG, "setUpUiFromLogin: Started")
        activityProfileBinding!!.rollNumber.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (count == 10) {
                    hideKeyboard(this@ProfileActivity)
                    rollNumberDecoder(
                        activityProfileBinding!!.rollNumber.text.toString().toUpperCase()
                    )
                } else {
                    activityProfileBinding!!.proceed.visibility = View.GONE
                    activityProfileBinding!!.rollNumber.error = "Invalid Roll Number"
                    activityProfileBinding!!.profileYear.text = ""
                    activityProfileBinding!!.profileSemester.text = ""
                    activityProfileBinding!!.profileBranch.text = ""
                    activityProfileBinding!!.rollNumber.requestFocus()
                }
            }

            override fun afterTextChanged(s: Editable) {}
        })
    }

    fun hideKeyboard(activity: Activity) {
        val imm = activity.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        //Find the currently focused view, so we can grab the correct window token from it.
        var view = activity.currentFocus
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = View(activity)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
        //        activityProfileBinding.rollNumber.findFocus().clearFocus();
    }

    fun rollNumberDecoder(rollNumberFromEt: String) {
        val tempList = rollNumberFromEt.toCharArray()
        val a = tempList[2].toString() + tempList[3].toString()
        val branch = tempList[4].toString() + tempList[5].toString() + tempList[6].toString()
        val rollYear = a.toInt()
        val currentYear = Calendar.getInstance()[Calendar.YEAR] % 100 // to get the last two digits
        val currentMonth =
            Calendar.getInstance()[Calendar.MONTH] + 1 // calendar is zero based in java

        //////////////////////////////
        val decidingMonth = 4 // start of new academic year is considered as April
        if (rollYear == currentYear && currentMonth < decidingMonth) {
            Toast.makeText(this, "Invalid RollNumber", Toast.LENGTH_SHORT).show()
        } else {
            if (currentMonth > decidingMonth) {
                profileYear = currentYear - rollYear + 1
                profileSem = 2 * profileYear - 1
            } else if (currentMonth < decidingMonth) {
                profileYear = currentYear - rollYear
                profileSem = 2 * profileYear
            }
            if (profileYear < 5) {
                // set text here
                activityProfileBinding!!.profileSemester.text = sem[profileSem]
                activityProfileBinding!!.profileYear.text = year[profileYear]
                activityProfileBinding!!.profileBranch.text = branch
                println("student year $profileYear student semester $profileSem")
                Toast.makeText(this, "Please Proceed", Toast.LENGTH_SHORT).show()
                activityProfileBinding!!.proceed.visibility = View.VISIBLE
                /////////////////////
                Log.d(TAG, "saving profile of name $profileName $profileEmail")
                if (encodedProfilePhoto != null) {
                    profileBox!!.put(
                        Profile(
                            0,
                            profileName,
                            profileEmail,
                            rollNumberFromEt,
                            profileYear,
                            profileSem,
                            branch,
                            encodedProfilePhoto
                        )
                    )
                } else {
                    profileBox!!.put(
                        Profile(
                            0,
                            profileName,
                            profileEmail,
                            rollNumberFromEt,
                            profileYear,
                            profileSem,
                            branch,
                            null
                        )
                    )
                }

                /////////////////////
            } else {
                activityProfileBinding!!.proceed.visibility = View.GONE
                activityProfileBinding!!.rollNumber.error = "Invalid Roll Number"
                activityProfileBinding!!.rollNumber.requestFocus()
            }
        }
    }

    fun updateImageDrawable(uri: String?) {
        val target: Target = object : Target {
            override fun onBitmapLoaded(bitmap: Bitmap, from: LoadedFrom) {
                saveImageBitmapToDB(bitmap)
                activityProfileBinding!!.profileImage.setImageBitmap(bitmap)
                val drawable: Drawable = BitmapDrawable(resources, bitmap)
                //                getSupportActionBar().setHomeAsUpIndicator(drawable);
            }

            override fun onBitmapFailed(e: Exception, errorDrawable: Drawable) {}
            override fun onPrepareLoad(placeHolderDrawable: Drawable) {}
        }
        activityProfileBinding!!.profileImage.tag = target
        Picasso.get()
            .load(uri)
            .into(target)

        // save image into box
    }

    fun saveImageBitmapToDB(bitmap: Bitmap) {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        encodedProfilePhoto = Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    fun decodeImageBitmap(encoded: String?) {
        val decodedString = Base64.decode(encoded, Base64.DEFAULT)
        val decodedBitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
    }

    companion object {
        const val MyPREFERENCES = "MyPrefs"
        const val SKIP_PROFILE_STEP = "SkipProfileStep"
        private const val TAG = "ProfileActivity"
    }
}