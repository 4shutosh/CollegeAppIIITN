package com.college.app

import android.content.*
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.*
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.GravityCompat
import androidx.fragment.app.FragmentManager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.college.app.Notification.Notification
import com.college.app.Notification.NotificationAdapter
import com.college.app.Notification.NotificationsFragment
import com.college.app.adapter.ViewPagerAdapter
import com.college.app.courses.CoursesActivity
import com.college.app.databinding.ActivityMainBinding
import com.college.app.fragment.*
import com.college.app.profile.Profile
import com.college.app.todo.TodoFragment
import com.college.app.utils.CollegeAppViewPager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.messaging.FirebaseMessaging
import com.squareup.picasso.Picasso
import com.squareup.picasso.Picasso.LoadedFrom
import com.squareup.picasso.Target
import io.objectbox.Box
import io.objectbox.BoxStore
import java.io.ByteArrayOutputStream
import java.io.File

class MainActivity : AppCompatActivity() {
    private lateinit var activityMainBinding: ActivityMainBinding
    var bottomNavigationView: BottomNavigationView? = null
    private var auth //
            : FirebaseAuth? = null
    private lateinit var viewPager: CollegeAppViewPager
    private lateinit var adapter: ViewPagerAdapter
    private var bsNotifications: ConstraintLayout? = null
    private var bottomSheetBehaviorNotifications: BottomSheetBehavior<*>? = null
    var menuItem: MenuItem? = null
    private var profileBoxStore: BoxStore? = null
    private var profileBox: Box<Profile>? = null
    private var imageBitmap: Bitmap? = null
    var username: TextView? = null
    var emailNav: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate: ")
        super.onCreate(savedInstanceState)
        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)

