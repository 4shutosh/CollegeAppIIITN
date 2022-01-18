package com.college.app.ui.todo

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.college.app.databinding.ListItemTodoCompleteBinding
import com.college.app.databinding.ListItemTodoIncompleteBinding
import com.college.app.utils.extensions.executeAfter
import com.college.app.utils.extensions.strike

class TodoListAdapter constructor(private val clickListener: TodoItemClickListener) :
    ListAdapter<TodoListViewState, RecyclerView.ViewHolder>(TodoListDiffCallback) {

    private var lastExpandedItemPosition = -1 // no position

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TODO_LIST_VIEW_TYPE_COMPLETE -> ViewHolderItemComplete(
                ListItemTodoCompleteBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )
            TODO_LIST_VIEW_TYPE_INCOMPLETE -> ViewHolderItemIncomplete(
                ListItemTodoIncompleteBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )
            else -> throw Exception("TodoListAdapter: Wrong View Type found!")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentItem = getItem(position)
        when (getItemViewType(position)) {
            TODO_LIST_VIEW_TYPE_COMPLETE -> {
                val viewHolder = holder as ViewHolderItemComplete
                viewHolder.binding.executeAfter {
                    viewState = currentItem
                    itemPosition = position
                    actionHandler = clickListener
                }

                viewHolder.binding.listItemTodoBg.setOnClickListener {
                    if (lastExpandedItemPosition != -1 && lastExpandedItemPosition != position
                        && lastExpandedItemPosition < currentList.size
                    ) {
                        val lastItem = getItem(lastExpandedItemPosition)
                        lastItem.isExpanded = false
                        notifyItemChanged(lastExpandedItemPosition)
                    }
                    lastExpandedItemPosition = if (currentItem.isExpanded) -1
                    else viewHolder.absoluteAdapterPosition

                    currentItem.isExpanded = (!currentItem.isExpanded)
                    clickListener.onTodoItemClick(currentItem, position)
                }

                viewHolder.binding.listItemTodoTitle.strike = true
            }
            TODO_LIST_VIEW_TYPE_INCOMPLETE -> {
                val viewHolder = holder as ViewHolderItemIncomplete

                viewHolder.binding.executeAfter {
                    viewState = currentItem
                    itemPosition = position
                    actionHandler = clickListener
                }

                viewHolder.binding.listItemTodoBg.setOnClickListener {
                    if (lastExpandedItemPosition != -1 && lastExpandedItemPosition != position
                        && lastExpandedItemPosition < currentList.size
                    ) {
                        val lastItem = getItem(lastExpandedItemPosition)
                        lastItem.isExpanded = false
                        notifyItemChanged(lastExpandedItemPosition)
                    }
                    lastExpandedItemPosition = if (currentItem.isExpanded) -1
                    else viewHolder.absoluteAdapterPosition

                    currentItem.isExpanded = (!currentItem.isExpanded)
                    clickListener.onTodoItemClick(currentItem, position)
                }

                viewHolder.binding.listItemTodoNotifySwitch.setOnCheckedChangeListener { _, notify ->
                    clickListener.onTodoItemNotifyClicked(currentItem, notify)
                }
            }
        }
    }

    override fun getItemViewType(position: Int) =
        if (getItem(position).isCompleted) TODO_LIST_VIEW_TYPE_COMPLETE
        else TODO_LIST_VIEW_TYPE_INCOMPLETE

    inner class ViewHolderItemComplete(val binding: ListItemTodoCompleteBinding) :
        RecyclerView.ViewHolder(binding.root)

    inner class ViewHolderItemIncomplete(val binding: ListItemTodoIncompleteBinding) :
        RecyclerView.ViewHolder(binding.root)

    companion object {
        const val TODO_LIST_VIEW_TYPE_INCOMPLETE = 0
        const val TODO_LIST_VIEW_TYPE_COMPLETE = 1
    }

    interface TodoItemClickListener {
        fun onTodoItemClick(viewState: TodoListViewState, position: Int)

        fun onTodoItemDelete(viewState: TodoListViewState, position: Int)

        fun onTodoItemEdit(viewState: TodoListViewState, position: Int)
        fun onTodoItemEditDate(viewState: TodoListViewState, position: Int)
        fun onTodoItemEditTime(viewState: TodoListViewState, position: Int)

        fun onTodoItemNotifyClicked(viewState: TodoListViewState, notify: Boolean)

        fun onTodoItemMarkAsDone(viewState: TodoListViewState, position: Int)
    }
}

object TodoListDiffCallback : DiffUtil.ItemCallback<TodoListViewState>() {
    override fun areItemsTheSame(oldItem: TodoListViewState, newItem: TodoListViewState) =
        oldItem.id == newItem.id

    override fun areContentsTheSame(
        oldItem: TodoListViewState,
        newItem: TodoListViewState
    ) = oldItem == newItem

}

enum class TodoListFilterTypes(val title: String) {
    ALL("All"),
    TODAY("Today"),
    WEEK("Week"),
    MONTH("Month"),
    LATER("Later"),
    DEAD("DEAD")
}


