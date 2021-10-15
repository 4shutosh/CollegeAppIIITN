package com.college.app.network

import com.college.app.utils.Constants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
open class CollegeNetworking @Inject constructor(
    @Named(Constants.Injection.IS_DEBUG) val isDebugMode: Boolean,
    @Named(Constants.Injection.COLLEGE_ENDPOINT) private val collegeEndPoint: String,
    private val collegeInterceptor: CollegeInterceptor
) {
    private var retrofit: Retrofit? = null
    private var okHttpClient: OkHttpClient? = null

    private fun retrofit(): Retrofit {
        if (retrofit == null) retrofit = retrofitBuilder().build()
        return requireNotNull(retrofit)
    }

    @Synchronized
    open fun okHttpClient(): OkHttpClient {
        if (okHttpClient == null) {
            val builder: OkHttpClient.Builder = OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().apply {
                    setLevel(
                        if (isDebugMode) HttpLoggingInterceptor.Level.BODY
                        else HttpLoggingInterceptor.Level.NONE
                    )
                }).addInterceptor(collegeInterceptor)

            okHttpClient = builder.build()
        }
        return requireNotNull(okHttpClient)
    }

    private fun retrofitBuilder(): Retrofit.Builder {
        return Retrofit.Builder()
            .baseUrl(collegeEndPoint)
            .addConverterFactory(MoshiConverterFactory.create())
            .client(okHttpClient())
    }

    fun collegeAppService(): CollegeAppService = retrofit().create(CollegeAppService::class.java)
}