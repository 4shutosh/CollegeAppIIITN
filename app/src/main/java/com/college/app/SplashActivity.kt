package com.college.app

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.college.app.databinding.ActivitySplashBinding
import com.college.base.utils.logger.CollegeLogger
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    @Inject
    lateinit var logger : CollegeLogger

    private lateinit var activitySplashBinding: ActivitySplashBinding
    private var skipStatus: Boolean? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activitySplashBinding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(activitySplashBinding.root)
//        installSplashScreen()

//        moveActivity()
    }


    override fun onPostCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onPostCreate(savedInstanceState, persistentState)
        logger.d("post create buddy")
    }

    private fun moveActivity() {
        goToMainActivity()
    }

    private fun runAnimation() {
        val a = AnimationUtils.loadAnimation(this, R.anim.splash_text_animation)
        a.reset()
        val tv = activitySplashBinding?.splashText
        val card = activitySplashBinding!!.materialCardView
        card.clearAnimation()
        tv?.clearAnimation()
        tv?.startAnimation(a)
        card.startAnimation(a)
    }

    private fun goToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        this.startActivity(intent)
        finish()
    }

    private fun goToLoginActivity() {
//        val intent = Intent(this, LoginActivity::class.java)
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