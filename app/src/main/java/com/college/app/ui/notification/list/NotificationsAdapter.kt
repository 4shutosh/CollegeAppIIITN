package com.college.app.ui.notification.list

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.college.app.R
import com.college.app.databinding.ListItemNotificationsBinding
import com.college.app.utils.extensions.executeAfter
import com.college.app.utils.extensions.gone
import com.college.app.utils.extensions.visible


class NotificationsAdapter constructor(
    private val itemClickListener: ItemClickListener,
) : ListAdapter<NotificationsAdapter.NotificationsListViewState, NotificationsAdapter.NotificationsListViewHolder>(
    NotificationsListDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = NotificationsListViewHolder(
        ListItemNotificationsBinding.inflate(LayoutInflater.from(parent.context), parent, false),
        parent.context
    )

    override fun onBindViewHolder(holder: NotificationsListViewHolder, position: Int) {
        val item = getItem(position)
        holder.binding.executeAfter {
            viewState = item
            holder.setImage(item.url != null)
            if (item.url != null) {
                rootNotification.isClickable = true
                rootNotification.isFocusable = true
                rootNotification.setOnClickListener {
                    itemClickListener.onItemLinkClick(item.url)
                }
            }
        }
    }

    inner class NotificationsListViewHolder(
        val binding: ListItemNotificationsBinding,
        val context: Context,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun setImage(linkPresent: Boolean) {
            binding.notificationIcon.setImageDrawable(
                ContextCompat.getDrawable(context,
                    if (linkPresent) R.drawable.ic_link else R.drawable.ic_notifications_black_24dp)
            )
        }
    }

    data class NotificationsListViewState(
        val title: String,
        val message: String,
        val url: String? = null,
    )

    interface ItemClickListener {
        fun onItemLinkClick(url: String)
    }
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