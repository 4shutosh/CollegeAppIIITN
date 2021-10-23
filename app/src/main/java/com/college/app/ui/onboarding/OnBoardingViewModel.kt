package com.college.app.ui.onboarding

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.college.app.data.DataStoreRepository
import com.college.base.AppCoroutineDispatcher
import com.college.base.logger.CollegeLogger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnBoardingViewModel @Inject constructor(
    private val appCoroutineDispatcher: AppCoroutineDispatcher,
    private val dataStoreRepository: DataStoreRepository,
    private val logger: CollegeLogger
) : ViewModel() {

    data class OnBoardingViewState(
        var isReady: Boolean = false,
        var loggedIn: Boolean = false,
    )

    val onBoardingViewState: MutableLiveData<OnBoardingViewState> = MutableLiveData()

    init {
        onBoardingViewState.value = OnBoardingViewState()
        checkForLogin()
    }

    private fun currentOnBoardingViewState(): OnBoardingViewState = onBoardingViewState.value!!

    private fun checkForLogin() {
        logger.d("checking for login")
        viewModelScope.launch(appCoroutineDispatcher.main) {
            onBoardingViewState.value = currentOnBoardingViewState().copy(
                loggedIn = dataStoreRepository.getUserId() != null,
                isReady = true
            )
        }
    }

}