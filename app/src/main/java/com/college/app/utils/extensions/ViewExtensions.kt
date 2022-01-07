package com.college.app.utils.extensions

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.abs

fun RecyclerView.bringItemToView(position: Int) {
    val first = getChildAt(0)
    val height = first.height
    val current = getChildAdapterPosition(first)
    val p = abs(position - current)

    val distance = if (p > 2) (p - (p - 2)) * height
    else p * height

    (layoutManager as LinearLayoutManager).scrollToPositionWithOffset(
        position,
        distance
    )
}

inline fun <T : ViewDataBinding> Fragment.createBinding(
    inflater: LayoutInflater,
    @LayoutRes layoutId: Int,
    container: ViewGroup?,
    bind: (T.() -> Unit) = {}
): T {
    val binding: T = DataBindingUtil.inflate(inflater, layoutId, container, false)
    binding.lifecycleOwner = viewLifecycleOwner
    binding.bind()
    return binding
}