//        applyTheme();
        setContentView(activityMainBinding.root)
        setSupportActionBar(activityMainBinding.toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeButtonEnabled(true)
        navigationDrawer() // navigationDrawer + toolbar

//        allFirebase();
        viewPager = activityMainBinding.fragmentContainer
        adapter = ViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(HomeFragment())
        adapter.addFragment(NotificationsFragment())
        adapter.addFragment(TodoFragment())
        viewPager.setPagingEnabled(false)
        viewPager.adapter = adapter
        bottomNavigationView = activityMainBinding.bottomNavigation
        bottomNavigationView!!.setOnNavigationItemSelectedListener(
            bottomNavigationItemSelectedListener
        ) // bottomNavigation View
        // fragment controller
        viewPager.addOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                if (menuItem != null) {
                    menuItem!!.isChecked = false
                } else {
                    bottomNavigationView!!.menu.getItem(0).isChecked = false
                }
                bottomNavigationView!!.menu.getItem(position).isChecked = true
                menuItem = bottomNavigationView!!.menu.getItem(position)
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })
        //        getSupportFragmentManage().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
        var extraFragment: String? = "a"
        val extras: Bundle? = intent.extras
        if (extras != null) {
            extraFragment = extras.getString("fragmentInformation")
            val fm: FragmentManager = supportFragmentManager
            for (i in 0 until fm.backStackEntryCount) {
                fm.popBackStack()
            }
            when (extraFragment) {
                "1" -> supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, HomeFragment()).commit()
                "2" -> supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, NotificationsFragment()).commit()
                "3" -> supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, TodoFragment()).commit()
                else -> supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, HomeFragment()).commit()
            }
        }
        bsNotifications = findViewById(R.id.bottom_sheet_notifications)
        bottomSheetBehaviorNotifications = BottomSheetBehavior.from(bsNotifications!!)
        updateUserDetails()
        handleFCMData()
    }

    private fun handleFCMData() {
        FirebaseMessaging.getInstance().subscribeToTopic("all")
        val intent: Intent? = intent
        if (intent?.extras != null) {
            Log.d(TAG, "handleFCMData: data found bruh")
            Toast.makeText(this, "Data found", Toast.LENGTH_SHORT).show()
            val extras: Bundle? = intent.extras
            val title: String? = extras!!.getString("Title")
            val body: String? = extras.getString("Message")
            NotificationAdapter.notificationAdapterBox.put(Notification(0, title, body))
//            NotificationsFragment.notificationAdapter!!.updateNotificationView()
        } else {
            Log.d(TAG, "handleFCMData: no data found")
            //            Toast.makeText(this, "no data found", Toast.LENGTH_SHORT).show();
        }
    }

    override fun onBackPressed() {
        if (activityMainBinding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            activityMainBinding.drawerLayout.closeDrawer(GravityCompat.START)
        } else if (bottomSheetBehaviorNotifications!!.state == BottomSheetBehavior.STATE_EXPANDED) {
            bottomSheetBehaviorNotifications!!.setState(BottomSheetBehavior.STATE_HIDDEN)
        } else {
            super.onBackPressed()
        }
    }

    override fun onResume() {
        Log.d(TAG, "onResume: ")
        closeDrawer()
        super.onResume()
    }

    fun closeDrawer() {
        if (activityMainBinding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            activityMainBinding.drawerLayout.closeDrawer(GravityCompat.START)
        }
        activityMainBinding.navigationView.setCheckedItem(R.id.homeDrawer)
    }

    private val mMessageReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        public override fun onReceive(context: Context, intent: Intent) {
            val message: String = intent.getStringExtra("body")
        }
    }
    private val bottomNavigationItemSelectedListener: BottomNavigationView.OnNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item: MenuItem ->
            when (item.itemId) {
                R.id.bottom_navigation_home -> {
                    //                        selectedFragment = new HomeFragment();
                    viewPager.currentItem = 0
                    activityMainBinding.toolbar.title = getText(R.string.home)
                }
                R.id.bottom_navigation_notifications -> {
                    //                        selectedFragment = new NotificationsFragment();
                    viewPager.currentItem = 1
                    activityMainBinding.toolbar.title = getText(R.string.notifications)
                }
                R.id.bottom_navigation_todo -> {
                    //                        selectedFragment = new TodoFragment();
                    viewPager.currentItem = 2
                    activityMainBinding.toolbar.title = getText(R.string.todo)
                }
            }
            false
        }
    private val drawerNavigationItemSelectedListener: NavigationView.OnNavigationItemSelectedListener =
        NavigationView.OnNavigationItemSelectedListener { item: MenuItem ->
            when (item.itemId) {
                R.id.exitApp -> finish()
                R.id.logOut -> signOut()
                R.id.settings -> HolderActivity.startActivity(
                    this,
                    SettingsFragment::class.java,
                    null,
                    null
                )
                R.id.coursesDrawer -> {
                    val intent: Intent = Intent(this, CoursesActivity::class.java)
                    startActivity(intent)
                }
                R.id.eventsDrawer -> HolderActivity.startActivity(
                    this,
                    EventsFragment::class.java,
                    null,
                    null
                )
                R.id.servicesDrawer -> HolderActivity.startActivity(
                    this,
                    ServiceFragment::class.java,
                    null,
                    null
                )
            }
            closeDrawer()
            true
        }

    private fun updateUserDetails() {
        profileBoxStore = (this.application as AppClass).boxStore
        profileBox = profileBoxStore!!.boxFor(Profile::class.java) as Box<Profile>
        val list: List<Profile> = profileBox!!.all
        val profile: Profile = list[0]
        Log.d(TAG, "updateUserDetails: " + profile.name)
        username?.text = profile.name
        emailNav?.text = profile.email
        if (profile.imageBitmapEncoded != null) {
            imageBitmap = decodeImageBitmap(profile.imageBitmapEncoded)
            val view: View = activityMainBinding.navigationView.getHeaderView(0)
            val profileImage: ImageView = view.findViewById(R.id.navigation_drawer_userImage)
            profileImage.setImageBitmap(imageBitmap)
        }

        // use
    }

    fun getImageUriFromBitmap(bitmap: Bitmap): Uri {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        val path: String =
            MediaStore.Images.Media.insertImage(contentResolver, bitmap, "Title", null)
        return Uri.parse(path)
    }

    private fun decodeImageBitmap(encoded: String?): Bitmap {
        val decodedString: ByteArray = Base64.decode(encoded, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
    }

    private fun navigationDrawer() {
        activityMainBinding.navigationView.setNavigationItemSelectedListener(
            drawerNavigationItemSelectedListener
        )
        val view: View =
            activityMainBinding.navigationView.inflateHeaderView(R.layout.navigation_header)
        username = view.findViewById(R.id.navigation_drawer_username)
        emailNav = view.findViewById(R.id.navigation_drawer_email)
        val toggle = ActionBarDrawerToggle(
            this,
            activityMainBinding.drawerLayout,
            activityMainBinding.toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        activityMainBinding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        activityMainBinding.navigationView.setCheckedItem(R.id.homeDrawer)
        //        getSupportActionBar().setHomeAsUpIndicator(R.drawable.user_image);
        // should be after adding toggle listener
    }

    private fun signOut() {
        auth = FirebaseAuth.getInstance()
        val sharedPreferences: SharedPreferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        MaterialAlertDialogBuilder(this)
            .setTitle(this.getString(R.string.sureLogOut))
            .setMessage(this.getString(R.string.logOutText))
            .setPositiveButton(
                this.getString(R.string.yes)
            ) { dialog: DialogInterface?, which: Int ->
                if (auth != null) {
                    editor.putBoolean(SKIP_PROFILE_STEP, false)
                    editor.apply()
                    auth!!.signOut()
                    clearApplicationData()
                    val intent: Intent = Intent(this@MainActivity, LoginActivity::class.java)
                    this@MainActivity.startActivity(intent)
                    finish()
                    Toast.makeText(this, "Logged Out!", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton(
                this.getString(R.string.cancel),
                DialogInterface.OnClickListener { dialog: DialogInterface?, which: Int -> }
            )
            .show()
    }

    private fun updateImageDrawable(uriString: String) {
////        ImageView profileImage = navigationHeaderBinding.navigationDrawerUserImage;
//        View view = activityMainBinding.navigationView.getHeaderView(0);
//        ImageView profileImage = view.findViewById(R.id.navigation_drawer_userImage);
        val profImageToolbar: ImageView = findViewById(R.id.profile_photo_toolbar)
        val target: Target = object : Target {
            public override fun onBitmapLoaded(bitmap: Bitmap, from: LoadedFrom) {
//                profileImage.setImageBitmap(bitmap);
                profImageToolbar.setImageBitmap(bitmap)
                val drawable = BitmapDrawable(resources, bitmap)
                //                getSupportActionBar().setHomeAsUpIndicator(drawable);
            }

            override fun onBitmapFailed(e: Exception, errorDrawable: Drawable) {}
            override fun onPrepareLoad(placeHolderDrawable: Drawable) {}
        }
        profImageToolbar.tag = target
        Picasso.get()
            .load(uriString)
            .into(target)
    }

    fun allFirebase() {
        // authentication
        auth = FirebaseAuth.getInstance()
        val firebaseUser: FirebaseUser? = FirebaseAuth.getInstance().getCurrentUser()

        // database
        val database: FirebaseDatabase = FirebaseDatabase.getInstance()
        val myRef: DatabaseReference = database.getReference("Users").child(firebaseUser!!.getUid())
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val userName: String = dataSnapshot.child("DisplayName").value.toString()
                username!!.text = userName
                val email: String = dataSnapshot.child("email").value.toString()
                emailNav!!.text = email
                val userImageUri: String = dataSnapshot.child("photoUrl").value.toString()
                //               ImageView navImage = navigationView.findViewById(R.id.navigation_drawer_userImage);
//               Picasso.with(MainActivity.this).load(userImageUri).into(navImage);
//                updateImageDrawable(userImageUri);
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    private fun clearApplicationData() {
        val cache: File = cacheDir
        val appDir = File(cache.getParent())
        if (appDir.exists()) {
            val children: Array<String> = appDir.list()
            for (s: String in children) {
                if (!(s == "lib")) {
                    deleteDir(File(appDir, s))
                    Log.i("TAG", "File /data/data/APP_PACKAGE/$s DELETED")
                }
            }
        }
    }

    companion object {
        const val MyPREFERENCES: String = "MyPrefs"
        const val SKIP_PROFILE_STEP: String = "SkipProfileStep"
        private val TAG: String = "MainActivity"
        fun deleteDir(dir: File?): Boolean {
            if (dir != null && dir.isDirectory) {
                val children: Array<String> = dir.list()
                for (i in children.indices) {
                    val success: Boolean = deleteDir(File(dir, children[i]))
                    if (!success) {
                        return false
                    }
                }
            }
            return dir!!.delete()
        }
    }
}