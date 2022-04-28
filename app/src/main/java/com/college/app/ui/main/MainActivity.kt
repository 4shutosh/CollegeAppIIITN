package com.college.app.ui.main

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import coil.load
import com.college.app.R
import com.college.app.databinding.ActivityMainBinding
import com.college.app.ui.main.holder.HolderActivity
import com.college.app.ui.profile.ProfileSettingsFragment.Companion.FRAGMENT_PROFILE_SETTINGS_ID
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpNavigation()
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        setUpObservers()
        setUpViews()
    }

    private fun setUpViews() {
        binding.userImage.setOnClickListener {
            val intent = HolderActivity.intent(this, FRAGMENT_PROFILE_SETTINGS_ID)
            startActivity(intent)
        }
    }

    private fun setUpNavigation() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.activity_main_fragment_host) as NavHostFragment
        binding.bottomNavigation.setupWithNavController(navHostFragment.navController)

        navHostFragment.navController.addOnDestinationChangedListener { controller, destination, arguments ->
            when (destination.id) {
                R.id.todoFragment -> binding.welcomeMessage.text = "// TODO"
                R.id.homeFragment -> binding.welcomeMessage.text = "Home"
                R.id.notificationsFragment -> binding.welcomeMessage.text = "Notifications & Alerts"
            }
        }
    }


    private fun setUpObservers() {
        viewModel.userImageUrl.observe(this) {
            binding.userImage.load(it.userImageUrl) {
                placeholder(R.drawable.user_image)
            }
            val messageText = "Hey ${it.userName}!"
            binding.welcomeMessage.text = messageText
        }
    }
}