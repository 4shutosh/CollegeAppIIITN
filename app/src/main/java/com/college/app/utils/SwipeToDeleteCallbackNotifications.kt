package com.college.app.utils

import android.content.ContentValues
import android.content.Context
import android.graphics.*
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.college.app.Notification.NotificationAdapter
import com.college.app.R

open class SwipeToDeleteCallbackNotifications(var mContext: Context?) : ItemTouchHelper.Callback() {
    private val mClearPaint: Paint
    private val mBackground: ColorDrawable
    private val backgroundColor: Int
    private val deleteDrawable: Drawable?
    private val intrinsicWidth: Int
    private val intrinsicHeight: Int
    var notificationAdapter: NotificationAdapter? = null
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
        viewHolder1: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    //    @Override
    //    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
    //        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    //
    //        View itemView = viewHolder.itemView;
    //        int itemHeight = itemView.getHeight();
    //
    //        boolean isCancelled = dX == 0 && !isCurrentlyActive;
    //
    //        if (isCancelled) {
    //            clearCanvas(c, itemView.getRight() + dX, (float) itemView.getTop(), (float) itemView.getRight(), (float) itemView.getBottom());
    //            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    //            return;
    //        }
    //
    //        mBackground.setColor(backgroundColor);
    //        mBackground.setBounds(itemView.getRight() + (int) dX, itemView.getTop(), itemView.getRight(), itemView.getBottom());
    //        mBackground.draw(c);
    //
    //        int deleteIconTop = itemView.getTop() + (itemHeight - intrinsicHeight) / 2;
    //        int deleteIconMargin = (itemHeight - intrinsicHeight) / 2;
    //        int deleteIconLeft = itemView.getRight() - deleteIconMargin - intrinsicWidth;
    //        int deleteIconRight = itemView.getRight() - deleteIconMargin;
    //        int deleteIconBottom = deleteIconTop + intrinsicHeight;
    //
    //
    //        deleteDrawable.setBounds(deleteIconLeft, deleteIconTop, deleteIconRight, deleteIconBottom);
    //        deleteDrawable.draw(c);
    //
    //        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    //
    //
    //    }
    private fun clearCanvas(c: Canvas, left: Float, top: Float, right: Float, bottom: Float) {
        Log.d(ContentValues.TAG, "clearCanvas: reached")
        c.drawRect(left, top, right, bottom, mClearPaint)
    }

    override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder): Float {
        return 0.7f
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
//        final int position = viewHolder.getAdapterPosition();
//        final Notification item = notificationAdapter.getData().get(position);
//
//        notificationAdapter.removeItem(position);
    }

    init {
        mBackground = ColorDrawable()
        backgroundColor = Color.parseColor("#b80f0a")
        mClearPaint = Paint()
        mClearPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
        deleteDrawable = ContextCompat.getDrawable(mContext!!, R.drawable.ic_delete_white_24dp)
        intrinsicWidth = deleteDrawable!!.intrinsicWidth
        intrinsicHeight = deleteDrawable.intrinsicHeight
    }
}