package com.college.app.network.library

import com.college.app.data.repositories.library.LibraryBooksRepository
import com.college.app.models.local.CollegeBook
import com.college.base.AppCoroutineDispatcher
import com.college.base.domain.SuspendUseCase
import javax.inject.Inject

class GetBookUseCase @Inject constructor(
    appCoroutineDispatcher: AppCoroutineDispatcher,
    private val libraryBooksRepository: LibraryBooksRepository,
) : SuspendUseCase<Long, CollegeBook>(appCoroutineDispatcher.io) {
    override suspend fun execute(parameters: Long): CollegeBook {
        return libraryBooksRepository.getBookByLibraryNumber(parameters)
    }
}