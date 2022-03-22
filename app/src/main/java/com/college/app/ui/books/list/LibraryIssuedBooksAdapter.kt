package com.college.app.ui.books.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.college.app.databinding.ListItemIssueBookItemBookBinding
import com.college.app.models.local.CollegeBook
import com.college.app.utils.extensions.executeAfter

class LibraryIssuedBooksAdapter constructor(
    private val itemClickListener: LibraryListAdapter.LibraryListOnItemTouchListener,
) : ListAdapter<LibraryIssuedBooksAdapter.LibraryListIssuedBookItemViewState, LibraryIssuedBooksAdapter.IssuedBookItemViewHolder>(
    LibraryIssuedBookDiffer
) {

    data class LibraryListIssuedBookItemViewState(
        val book: CollegeBook,
        val returnTimeStamp: String,
    )

    inner class IssuedBookItemViewHolder(
        val binding: ListItemIssueBookItemBookBinding,
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = IssuedBookItemViewHolder(
        ListItemIssueBookItemBookBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: IssuedBookItemViewHolder, position: Int) {
        val item = getItem(position)
        holder.binding.executeAfter {
            listItemBookItemTitle.text = item.book.bookName
            listItemBookItemReturnDate.text = item.returnTimeStamp
            rootView.setOnClickListener {
                itemClickListener.issuedBookItemClicked()
            }
        }
    }

    object LibraryIssuedBookDiffer : DiffUtil.ItemCallback<LibraryListIssuedBookItemViewState>() {
        override fun areItemsTheSame(
            oldItem: LibraryListIssuedBookItemViewState,
            newItem: LibraryListIssuedBookItemViewState,
        ) = oldItem == newItem

        override fun areContentsTheSame(
            oldItem: LibraryListIssuedBookItemViewState,
            newItem: LibraryListIssuedBookItemViewState,
        ) = oldItem == newItem
    }
}