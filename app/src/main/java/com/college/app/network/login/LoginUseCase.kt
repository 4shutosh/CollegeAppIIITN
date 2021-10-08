package com.college.app.network.login

import com.college.app.data.LoginRepository
import com.college.app.models.network.requests.LoginRequest
import com.college.base.AppCoroutineDispatcher
import com.college.base.domain.SuspendUseCase
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    appCoroutineDispatcher: AppCoroutineDispatcher,
    private val loginRepository: LoginRepository,
) : SuspendUseCase<LoginRequest, Boolean>(appCoroutineDispatcher.io) {
    override suspend fun execute(parameters: LoginRequest) : Boolean {
        return loginRepository.checkForUser(parameters.googleUserIdToken)
    }

}