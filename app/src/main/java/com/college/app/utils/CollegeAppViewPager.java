package com.college.app.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

public class CollegeAppViewPager extends ViewPager {

    private boolean isPagingEnabled;

    public CollegeAppViewPager(@NonNull Context context) {
        super(context);
        this.isPagingEnabled = true;
    }

    public CollegeAppViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.isPagingEnabled = true;
    }

    public void setPagingEnabled(boolean pagingEnabled) {
        isPagingEnabled = pagingEnabled;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return this.isPagingEnabled && super.onTouchEvent(ev);
    }


    @Override
    public boolean executeKeyEvent(@NonNull KeyEvent event) {
        return this.isPagingEnabled && super.executeKeyEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return this.isPagingEnabled && super.onInterceptTouchEvent(ev);
    }
}
