package com.college.app.ui.books.list

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.college.app.R
import com.college.app.databinding.*
import com.college.app.ui.books.list.LibraryIssuedBooksAdapter.LibraryListIssuedBookItemViewState
import com.college.app.utils.extensions.gone
import com.college.app.utils.extensions.visible

class LibraryListAdapter(
    private val itemClickListener: LibraryListOnItemTouchListener,
) : ListAdapter<Any, RecyclerView.ViewHolder>(LibraryListAdapterDiff) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ISSUE_A_BOOK -> {
                IssueBookViewHolder(
                    ListItemIssueBookBinding.inflate(
                        LayoutInflater.from(parent.context), parent, false
                    )
                )
            }
            ISSUED_BOOKS -> {
                IssuedBookViewHolder(
                    ListItemIssuedBooksBinding.inflate(
                        LayoutInflater.from(parent.context), parent, false
                    ), itemClickListener
                )
            }
            RETURN_A_BOOK -> {
                ReturnBookViewHolder(
                    ListItemReturnBookBinding.inflate(
                        LayoutInflater.from(parent.context), parent, false
                    )
                )
            }
            else -> throw Exception("No valid view holder found for ${this.javaClass.name}")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            ISSUE_A_BOOK -> {
                val h = holder as IssueBookViewHolder
                h.binding.listItemIssueBookTitle.setOnClickListener {
                    itemClickListener.issueBookClicked()
                }
            }
            ISSUED_BOOKS -> {
                val viewHolder = holder as IssuedBookViewHolder
                val item = getItem(position) as LibraryListIssuedBookViewState
                viewHolder.bind(item)
            }
            RETURN_A_BOOK -> {
                val viewHolder = holder as ReturnBookViewHolder
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is LibraryListIssueABookViewState -> ISSUE_A_BOOK
            is LibraryListIssuedBookViewState -> ISSUED_BOOKS
            is LibraryListReturnABookViewState -> RETURN_A_BOOK
            else -> throw Exception("No valid view type found for ${getItem(position).javaClass}")
        }
    }

    object LibraryListIssueABookViewState
    object LibraryListReturnABookViewState
    data class LibraryListIssuedBookViewState(
        val listOfBooks: MutableList<LibraryListIssuedBookItemViewState> = mutableListOf(),
    )

    inner class IssueBookViewHolder(val binding: ListItemIssueBookBinding) :
        RecyclerView.ViewHolder(binding.root)

    inner class ReturnBookViewHolder(val binding: ListItemReturnBookBinding) :
        RecyclerView.ViewHolder(binding.root)

    inner class IssuedBookViewHolder(
        val binding: ListItemIssuedBooksBinding,
        listener: LibraryListOnItemTouchListener,
    ) : RecyclerView.ViewHolder(binding.root) {

        private val adapter = LibraryIssuedBooksAdapter(listener)

        init {
            binding.listItemIssueBookRv.adapter = adapter
        }

        fun bind(viewState: LibraryListIssuedBookViewState) {
            if (viewState.listOfBooks.isEmpty()) {
                binding.noBooksFoundGroup.visible()
                binding.listItemIssueBookRv.gone()
            } else {
                adapter.submitList(viewState.listOfBooks)
                binding.noBooksFoundGroup.gone()
                binding.listItemIssueBookRv.visible()
            }
        }
    }


    object LibraryListAdapterDiff : DiffUtil.ItemCallback<Any>() {
        override fun areItemsTheSame(oldItem: Any, newItem: Any) = oldItem == newItem

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: Any, newItem: Any) = oldItem == newItem
    }

    companion object {
        const val ISSUE_A_BOOK = R.layout.list_item_issue_book
        const val ISSUED_BOOKS = R.layout.list_item_issued_books
        const val RETURN_A_BOOK = R.layout.list_item_return_book
    }

    interface LibraryListOnItemTouchListener {
        fun issueBookClicked()
        fun issuedBookItemClicked()
    }
}