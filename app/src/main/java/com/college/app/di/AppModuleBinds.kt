package com.college.app.di

import com.curieo.base.utils.logger.CollegeLogger
import com.curieo.base.utils.logger.CrashlyticsTree
import com.curieo.base.utils.logger.Logger
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
    abstract fun bindsCrashlyticsTree(bind: CrashlyticsTree) : Timber.Tree
}