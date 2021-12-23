package com.college.app.utils.extensions

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