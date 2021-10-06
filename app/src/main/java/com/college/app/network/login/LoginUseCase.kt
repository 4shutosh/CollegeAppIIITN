package com.college.app.network.login

import com.college.app.models.network.requests.LoginRequest
import com.college.base.AppCoroutineDispatcher
import com.college.base.domain.SuspendUseCase
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    appCoroutineDispatcher: AppCoroutineDispatcher,
) : SuspendUseCase<LoginRequest, Unit>(appCoroutineDispatcher.io){
    override suspend fun execute(parameters: LoginRequest) {
        TODO("Not yet implemented")
    }

}