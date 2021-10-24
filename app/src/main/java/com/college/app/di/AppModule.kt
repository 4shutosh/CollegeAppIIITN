package com.college.app.di

import android.content.Context
import com.college.app.BuildConfig
import com.college.app.R
import com.college.app.network.CollegeAppService
import com.college.app.network.CollegeNetworking
import com.college.app.utils.Constants.Injection.BUILD_VERSION_CODE
import com.college.app.utils.Constants.Injection.BUILD_VERSION_NAME
import com.college.app.utils.Constants.Injection.COLLEGE_ENDPOINT
import com.college.app.utils.Constants.Injection.IS_DEBUG
import com.college.base.AppCoroutineDispatcher
import com.google.firebase.crashlytics.FirebaseCrashlytics
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideFirebaseCrashlytics(): FirebaseCrashlytics = FirebaseCrashlytics.getInstance()

    @Singleton
    @Provides
    fun provideCoroutineDispatchers() = AppCoroutineDispatcher()

    @Provides
    @Named(IS_DEBUG)
    fun providesIsDebug(): Boolean = BuildConfig.DEBUG

    @Provides
    @Named(BUILD_VERSION_CODE)
    fun providesBuildVersionCode() = BuildConfig.VERSION_CODE

    @Provides
    @Named(BUILD_VERSION_NAME)
    fun providesBuildVersionName() = BuildConfig.VERSION_NAME

    @Provides
    @Named(COLLEGE_ENDPOINT)
    fun providesCollegeEndpoint(@ApplicationContext context: Context): String =
        context.getString(R.string.college_endpoint)

    @Provides
    @Singleton
    fun provideCollegeAppService(collegeNetworking: CollegeNetworking): CollegeAppService =
        collegeNetworking.collegeAppService()
}