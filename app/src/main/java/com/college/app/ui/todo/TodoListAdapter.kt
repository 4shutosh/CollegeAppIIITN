package com.college.app.ui.todo

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.college.app.databinding.ListItemTodoBinding
import com.college.app.utils.extensions.executeAfter

class TodoListAdapter constructor(private val clickListener: TodoItemClickListener) :
    ListAdapter<TodoListViewState, TodoListAdapter.ViewHolder>(
        TodoListDiffCallback()
    ) {

    private var lastExpandedItemPosition = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        ListItemTodoBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.binding.executeAfter {
            viewState = getItem(position)
        }
        holder.binding.listItemTodoBg.setOnClickListener {
            if (lastExpandedItemPosition != -1 && lastExpandedItemPosition != position) {
                val lastItem = getItem(lastExpandedItemPosition)
                lastItem.isExpanded = false
                notifyItemChanged(lastExpandedItemPosition)
            }
            lastExpandedItemPosition = holder.absoluteAdapterPosition

            currentItem.isExpanded = !currentItem.isExpanded
            notifyItemChanged(position)
            clickListener.onTodoItemClickListener(currentItem, position)
        }
    }

    interface TodoItemClickListener {
        fun onTodoItemClickListener(viewState: TodoListViewState, position: Int)
    }

    class ViewHolder(val binding: ListItemTodoBinding) : RecyclerView.ViewHolder(binding.root)
}

private class TodoListDiffCallback : DiffUtil.ItemCallback<TodoListViewState>() {
    override fun areItemsTheSame(oldItem: TodoListViewState, newItem: TodoListViewState) =
        oldItem.id == newItem.id

    override fun areContentsTheSame(
        oldItem: TodoListViewState,
        newItem: TodoListViewState
    ) = oldItem.title == newItem.title

}


