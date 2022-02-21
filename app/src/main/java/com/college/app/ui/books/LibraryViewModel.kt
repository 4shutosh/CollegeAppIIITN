package com.college.app.ui.books

import android.content.Context
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.concurrent.ExecutionException
import com.college.app.utils.extensions.toLiveData
import com.college.base.AppCoroutineDispatcher
import com.college.base.logger.CollegeLogger
import dagger.hilt.android.internal.Contexts.getApplication
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject


@HiltViewModel
class LibraryViewModel
@Inject constructor(
    @ApplicationContext
    private val applicationContext: Context,
    private val logger: CollegeLogger,
    private val appCoroutineDispatcher: AppCoroutineDispatcher,
) : ViewModel() {

    private val _viewList = MutableLiveData(mutableListOf<Pair<Int, Any>>())
    val viewList = _viewList.toLiveData()

    private var cameraProviderLiveData: MutableLiveData<ProcessCameraProvider>? = null

    val processCameraProvider: LiveData<ProcessCameraProvider>
        get() {
            if (cameraProviderLiveData == null) {
                cameraProviderLiveData = MutableLiveData()

                val cameraProviderFuture =
                    ProcessCameraProvider.getInstance(getApplication(applicationContext))
                cameraProviderFuture.addListener(
                    Runnable {
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

    init {

    }

    companion object {

    }

    private fun populateData() {
        val list = mutableListOf<Pair<Int, Any>>()
        _viewList.postValue(list)
    }


}