package com.college.app.Notification;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.college.app.R;
import com.college.app.databinding.FragmentNotificationsBinding;
import com.college.app.utils.SwipeToDeleteCallbackNotifications;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.snackbar.Snackbar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class NotificationsFragment extends Fragment {

    private static FragmentNotificationsBinding fragmentNotificationsBinding;
    public static NotificationAdapter notificationAdapter;
    private static LinearLayoutManager linearLayoutManager;
    private static RecyclerView recyclerView;
    private static ConstraintLayout notificationLayout;
    ConstraintLayout bsNotifications;
    BottomSheetBehavior bottomSheetBehaviorNotifications;
    private static List<Notification> notificationList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentNotificationsBinding = FragmentNotificationsBinding.inflate(inflater, container, false);
        EventBus.getDefault().register(this);
        setHasOptionsMenu(true);

        attachRecyclerView(getActivity());

        notificationLayout = fragmentNotificationsBinding.notificationsLayout;

        notificationAdapter.updateNotificationView();
        enableSwipeToDeleteAndUndo();

        bsNotifications = getActivity().findViewById(R.id.bottom_sheet_notifications);

        bottomSheetBehaviorNotifications = BottomSheetBehavior.from(bsNotifications);
        fragmentNotificationsBinding.cardViewNotiInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bottomSheetBehaviorNotifications.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                    bottomSheetBehaviorNotifications.setState(BottomSheetBehavior.STATE_EXPANDED);
                } else {
                    bottomSheetBehaviorNotifications.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }

            }
        });
        return fragmentNotificationsBinding.getRoot();

        //
    }

    @Subscribe
    public void onEvent(Notification notification) {
//        Notification n = FirebaseDataReceiver.getNotification();
//        if (n != null) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "run: success");
                notificationAdapter.addNotification(notification.getTitle(), notification.getDescription());
                notificationAdapter.notifyDataSetChanged();
            }
        });
    }

    private void enableSwipeToDeleteAndUndo() {

        SwipeToDeleteCallbackNotifications swipeToDeleteCallback = new SwipeToDeleteCallbackNotifications(getContext()) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {


                final int position = viewHolder.getAdapterPosition();
                final Notification item = notificationAdapter.getData().get(position);

                notificationAdapter.removeItem(position);

                Snackbar snackbar = Snackbar
                        .make(notificationLayout, "Item was removed from the list.", Snackbar.LENGTH_LONG);
                snackbar.setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        notificationAdapter.restoreItem(item, position);
                        recyclerView.scrollToPosition(position);
                    }
                });
                snackbar.setDuration(1000);
                snackbar.setActionTextColor(Color.GREEN);
                snackbar.show();

            }
        };

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchhelper.attachToRecyclerView(recyclerView);
    }

    public static void attachRecyclerView(Context context) {
        ////////////////////////Recycler View
        recyclerView = fragmentNotificationsBinding.notificationRecyclerView;
        notificationAdapter = new NotificationAdapter(context, notificationList);
        linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setAdapter(notificationAdapter);
        Log.d(TAG, "attachRecyclerView: view attached");
        ////////////////////////
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.notifications_fragment_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.notification_remove_all:
                notificationAdapter.removeAll();
        }
        return super.onOptionsItemSelected(item);

    }
}
