package com.college.app.di

import com.college.app.data.repositories.courses.CoursesRepository
import com.college.app.data.repositories.courses.CoursesRepositoryImpl
import com.college.app.data.repositories.library.LibraryBooksRepository
import com.college.app.data.repositories.library.LibraryBooksRepositoryImpl
import com.college.app.data.repositories.login.LoginRepository
import com.college.app.data.repositories.login.LoginRepositoryImpl
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
    fun providesLoginRepository(loginRepository: LoginRepositoryImpl): LoginRepository =
        loginRepository

    @Provides
    @ViewModelScoped
    fun providesLibraryRepository(libraryBooksRepository: LibraryBooksRepositoryImpl): LibraryBooksRepository =
        libraryBooksRepository

    @Provides
    @ViewModelScoped
    fun providesCourseRepository(coursesRepository: CoursesRepositoryImpl): CoursesRepository =
        coursesRepository
}