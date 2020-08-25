package com.college.app.Notification;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.college.app.AppClass;
import com.college.app.MainActivity;
import com.college.app.R;
import com.college.app.databinding.NotificationItemBinding;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.List;

import io.objectbox.Box;
import io.objectbox.BoxStore;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    private Context mContext;
    private List<Notification> notificationList;
    public static BoxStore notificationBoxStore;
    public static Box<Notification> notificationAdapterBox;

    public NotificationAdapter(Context mContext, List<Notification> mData) {
        this.mContext = mContext;
        this.notificationList = mData;
        notificationBoxStore = ((AppClass) ((MainActivity) mContext).getApplication()).getBoxStore();
        notificationAdapterBox = notificationBoxStore.boxFor(Notification.class);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private NotificationItemBinding notificationItemBinding;

        ViewHolder(NotificationItemBinding binding) {
            super(binding.getRoot());
            this.setIsRecyclable(false);
            this.notificationItemBinding = binding;
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        NotificationItemBinding itemBinding = NotificationItemBinding.inflate(layoutInflater, parent, false);
        return new ViewHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.notificationItemBinding.notificaitonItemTitle.setText(notificationList.get(position).getTitle());
        holder.notificationItemBinding.notificationItemMessage.setText(notificationList.get(position).getDescription());
    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }


    private void setNotifications(List<Notification> list) {
        notificationList = list;
        notifyDataSetChanged();
    }

    public void updateNotificationView() {
        List<Notification> nList = notificationAdapterBox.query().build().find();
        this.setNotifications(nList);
        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        if (!notificationList.isEmpty()) {
            Notification notification = getNotificationById(notificationList.get(position).getId());
            if (notification != null) {
                notificationAdapterBox.remove(notification.getId());
            }
            notificationList.remove(position);
        }
        updateNotificationView();
        notifyItemRemoved(position);
    }

    private Notification getNotificationById(long id) {
        return notificationAdapterBox.query().equal(Notification_.id, id).build().findUnique();
    }

    public void restoreItem(Notification item, int position) {
        notificationList.add(position, item);
        if (item != null) {
            notificationAdapterBox.put(item);
        }
        updateNotificationView();
        notifyItemInserted(position);
    }

    public List<Notification> getData() {
        return notificationList;
    }

    public void addNotification(String title, String description) {
        notificationAdapterBox.put(new Notification(0, title, description));
        updateNotificationView();
    }

    public void removeAll() {
        if (!notificationAdapterBox.isEmpty()) {
            new MaterialAlertDialogBuilder(mContext, R.style.MaterialAlertDialog)
                    .setTitle(mContext.getString(R.string.delete_all_noticiations))
                    .setPositiveButton(mContext.getString(R.string.yes), (dialog, which) -> {
                        notificationAdapterBox.removeAll();
                        updateNotificationView();
                        Toast.makeText(mContext, "Deleted!", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton(mContext.getString(R.string.cancel), (dialog, which) -> {
                    })
                    .show();
        } else {
            new MaterialAlertDialogBuilder(mContext, R.style.MaterialAlertDialog)
                    .setTitle(mContext.getString(R.string.no_notifications_found))
                    .setPositiveButton(mContext.getString(R.string.okay), (dialog, which) -> {

                    })
                    .show();
        }

    }

}
