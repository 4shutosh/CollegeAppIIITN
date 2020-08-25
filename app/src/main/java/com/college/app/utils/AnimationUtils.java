package com.college.app.utils;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;

import androidx.annotation.NonNull;

public class AnimationUtils {

    public static void expand(final View v) {
        v.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final int targetHeight = v.getMeasuredHeight();

        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        v.getLayoutParams().height = 1;
        v.setVisibility(View.VISIBLE);

        ValueAnimator valueAnimator = ValueAnimator.ofInt(1, targetHeight);
        valueAnimator.addUpdateListener(animation -> {
            v.getLayoutParams().height = (Integer) animation.getAnimatedValue();
            v.requestLayout();
        });
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationEnd(Animator animation) {
                v.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
            }

            @Override public void onAnimationStart(Animator animation) {}
            @Override public void onAnimationCancel(Animator animation) {}
            @Override public void onAnimationRepeat(Animator animation) {}
        });
        valueAnimator.setDuration(100);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.start();
    }

    public static void collapse(final @NonNull View v) {
        final int initialHeight = v.getMeasuredHeight();

        ValueAnimator valueAnimator = ValueAnimator.ofInt(initialHeight, 0);
        valueAnimator.addUpdateListener(animation -> {
            v.getLayoutParams().height = (Integer) animation.getAnimatedValue();
            v.requestLayout();
        });
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationEnd(Animator animation) {
                v.setVisibility(View.GONE);
            }

            @Override public void onAnimationStart(Animator animation) {}
            @Override public void onAnimationCancel(Animator animation) {}
            @Override public void onAnimationRepeat(Animator animation) {}
        });
        valueAnimator.setDuration(100);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.start();
    }
}
