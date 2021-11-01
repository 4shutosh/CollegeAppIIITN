package com.college.app.di

import com.college.app.data.repositories.LoginRepository
import com.college.app.data.repositories.LoginRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
class ViewModelModule {

    @Provides
    @ViewModelScoped
    fun providesLoginRepository(loginRepository: LoginRepositoryImpl): LoginRepository = loginRepository
}