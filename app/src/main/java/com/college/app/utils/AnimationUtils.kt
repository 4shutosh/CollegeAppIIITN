package com.college.app.utils

import android.animation.Animator
import android.animation.ValueAnimator
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator

object AnimationUtils {
    fun expand(v: View) {
        v.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        val targetHeight = v.measuredHeight

        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        v.layoutParams.height = 1
        v.visibility = View.VISIBLE
        val valueAnimator = ValueAnimator.ofInt(1, targetHeight)
        valueAnimator.addUpdateListener { animation: ValueAnimator ->
            v.layoutParams.height = (animation.animatedValue as Int)
            v.requestLayout()
        }
        valueAnimator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationEnd(animation: Animator) {
                v.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
            }

            override fun onAnimationStart(animation: Animator) {}
            override fun onAnimationCancel(animation: Animator) {}
            override fun onAnimationRepeat(animation: Animator) {}
        })
        valueAnimator.duration = 100
        valueAnimator.interpolator = LinearInterpolator()
        valueAnimator.start()
    }

    fun collapse(v: View) {
        val initialHeight = v.measuredHeight
        val valueAnimator = ValueAnimator.ofInt(initialHeight, 0)
        valueAnimator.addUpdateListener { animation: ValueAnimator ->
            v.layoutParams.height = (animation.animatedValue as Int)
            v.requestLayout()
        }
        valueAnimator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationEnd(animation: Animator) {
                v.visibility = View.GONE
            }

            override fun onAnimationStart(animation: Animator) {}
            override fun onAnimationCancel(animation: Animator) {}
            override fun onAnimationRepeat(animation: Animator) {}
        })
        valueAnimator.duration = 100
        valueAnimator.interpolator = LinearInterpolator()
        valueAnimator.start()
    }
}