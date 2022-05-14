package com.college.app.ui.notification.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.college.app.databinding.ListItemNotificationsBinding
import com.college.app.utils.extensions.executeAfter


class NotificationsAdapter: ListAdapter<NotificationsAdapter.NotificationsListViewState, NotificationsAdapter.NotificationsListViewHolder>(
    NotificationsListDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = NotificationsListViewHolder(
        ListItemNotificationsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: NotificationsListViewHolder, position: Int) {
        val item = getItem(position)
        holder.binding.executeAfter {
            viewState = item
        }
    }

    inner class NotificationsListViewHolder(
        val binding: ListItemNotificationsBinding,
    ) : RecyclerView.ViewHolder(binding.root)

    data class NotificationsListViewState(
        val title: String,
        val message: String,
    )
}

object NotificationsListDiffCallback :
    DiffUtil.ItemCallback<NotificationsAdapter.NotificationsListViewState>() {
    override fun areItemsTheSame(
        oldItem: NotificationsAdapter.NotificationsListViewState,
        newItem: NotificationsAdapter.NotificationsListViewState,
    ) = oldItem == newItem

    override fun areContentsTheSame(
        oldItem: NotificationsAdapter.NotificationsListViewState,
        newItem: NotificationsAdapter.NotificationsListViewState,
    ) = oldItem == newItem

}