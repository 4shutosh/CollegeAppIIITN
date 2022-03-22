package com.college.app.network.library

import com.college.app.data.repositories.library.LibraryBooksRepository
import com.college.app.ui.books.list.LibraryIssuedBooksAdapter
import com.college.app.ui.books.list.LibraryListAdapter
import com.college.app.ui.books.list.LibraryListAdapter.LibraryListIssuedBookViewState
import com.college.app.utils.extensions.getFormattedDate
import com.college.base.AppCoroutineDispatcher
import com.college.base.domain.SuspendUseCase
import javax.inject.Inject

class GetIssuedBookUseCase @Inject constructor(
    appCoroutineDispatcher: AppCoroutineDispatcher,
    private val libraryBooksRepository: LibraryBooksRepository,
) : SuspendUseCase<String, LibraryListIssuedBookViewState>(
    appCoroutineDispatcher.io) {
    override suspend fun execute(parameters: String): LibraryListIssuedBookViewState {
        return LibraryListIssuedBookViewState(
            libraryBooksRepository.getIssuedBookForUser(parameters).map {
                LibraryIssuedBooksAdapter.LibraryListIssuedBookItemViewState(
                    it.book,
                    "Return Date: ${getFormattedDate(it.returnTimeStamp)}"
                )
            }
        )
    }
}