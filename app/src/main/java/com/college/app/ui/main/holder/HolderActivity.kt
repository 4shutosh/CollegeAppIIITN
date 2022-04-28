package com.college.app.ui.main.holder

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.college.app.R
import com.college.app.databinding.ActivityHolderBinding
import com.college.app.utils.AppUtils.fragmentFromId
import com.college.app.utils.extensions.showToast
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HolderActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHolderBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHolderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpNavigation()
    }

    private fun setUpNavigation() {
        when (val fragmentId = intent.extras?.getInt(KEY_HOLDER_ACTIVITY_FRAGMENT) ?: 0) {
            R.layout.fragment_courses -> {
                val navHost =
                    supportFragmentManager.findFragmentById(R.id.activity_holder_fragment) as NavHostFragment
                val navController = navHost.navController
                navController.setGraph(R.navigation.nav_courses)
            }
            else -> {
                val fragment = fragmentFromId(fragmentId)
                if (fragment == null) {
                    showToast("Feature Not Yet Implemented!")
                } else {
                    supportFragmentManager.beginTransaction()
                        .add(R.id.activity_holder_fragment, fragment)
                        .commit()
                }
            }
        }

    }

    companion object {

        private const val KEY_HOLDER_ACTIVITY_FRAGMENT = "HOLDER_ACTIVITY_FRAGMENT_KEY"

        fun intent(context: Context, fragmentToHold: Int): Intent {
            val intent = Intent(context, HolderActivity::class.java)
            val bundle = Bundle()
            bundle.putInt(KEY_HOLDER_ACTIVITY_FRAGMENT, fragmentToHold)
            intent.putExtras(bundle)
            return intent
        }
    }
}