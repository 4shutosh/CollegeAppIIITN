package com.college.app.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.college.app.Notification.Notification;
import com.college.app.Notification.NotificationAdapter;
import com.college.app.R;
import com.google.android.material.snackbar.Snackbar;

import static android.content.ContentValues.TAG;

public class SwipeToDeleteCallbackNotifications extends ItemTouchHelper.Callback {

    Context mContext;
    private Paint mClearPaint;
    private ColorDrawable mBackground;
    private int backgroundColor;
    private Drawable deleteDrawable;
    private int intrinsicWidth;
    private int intrinsicHeight;
    NotificationAdapter notificationAdapter;


    public SwipeToDeleteCallbackNotifications(Context context) {
        mContext = context;
        mBackground = new ColorDrawable();
        backgroundColor = Color.parseColor("#b80f0a");
        mClearPaint = new Paint();
        mClearPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        deleteDrawable = ContextCompat.getDrawable(mContext, R.drawable.ic_delete_white_24dp);
        intrinsicWidth = deleteDrawable.getIntrinsicWidth();
        intrinsicHeight = deleteDrawable.getIntrinsicHeight();

    }


    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
        return makeMovementFlags(0, swipeFlags);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
        return false;
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

    private void clearCanvas(Canvas c, Float left, Float top, Float right, Float bottom) {
        Log.d(TAG, "clearCanvas: reached");
        c.drawRect(left, top, right, bottom, mClearPaint);

    }

    @Override
    public float getSwipeThreshold(@NonNull RecyclerView.ViewHolder viewHolder) {
        return 0.7f;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
//        final int position = viewHolder.getAdapterPosition();
//        final Notification item = notificationAdapter.getData().get(position);
//
//        notificationAdapter.removeItem(position);

    }

}
