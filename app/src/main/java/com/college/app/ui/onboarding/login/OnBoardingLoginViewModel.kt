package com.college.app.ui.onboarding.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.college.app.data.DataStoreRepository
import com.college.app.models.local.CollegeUser
import com.college.app.models.network.requests.LoginRequest
import com.college.app.network.login.LoginUseCase
import com.college.base.AppCoroutineDispatcher
import com.college.base.SingleLiveEvent
import com.college.base.logger.CollegeLogger
import com.college.base.result.onError
import com.college.base.result.onSuccess
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnBoardingLoginViewModel @Inject constructor(
    private val appCoroutineDispatcher: AppCoroutineDispatcher,
    private val dataStoreRepository: DataStoreRepository,
    private val loginUseCase: LoginUseCase,
    private val logger: CollegeLogger
) : ViewModel() {

    data class LoginViewState(
        var isLoading: Boolean = false,
        val userState: CollegeUser? = null
    )

    private val _loginViewState = MutableStateFlow(LoginViewState())
    val loginViewState: StateFlow<LoginViewState> = _loginViewState.asStateFlow()

    val command: SingleLiveEvent<Command> = SingleLiveEvent()

    sealed class Command {
        object StartGoogleLogin : Command()
        object NavigateToMainGraph : Command()
    }

    fun userClickedForLogin() {
        command.value = Command.StartGoogleLogin
    }

    fun loginSuccess(user: GoogleSignInAccount) {
        viewModelScope.launch(appCoroutineDispatcher.io) {

            _loginViewState.update { it.copy(isLoading = true) }

            logger.d("user found : ${user.email} with user id ${user.id}")

            user.idToken?.let {
                loginUseCase(
                    LoginRequest(
                        name = user.displayName.orEmpty(),
                        email = user.email.orEmpty(),
                        imageUrl = user.photoUrl?.toString()
                    )
                ).onSuccess {
                    logger.d(this.toString())

                    dataStoreRepository.setUserId(this.userId)
                    dataStoreRepository.setAccessToken(this.accessToken)

                    moveToMainGraph()
                }.onError {
                    loginFail(this.toString())
                    _loginViewState.update { it.copy(isLoading = false) }
                }
            }
        }
    }

    private fun moveToMainGraph() {
        viewModelScope.launch(appCoroutineDispatcher.main) {
            command.value = Command.NavigateToMainGraph
        }
    }

    fun loginFail(message: String) {
        logger.e("login Fail $message")
        // todo show toast message here
        command.value = null
    }

}