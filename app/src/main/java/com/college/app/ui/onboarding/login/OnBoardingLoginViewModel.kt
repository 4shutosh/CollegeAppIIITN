package com.college.app.ui.onboarding.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.college.app.data.repositories.DataStoreRepository
import com.college.app.models.local.CollegeUser
import com.college.app.models.network.requests.LoginRequest
import com.college.app.network.login.GoogleLogOutHelper
import com.college.app.network.login.LoginUseCase
import com.college.app.utils.CollegeBuildVariantType
import com.college.app.utils.isCommunityType
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
    private val logger: CollegeLogger,
    private val googleLogOutHelper: GoogleLogOutHelper,
    private val collegeBuildVariantType: CollegeBuildVariantType
) : ViewModel() {

    data class LoginViewState(
        var isLoading: Boolean = false,
        var communityEdition: Boolean = false,
        val userState: CollegeUser? = null
    )

    // view state update should be always on the main thread and not in any other scope
    private val _loginViewState = MutableStateFlow(LoginViewState())
    val loginViewState: StateFlow<LoginViewState> = _loginViewState.asStateFlow()

    val toast: SingleLiveEvent<String> = SingleLiveEvent()

    val command: SingleLiveEvent<Command> = SingleLiveEvent()

    init {
        viewModelScope.launch(appCoroutineDispatcher.main) {
            if (collegeBuildVariantType.isCommunityType()) {
                _loginViewState.update { it.copy(communityEdition = true) }
            }
        }
    }

    sealed class Command {
        object StartGoogleLogin : Command()
        object NavigateToMainGraph : Command()
    }

    fun userClickedForLogin() {
        command.value = Command.StartGoogleLogin
    }

    private fun loginSuccess(user: GoogleSignInAccount) {
        viewModelScope.launch(appCoroutineDispatcher.io) {
            logger.d("user found : ${user.email} with user id ${user.id}")

            _loginViewState.update { it.copy(isLoading = true) }

            val userEmail = user.email

            if (!user.idToken.isNullOrEmpty() && !userEmail.isNullOrEmpty()) {
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

                    navigateToMainScreen()
                }.onError {
                    loginFail(this.toString())
                    viewModelScope.launch(appCoroutineDispatcher.main) {
                        _loginViewState.update { it.copy(isLoading = false) }
                    }
                }
            }
        }
    }

    fun processGoogleUser(googleUser: GoogleSignInAccount) {
        if (!loginViewState.value.communityEdition) {
            if (validateGoogleEmail(googleUser.email.orEmpty())) {
                loginSuccess(googleUser)
            } else {
                wrongEmailLogout()
            }
        } else {
            loginSuccess(googleUser)
        }
    }

    private fun validateGoogleEmail(emailAddress: String): Boolean {
        return if (emailAddress.isEmpty()) {
            false
        } else {
            val domain = emailAddress.substringAfter('@')
            domain == requiredEmailDomain
        }
    }

    private fun wrongEmailLogout() {
        googleLogOutHelper.signOutAndClearPreferences()
        toast.value = "Wrong Email Used, Please Use College Email"
        command.value = null
    }

    private fun navigateToMainScreen() {
        viewModelScope.launch(appCoroutineDispatcher.main) {
            command.value = Command.NavigateToMainGraph
            toast.value = welcomeMessage
        }
    }

    fun loginFail(message: String) {
        logger.e("login Fail $message")
        toast.value = message
        command.value = null
    }

    companion object {
        const val requiredEmailDomain = "iiitn.ac.in"
        const val welcomeMessage = "Welcome!"
    }

}