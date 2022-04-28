package com.college.app.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.college.app.data.repositories.DataStoreRepository
import com.college.app.utils.extensions.toLiveData
import com.college.base.AppCoroutineDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val dataStoreRepository: DataStoreRepository,
    private val appCoroutineDispatcher: AppCoroutineDispatcher,
) : ViewModel() {

    private val _userImageUrl = MutableLiveData(ViewState())
    private fun currentViewState() = _userImageUrl.value!!
    val userImageUrl = _userImageUrl.toLiveData()

    data class ViewState(
        val userName: String? = null,
        val userImageUrl: String? = null,
    )


    init {
        viewModelScope.launch(appCoroutineDispatcher.io) {
            val imageUrl = dataStoreRepository.getUserImageUrl().orEmpty()
            val userName = dataStoreRepository.getUserName().orEmpty()
            _userImageUrl.postValue(currentViewState().copy(userImageUrl = imageUrl,
                userName = userName))
        }
    }


}