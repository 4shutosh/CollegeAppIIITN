package com.college.app.Notification

import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.college.app.AppClass
import com.college.app.MainActivity
import com.college.app.R
import com.college.app.databinding.NotificationItemBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import io.objectbox.Box
import io.objectbox.BoxStore

class NotificationAdapter(private val mContext: Context?, var data: ArrayList<Notification>) :
    RecyclerView.Adapter<NotificationAdapter.ViewHolder>() {
    class ViewHolder internal constructor(binding: NotificationItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val notificationItemBinding: NotificationItemBinding

        init {
            setIsRecyclable(false)
            notificationItemBinding = binding
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemBinding = NotificationItemBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.notificationItemBinding.notificaitonItemTitle.text = data[position].title
        holder.notificationItemBinding.notificationItemMessage.text =
            data[position].description
    }

    override fun getItemCount(): Int {
        return data.size
    }

    private fun setNotifications(list: ArrayList<Notification>) {
        data = list
        notifyDataSetChanged()
    }

    fun updateNotificationView() {
        val nList = notificationAdapterBox.query().build().find()
        setNotifications(nList as ArrayList<Notification>)
        notifyDataSetChanged()
    }

    fun removeItem(position: Int) {
        if (data.isNotEmpty()) {
            val notification = getNotificationById(
                data[position].id
            )
            if (notification != null) {
                notificationAdapterBox.remove(notification.id)
            }
            data.removeAt(position)
        }
        updateNotificationView()
        notifyItemRemoved(position)
    }

    private fun getNotificationById(id: Long): Notification? {
        return notificationAdapterBox.query().equal(Notification_.id, id).build().findUnique()
    }

    fun restoreItem(item: Notification, position: Int) {
        data.add(position, item)
        notificationAdapterBox.put(item)
        updateNotificationView()
        notifyItemInserted(position)
    }

    fun addNotification(title: String?, description: String?) {
        notificationAdapterBox.put(Notification(0, title, description))
        updateNotificationView()
    }

    fun removeAll() {
        if (!notificationAdapterBox.isEmpty) {
            MaterialAlertDialogBuilder(mContext!!, R.style.MaterialAlertDialog)
                .setTitle(mContext.getString(R.string.delete_all_noticiations))
                .setPositiveButton(mContext.getString(R.string.yes)) { dialog: DialogInterface?, which: Int ->
                    notificationAdapterBox.removeAll()
                    updateNotificationView()
                    Toast.makeText(mContext, "Deleted!", Toast.LENGTH_SHORT).show()
                }
                .setNegativeButton(mContext.getString(R.string.cancel)) { dialog: DialogInterface?, which: Int -> }
                .show()
        } else {
            MaterialAlertDialogBuilder(mContext!!, R.style.MaterialAlertDialog)
                .setTitle(mContext.getString(R.string.no_notifications_found))
                .setPositiveButton(mContext.getString(R.string.okay)) { dialog: DialogInterface?, which: Int -> }
                .show()
        }
    }

    companion object {
        private lateinit var notificationBoxStore: BoxStore
        lateinit var notificationAdapterBox: Box<Notification?>
    }

    init {
        notificationBoxStore = ((mContext as MainActivity?)!!.application as AppClass).boxStore!!
        notificationAdapterBox = notificationBoxStore.boxFor(
            Notification::class.java
        )
    }
}