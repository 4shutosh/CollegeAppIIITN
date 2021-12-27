package com.college.app.utils

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.drawable.ColorDrawable
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView


class SwipeActionRecyclerViewItem(
    private val context: Context,
    @DrawableRes private val swipeRightIcon: Int,
    @DrawableRes private val swipeLeftIcon: Int,
    @ColorRes private val swipeRightBgColor: Int,
    @ColorRes private val swipeLeftBgColor: Int,
    private val swipeActionCallback: SwipeActionCallback
) : ItemTouchHelper.Callback() {

    companion object {
        // if the row is swiped less than 70%, the onSwipe method won’t be triggered
        private const val SWIPE_THRESHOLD = 0.7f
    }

    private val swipeRightBackground by lazy { ColorDrawable() }
    private val swipeLeftBackground by lazy { ColorDrawable() }

    init {
        swipeRightBackground.color = ContextCompat.getColor(context, swipeRightBgColor)
        swipeLeftBackground.color = ContextCompat.getColor(context, swipeLeftBgColor)
    }

    interface SwipeActionCallback {
        fun onLeftSwipe(position: Int)
        fun onRightSwipe(position: Int)
    }

    override fun isLongPressDragEnabled() = false

    override fun isItemViewSwipeEnabled() = true

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        val swipeFlags = ItemTouchHelper.START or ItemTouchHelper.END
        return makeMovementFlags(0, swipeFlags)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ) = false

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        when (direction) {
            ItemTouchHelper.START -> swipeActionCallback.onLeftSwipe(viewHolder.absoluteAdapterPosition)
            ItemTouchHelper.END -> swipeActionCallback.onRightSwipe(viewHolder.absoluteAdapterPosition)
        }
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)

        val itemView = viewHolder.itemView
        val height = itemView.height
        val isCancelled = (dX == 0f) && !isCurrentlyActive
        if (isCancelled) {
            clearCanvas(
                c,
                itemView.left.toFloat() + dX,
                itemView.top.toFloat(),
                itemView.right.toFloat(),
                itemView.bottom.toFloat()
            )
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            return
        }


    }

    private fun clearCanvas(
        c: Canvas,
        left: Float,
        top: Float,
        right: Float,
        bottom: Float
    ) {
        c.drawRect(left, top, right, bottom, Paint())
    }

    override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder) = SWIPE_THRESHOLD
}