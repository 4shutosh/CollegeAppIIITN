package com.college.app.network

import com.college.app.data.repositories.DataStoreRepository
import com.college.app.utils.Constants.Injection.BUILD_VERSION_CODE
import com.college.app.utils.Constants.Injection.BUILD_VERSION_NAME
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Named

class CollegeInterceptor @Inject constructor(
    private val dataStoreRepository: DataStoreRepository,
    @Named(BUILD_VERSION_CODE) private val buildVersionCode: Int,
    @Named(BUILD_VERSION_NAME) private val buildVersionName: String
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val accessToken = runBlocking { dataStoreRepository.getAccessToken() }
        val newChain = chain.request().newBuilder()

        if (accessToken.isNullOrBlank().not()) {
            newChain.addHeader("Authorization", "Token $accessToken")
        }

        newChain.addHeader("appVersion", buildVersionCode.toString())
            .addHeader("appVersionName", buildVersionName)
            .addHeader("source", "android")

        return chain.proceed(newChain.build())
    }
}
