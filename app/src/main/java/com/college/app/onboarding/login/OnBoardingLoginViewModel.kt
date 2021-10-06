package com.college.app.onboarding.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.college.app.data.DataStoreRepository
import com.college.app.models.network.responses.GoogleUserModel
import com.college.base.AppCoroutineDispatcher
import com.college.base.SingleLiveEvent
import com.college.base.logger.CollegeLogger
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnBoardingLoginViewModel @Inject constructor(
    private val appCoroutineDispatcher: AppCoroutineDispatcher,
    private val dataStoreRepository: DataStoreRepository,
    private val logger: CollegeLogger
) : ViewModel() {

    data class LoginViewState(
        var isLoading: Boolean = false,
        val userState: GoogleUserModel
    )

    val loginViewState: MutableLiveData<LoginViewState> = MutableLiveData()

    fun currentLoginViewState(): LoginViewState = loginViewState.value!!

    val command: SingleLiveEvent<Command> = SingleLiveEvent()

    sealed class Command {
        object StartGoogleLogin : Command()
    }

    fun userClickedForLogin() {
        command.value = Command.StartGoogleLogin
    }

    fun loginSuccess(user: GoogleSignInAccount) {
        viewModelScope.launch(appCoroutineDispatcher.io) {

            logger.d("user found : ${user.email} with user id ${user.id}")

            user.id?.let {
//                logger.setUserId(it)
                dataStoreRepository.setUserId(it.toLong())
            }
        }
    }

    fun loginFail(message: String) {
        logger.e("login Fail $message")
        command.value = null
    }

}