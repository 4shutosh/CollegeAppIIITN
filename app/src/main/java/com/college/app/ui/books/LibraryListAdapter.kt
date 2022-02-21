package com.college.app.ui.books

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.college.app.databinding.ListItemIssueBookBinding

class LibraryListAdapter(
    private val itemClickListener: LibraryListOnItemTouchListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val items = arrayListOf<Pair<Int, Any>>()

    init {
        items.add(Pair(ISSUE_A_BOOK, Unit))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ISSUE_A_BOOK -> {
                IssueBookViewHolder(
                    ListItemIssueBookBinding.inflate(
                        LayoutInflater.from(
                            parent.context
                        ), parent, false
                    )
                )
            }
//            ISSUED_BOOKS_HEADER -> {}
//            ISSUED_BOOKS -> {}
            else -> throw Exception("No valid view type found for ${this.javaClass.name}")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            ISSUE_A_BOOK -> {
                val h = holder as IssueBookViewHolder
                h.binding.rootListItemIssueBook.setOnClickListener {
                    itemClickListener.issueBookClicked()
                }
            }
        }
    }

    override fun getItemCount() = items.size

    override fun getItemViewType(position: Int) = items[position].first

    inner class IssueBookViewHolder(val binding: ListItemIssueBookBinding) :
        RecyclerView.ViewHolder(binding.root)

    companion object {
        const val ISSUE_A_BOOK = 0
        const val ISSUED_BOOKS_HEADER = 1
        const val ISSUED_BOOKS = 2
    }

    interface LibraryListOnItemTouchListener{
        fun issueBookClicked()
    }
}