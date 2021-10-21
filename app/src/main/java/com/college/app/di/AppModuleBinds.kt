package com.college.app.di

import com.college.app.data.DataStoreRepository
import com.college.app.data.DataStoreRepositoryImpl
import com.college.app.network.CollegeAppService
import com.college.app.network.CollegeFirebaseService
import com.college.app.network.CollegeFirebaseServiceImpl
import com.college.app.network.CollegeNetworking
import com.college.base.logger.CollegeLogger
import com.college.base.logger.CrashlyticsTree
import com.college.base.logger.Logger
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import timber.log.Timber
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModuleBinds {

    @Singleton
    @Binds
    abstract fun bindsLogger(bind: CollegeLogger): Logger

    @Singleton
    @Binds
    abstract fun bindsCrashlyticsTree(bind: CrashlyticsTree): Timber.Tree

    @Singleton
    @Binds
    abstract fun bindsDataStoreRepository(dataStoreRepository: DataStoreRepositoryImpl): DataStoreRepository

    @Singleton
    @Binds
    abstract fun bindsCollegeFirebaseService(collegeFirebaseService: CollegeFirebaseServiceImpl): CollegeFirebaseService

}