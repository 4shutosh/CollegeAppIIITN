package com.college.app.utils

import android.content.Context
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager

class CollegeAppViewPager : ViewPager {
    private var isPagingEnabled: Boolean

    constructor(context: Context) : super(context) {
        isPagingEnabled = true
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        isPagingEnabled = true
    }

    fun setPagingEnabled(pagingEnabled: Boolean) {
        isPagingEnabled = pagingEnabled
    }

    override fun onTouchEvent(ev: MotionEvent): Boolean {
        return isPagingEnabled && super.onTouchEvent(ev)
    }

    override fun executeKeyEvent(event: KeyEvent): Boolean {
        return isPagingEnabled && super.executeKeyEvent(event)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        return isPagingEnabled && super.onInterceptTouchEvent(ev)
    }
}