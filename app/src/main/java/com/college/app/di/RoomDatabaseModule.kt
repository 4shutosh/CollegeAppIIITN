package com.college.app.di

import android.content.Context
import android.os.Debug
import androidx.room.Room
import com.college.app.data.database.CollegeRoomDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class RoomDatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(
        @ApplicationContext context: Context
    ): CollegeRoomDatabase {
        val builder = Room.databaseBuilder(context, CollegeRoomDatabase::class.java, DATABASE_NAME)
            .fallbackToDestructiveMigration()
        if (Debug.isDebuggerConnected()){
            builder.allowMainThreadQueries()
        }
        return builder.build()
    }

    companion object {
        const val DATABASE_NAME = "college.db"
    }
}

@InstallIn(SingletonComponent::class)
@Module
object DatabaseDaoModule {

    @Provides
    fun provideTodoDatabase(db: CollegeRoomDatabase) = db.todoDao()
}