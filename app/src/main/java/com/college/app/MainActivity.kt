package com.college.app

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.college.app.databinding.ActivityMainBinding
import com.college.app.databinding.ActivitySplashBinding

class MainActivity : AppCompatActivity() {
    private lateinit var activityMainBinding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityMainBinding = ActivitySplashBinding.inflate(layoutInflater)

        installSplashScreen()

        setContentView(activityMainBinding.root)

        val content: View = findViewById(android.R.id.content)
        content.viewTreeObserver.addOnPreDrawListener {
            true
        }

    }
}