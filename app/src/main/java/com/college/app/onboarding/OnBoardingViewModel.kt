package com.college.app.onboarding

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.college.app.data.DataStoreRepository
import com.college.base.AppCoroutineDispatcher
import com.college.base.logger.CollegeLogger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
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
        var startDestination: String = OnBoardingDestinations.SPLASH
    )

    var isReady = false

    val onBoardingViewState: MutableLiveData<OnBoardingViewState> = MutableLiveData()

    init {
        onBoardingViewState.value = OnBoardingViewState()

//        demoLogin()
        checkForLogin()
    }

    private fun currentOnBoardingViewState(): OnBoardingViewState = onBoardingViewState.value!!

    private fun demoLogin() {
        viewModelScope.launch {
            dataStoreRepository.setUserId(121)
            delay(100)
        }
    }

    private fun checkForLogin() {
        logger.d("checking for login")
        viewModelScope.launch(appCoroutineDispatcher.main) {
            onBoardingViewState.value = currentOnBoardingViewState().copy(
                startDestination = if (dataStoreRepository.getUserId() == null) {
                    OnBoardingDestinations.LOGIN
                } else OnBoardingDestinations.SPLASH,
                isReady = true
            )
            isReady = true
        }
    }

}