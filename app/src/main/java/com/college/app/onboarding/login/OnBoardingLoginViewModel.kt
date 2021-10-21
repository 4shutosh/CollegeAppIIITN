package com.college.app.onboarding.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.college.app.data.DataStoreRepository
import com.college.app.models.local.CollegeUser
import com.college.app.models.network.requests.LoginRequest
import com.college.app.network.login.LoginUseCase
import com.college.base.AppCoroutineDispatcher
import com.college.base.SingleLiveEvent
import com.college.base.logger.CollegeLogger
import com.college.base.result.onSuccess
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import dagger.hilt.android.lifecycle.HiltViewModel
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

    val loginViewState: MutableLiveData<LoginViewState> = MutableLiveData()

    private fun currentLoginViewState(): LoginViewState = loginViewState.value!!

    val command: SingleLiveEvent<Command> = SingleLiveEvent()

    sealed class Command {
        object StartGoogleLogin : Command()
    }

    fun userClickedForLogin() {
        command.value = Command.StartGoogleLogin
    }

    fun loginSuccess(user: GoogleSignInAccount) {
        viewModelScope.launch(appCoroutineDispatcher.io) {

//            loginViewState.value = currentLoginViewState().copy(isLoading = true)

            logger.d("user found : ${user.email} with user id ${user.id}")

//            user.id?.let {
//                logger.setUserId(it)
//                dataStoreRepository.setUserId(it.toLong())
//                loginViewState.value = currentLoginViewState().copy(
//                    isLoading = false,
//                    userState = CollegeUser(
//                        id = it.toLong(),
//                        email = user.email.orEmpty(),
//                        name = user.displayName.orEmpty(),
//                    )
//                )
//            }

            user.idToken?.let {
                loginUseCase(
                    LoginRequest(
                        name = user.displayName.orEmpty(),
                        email = user.email.orEmpty(),
                        imageUrl = user.photoUrl?.toString()
                    )
                ).onSuccess {
                    logger.d(this.toString())
                }
            }
        }
    }

    fun loginFail(message: String) {
        logger.e("login Fail $message")
        command.value = null
    }

}