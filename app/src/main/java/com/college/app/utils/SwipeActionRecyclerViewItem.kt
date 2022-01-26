package com.college.app.utils

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
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

    private val swipeRightBackground by lazy {
        ColorDrawable(
            ContextCompat.getColor(
                context,
                swipeRightBgColor
            )
        )
    }
    private val swipeLeftBackground by lazy {
        ColorDrawable(
            ContextCompat.getColor(
                context,
                swipeLeftBgColor
            )
        )
    }

    private val swipeRightDr by lazy { ContextCompat.getDrawable(context, swipeRightIcon) }
    private val swipeLeftDr by lazy { ContextCompat.getDrawable(context, swipeLeftIcon) }

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
        val isCancelled = (dX == 0f) && !isCurrentlyActive
        if (isCancelled) {
            clearCanvas(
                c,
                itemView.left.toFloat(),
                itemView.top.toFloat(),
                itemView.right.toFloat(),
                itemView.bottom.toFloat()
            )
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            return
        }

        when {
            dX > 0 -> {
                swipeRightBackground.setBounds(
                    itemView.left,
                    itemView.top,
                    itemView.right + dX.toInt(),
                    itemView.bottom
                )
                swipeRightBackground.draw(c)

                swipeLeftDr?.let { drawable ->
                    val itemTop: Int =
                        itemView.top + (itemView.height - drawable.intrinsicHeight) / 2
                    val itemMargin: Int = (itemView.height - drawable.intrinsicHeight) / 2
                    val itemLeft = itemView.left + itemMargin
                    val itemRight: Int = itemView.left + itemMargin + drawable.intrinsicWidth
                    val itemBottom: Int = itemTop + drawable.intrinsicHeight

                    drawable.setBounds(itemLeft, itemTop, itemRight, itemBottom)
                    drawable.draw(c)
                }

            }
            dX < 0 -> {
                swipeLeftBackground.setBounds(
                    itemView.left + dX.toInt(),
                    itemView.top,
                    itemView.right,
                    itemView.bottom
                )
                swipeLeftBackground.draw(c)

                swipeRightDr?.let { drawable ->
                    val itemTop: Int =
                        itemView.top + (itemView.height - drawable.intrinsicHeight) / 2
                    val itemMargin: Int = (itemView.height - drawable.intrinsicHeight) / 2
                    val itemLeft: Int = itemView.right - itemMargin - drawable.intrinsicWidth
                    val itemRight = itemView.right - itemMargin
                    val itemBottom: Int = itemTop + drawable.intrinsicHeight
                    drawable.setBounds(itemLeft, itemTop, itemRight, itemBottom)
                    drawable.draw(c)
                }
            }
            else -> {
                swipeLeftBackground.setBounds(0, 0, 0, 0)
                swipeRightBackground.setBounds(0, 0, 0, 0)
            }
        }

    }

    private fun clearCanvas(
        c: Canvas,
        left: Float,
        top: Float,
        right: Float,
        bottom: Float
    ) {
        val clearPaint = Paint()
        clearPaint.color = Color.TRANSPARENT
        clearPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SCREEN)
        c.drawRect(left, top, right, bottom, clearPaint)
    }

    override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder) = SWIPE_THRESHOLD
}