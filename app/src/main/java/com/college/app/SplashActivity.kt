package com.college.app

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.WindowManager
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.college.app.databinding.ActivitySplashBinding
import com.college.app.fragment.SettingsFragment

class SplashActivity : AppCompatActivity() {
    private var activitySplashBinding: ActivitySplashBinding? = null
    private var skipStatus: Boolean? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activitySplashBinding = ActivitySplashBinding.inflate(layoutInflater)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(activitySplashBinding!!.root)
        val sharedPreferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE)
        skipStatus = sharedPreferences.getBoolean(SKIP_STATUS, false)
        val sharedTheme = sharedPreferences.getString(SettingsFragment.THEME, "System Default")
        Log.d(TAG, "darkModeSwitch: shared value $sharedTheme")
        when (sharedTheme) {
            "System Default" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            "Dark Theme" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            "Light Theme" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
        Handler().postDelayed({
            runAnimation()
            if (skipStatus!!) {
                goToMainActivity()
            } else {
                goToLoginActivity()
            }
        }, SPLASH_SCREEN_TIME_OUT.toLong())
    }

    private fun runAnimation() {
        val a = AnimationUtils.loadAnimation(this, R.anim.splash_text_animation)
        a.reset()
        val tv = activitySplashBinding!!.splashText
        val card = activitySplashBinding!!.materialCardView
        card.clearAnimation()
        tv.clearAnimation()
        tv.startAnimation(a)
        card.startAnimation(a)
    }

    private fun goToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        this.startActivity(intent)
        finish()
    }

    private fun goToLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        this.startActivity(intent)
        finish()
    }

    companion object {
        private const val SPLASH_SCREEN_TIME_OUT = 500
        const val MyPREFERENCES = "MyPrefs"
        const val SKIP_STATUS = "SkipProfileStep"
        private const val TAG = "SplashActivity"
    }
}