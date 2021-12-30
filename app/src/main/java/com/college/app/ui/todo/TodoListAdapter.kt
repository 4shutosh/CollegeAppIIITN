package com.college.app.ui.todo

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.college.app.databinding.ListItemTodoBinding
import com.college.app.utils.extensions.executeAfter
import timber.log.Timber

class TodoListAdapter constructor(private val clickListener: TodoItemClickListener) :
    RecyclerView.Adapter<TodoListAdapter.ViewHolder>(),
    AsyncListDiffer.ListListener<TodoListViewState> {

    private val differ by lazy { AsyncListDiffer(this, TodoListDiffCallback) }

    init {
        differ.addListListener(this)
    }

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
            Timber.d("onBindViewHolder: $lastExpandedItemPosition current positoin $position")

            if (lastExpandedItemPosition != -1 && lastExpandedItemPosition != position
                && lastExpandedItemPosition < differ.currentList.size
            ) {

                val lastItem = getItem(lastExpandedItemPosition)
                lastItem.isExpanded = false
                notifyItemChanged(position)
            }

            lastExpandedItemPosition = if (currentItem.isExpanded) -1
            else holder.absoluteAdapterPosition

            currentItem.isExpanded = (!currentItem.isExpanded)
            clickListener.onTodoItemClick(currentItem, position)
        }
    }

    fun submitList(list: List<TodoListViewState>) {
        differ.submitList(list)
    }

    private fun getItem(position: Int) = differ.currentList[position]

    override fun getItemCount() = differ.currentList.size

    interface TodoItemClickListener {
        fun onTodoItemClick(viewState: TodoListViewState, position: Int)
        fun onTodoItemDelete(viewState: TodoListViewState, position: Int)
        fun onTodoItemEdit(viewState: TodoListViewState, position: Int)
    }

    inner class ViewHolder(val binding: ListItemTodoBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCurrentListChanged(
        previousList: MutableList<TodoListViewState>,
        currentList: MutableList<TodoListViewState>
    ) {
        Timber.d("prevList : $previousList")
        Timber.d("newList : $currentList")
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


