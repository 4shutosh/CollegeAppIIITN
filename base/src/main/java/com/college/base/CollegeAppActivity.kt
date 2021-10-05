package com.college.base

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity

abstract class CollegeAppActivity : AppCompatActivity() {

    abstract fun setUpViews()
    abstract fun setUpObservers()

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        setUpViews()
        setUpObservers()
    }
}