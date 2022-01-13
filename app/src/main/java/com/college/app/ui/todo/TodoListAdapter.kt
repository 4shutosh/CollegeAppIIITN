package com.college.app.ui.todo

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.college.app.databinding.ListItemTodoBinding
import com.college.app.utils.extensions.executeAfter

class TodoListAdapter constructor(private val clickListener: TodoItemClickListener) :
    ListAdapter<TodoListViewState, TodoListAdapter.ViewHolder>(TodoListDiffCallback) {

    private var lastExpandedItemPosition = -1 // no position

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        ListItemTodoBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.binding.executeAfter {
            viewState = currentItem
            itemPosition = position
            actionHandler = clickListener
        }

        holder.binding.listItemTodoBg.setOnClickListener {
            if (lastExpandedItemPosition != -1 && lastExpandedItemPosition != position
                && lastExpandedItemPosition < currentList.size
            ) {
                val lastItem = getItem(lastExpandedItemPosition)
                lastItem.isExpanded = false
                notifyItemChanged(lastExpandedItemPosition)
            }
            lastExpandedItemPosition = if (currentItem.isExpanded) -1
            else holder.absoluteAdapterPosition

            currentItem.isExpanded = (!currentItem.isExpanded)
            clickListener.onTodoItemClick(currentItem, position)
        }
    }

    interface TodoItemClickListener {
        fun onTodoItemClick(viewState: TodoListViewState, position: Int)
        fun onTodoItemDelete(viewState: TodoListViewState, position: Int)
        fun onTodoItemEdit(viewState: TodoListViewState, position: Int)
        fun onTodoItemEditDate(viewState: TodoListViewState, position: Int)
        fun onTodoItemEditTime(viewState: TodoListViewState, position: Int)
    }

    inner class ViewHolder(val binding: ListItemTodoBinding) :
        RecyclerView.ViewHolder(binding.root)
}

object TodoListDiffCallback : DiffUtil.ItemCallback<TodoListViewState>() {
    override fun areItemsTheSame(oldItem: TodoListViewState, newItem: TodoListViewState) =
        oldItem.id == newItem.id

    override fun areContentsTheSame(
        oldItem: TodoListViewState,
        newItem: TodoListViewState
    ) = oldItem == newItem

}

enum class TodoListTypes(val title: String) {
    ALL("All"),
    TODAY("Today"),
    WEEK("Week"),
    MONTH("Month"),
    LATER("Later"),
    DEAD("DEAD")
}


