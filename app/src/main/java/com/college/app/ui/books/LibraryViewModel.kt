package com.college.app.ui.books

import android.content.Context
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.college.app.data.repositories.DataStoreRepository
import com.college.app.models.local.CollegeBook
import com.college.app.network.library.GetBookUseCase
import com.college.app.network.library.IssueBookUseCase
import com.college.app.network.models.requests.IssueBookRequest
import com.college.app.utils.extensions.toLiveData
import com.college.base.AppCoroutineDispatcher
import com.college.base.SingleLiveEvent
import com.college.base.logger.CollegeLogger
import com.college.base.result.DataUiResult
import com.college.base.result.onError
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
    private val dataStoreRepository: DataStoreRepository,
) : ViewModel() {

//    todo check if the books is already issued: getting checked in the server but can be checked on front end as well ?

    private val _viewList = MutableLiveData(mutableListOf<Pair<Int, Any>>())
    val viewList = _viewList.toLiveData()

    private var cameraProviderLiveData: MutableLiveData<ProcessCameraProvider>? = null

    private val _scannedCollegeBook = MutableLiveData<DataUiResult<CollegeBook>?>()
    val scannedCollegeBook = _scannedCollegeBook.toLiveData()

    sealed class Command {
        object ShowBarcodeScannerFragment : Command()
    }

    val command = SingleLiveEvent<Command>()

    init {

    }

    companion object {

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
                _scannedCollegeBook.postValue(DataUiResult.Loading(true))
                getBookUseCase(libraryBookNumber).onSuccess {
                    _scannedCollegeBook.postValue(DataUiResult.Success(this))
                }.onError {
                    _scannedCollegeBook.postValue(DataUiResult.Error(this.exception))
                }
            }
    }

    fun barcodeScannerDialogClosed() {
        _scannedCollegeBook.value = null
    }

    fun actionIssueBookClicked() {
        command.postValue(Command.ShowBarcodeScannerFragment)
    }


}