package com.college.app.ui.books

import android.content.Context
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.college.app.data.entities.TodoItem
import com.college.app.data.repositories.DataStoreRepository
import com.college.app.data.repositories.todo.TodoRepository
import com.college.app.models.local.CollegeBook
import com.college.app.models.network.requests.IssueBookRequest
import com.college.app.network.library.GetBookUseCase
import com.college.app.network.library.GetIssuedBookUseCase
import com.college.app.network.library.IssueBookUseCase
import com.college.app.ui.books.list.LibraryListAdapter
import com.college.app.utils.extensions.getFormattedDate
import com.college.base.AppCoroutineDispatcher
import com.college.base.SingleLiveEvent
import com.college.base.logger.CollegeLogger
import com.college.base.result.DataUiResult
import com.college.base.result.onError
import com.college.base.result.onServerError
import com.college.base.result.onSuccess
import dagger.hilt.android.internal.Contexts.getApplication
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import java.util.concurrent.ExecutionException
import javax.inject.Inject


@HiltViewModel
class LibraryViewModel
@Inject constructor(
    @ApplicationContext
    private val applicationContext: Context,
    private val logger: CollegeLogger,
    private val appCoroutineDispatcher: AppCoroutineDispatcher,
    private val issueBookUseCase: IssueBookUseCase,
    private val getBookUseCase: GetBookUseCase,
    private val getIssuedBooksUseCase: GetIssuedBookUseCase,
    private val todoRepository: TodoRepository,
    private val dataStoreRepository: DataStoreRepository,
) : ViewModel() {

//    todo check if the books is already issued: getting checked in the server but can be checked on front end as well ?

    private var cameraProviderLiveData: MutableLiveData<ProcessCameraProvider>? = null

    data class LibraryListViewState(
        val isLoading: Boolean = false,
        val viewList: MutableList<Any> = mutableListOf(),
    )

    data class BarcodeScannerViewState(
        val issueBookButtonEnabled: Boolean = false,
        val scannedCollegeBook: DataUiResult<CollegeBook>? = null,
    )

    val barcodeScannerViewState = MutableLiveData(BarcodeScannerViewState())

    val libraryListViewState: MutableLiveData<LibraryListViewState> =
        MutableLiveData(LibraryListViewState())

    private fun currentLibraryListViewState() = libraryListViewState.value!!
    private fun currentBarcodeScannerViewState() = barcodeScannerViewState.value!!

    sealed class Command {
        object ShowBarcodeScannerFragment : Command()
        object CloseBarcodeScannerFragment : Command()
        class ShowToastMessage(val message: String) : Command()
    }

    val command = SingleLiveEvent<Command>()

    init {
        populateInitialData()
        getIssuedBooks()
    }

    private fun populateInitialData() {
        viewModelScope.launch(appCoroutineDispatcher.io) {
            val newList = currentLibraryListViewState().viewList.apply {
                add(LibraryListAdapter.LibraryListIssueABookViewState)
                add(LibraryListAdapter.LibraryListReturnABookViewState)
            }
            libraryListViewState.postValue(currentLibraryListViewState().copy(viewList = newList))
        }
    }

    fun getIssuedBooks() {
        viewModelScope.launch(appCoroutineDispatcher.io) {
            libraryListViewState.postValue(currentLibraryListViewState().copy(isLoading = true))
            dataStoreRepository.getUserEmail()?.let { email ->
                getIssuedBooksUseCase(email).onSuccess {
                    currentLibraryListViewState().viewList.removeIf { it is LibraryListAdapter.LibraryListIssuedBookViewState }
                    currentLibraryListViewState().viewList.add(this)
                    libraryListViewState.postValue(currentLibraryListViewState().copy(
                        isLoading = false))
                }
            }
        }
    }

    val processCameraProvider: LiveData<ProcessCameraProvider>
        get() {
            if (cameraProviderLiveData == null) {
                cameraProviderLiveData = MutableLiveData()

                val cameraProviderFuture =
                    ProcessCameraProvider.getInstance(getApplication(applicationContext))
                cameraProviderFuture.addListener(
                    {
                        try {
                            cameraProviderLiveData!!.setValue(cameraProviderFuture.get())
                        } catch (e: ExecutionException) {
                            // Handle any errors (including cancellation) here.
                            logger.e("Unhandled exception", e)
                        } catch (e: InterruptedException) {
                            logger.e("Unhandled exception", e)
                        }
                    },
                    ContextCompat.getMainExecutor(getApplication(applicationContext))
                )
            }
            return cameraProviderLiveData!!
        }


    fun issueABook(libraryBookNumber: Long) {
        if (libraryBookNumber != 0L)
            viewModelScope.launch(appCoroutineDispatcher.io) {
                dataStoreRepository.getUserId()?.let {
                    issueBookUseCase(IssueBookRequest(it, libraryBookNumber)).onSuccess {
                        logger.d(this.toString())
                    }
                }
            }
    }

    fun getBookByLibraryNumber(libraryBookNumber: Long) {
        if (libraryBookNumber != 0L)
            viewModelScope.launch(appCoroutineDispatcher.io) {
                barcodeScannerViewState.postValue(
                    currentBarcodeScannerViewState().copy(
                        scannedCollegeBook = DataUiResult.Loading(true)
                    )
                )
                getBookUseCase(libraryBookNumber).onSuccess {
                    barcodeScannerViewState.postValue(
                        currentBarcodeScannerViewState().copy(
                            issueBookButtonEnabled = this.isAvailableToIssue,
                            scannedCollegeBook = DataUiResult.Success(this)
                        )
                    )
                }.onServerError {
                    barcodeScannerViewState.postValue(
                        currentBarcodeScannerViewState().copy(
                            scannedCollegeBook = DataUiResult.Error(this)
                        )
                    )
                }
            }
    }

    fun barcodeScannerDialogClosed() {
        barcodeScannerViewState.postValue(
            BarcodeScannerViewState()
        )
    }

    fun actionIssueBookClicked() {
        command.postValue(Command.ShowBarcodeScannerFragment)
    }

    fun actionIssueBookButtonClicked(libraryBookNumber: Long) {
        viewModelScope.launch(appCoroutineDispatcher.io) {
            dataStoreRepository.getUserId()?.let { userId ->
                issueBookUseCase(IssueBookRequest(
                    userId = userId,
                    libraryBookNumber = libraryBookNumber
                )).onSuccess {
                    val bookInserted =
                        userBookList.find { it.book.libraryBookNumber == libraryBookNumber }

                    if (bookInserted != null) {
                        // check the issued book return stamp date here
                        todoRepository.insertTodo(
                            TodoItem(
                                name = "Return Book ${bookInserted.book.bookName}",
                                description = "Return Book by ${bookInserted.book.bookName} visiting the library, on or before the return date of ${
                                    getFormattedDate(bookInserted.returnTimeStamp)
                                }",
                                timeStampMilliSeconds = bookInserted.returnTimeStamp,
                                notify = true
                            )
                        )
                        command.postValue(Command.CloseBarcodeScannerFragment)
                        command.postValue(Command.ShowToastMessage("Book Issued! and Return TODO Added!"))
                        getIssuedBooks()
                    }
                }.onError {
                    logger.d(this.exception)
                }
            }
        }
    }


}