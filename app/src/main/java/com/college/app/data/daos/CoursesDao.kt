package com.college.app.data.daos

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Update
import com.college.app.models.local.CollegeCourse
import kotlinx.coroutines.flow.Flow

@Dao
abstract class CoursesDao : EntityDao<CollegeCourse>() {

    @Query("SELECT * FROM courses")
    abstract suspend fun getAllCourses(): List<CollegeCourse>

    @Query("SELECT * FROM courses WHERE userEnrolled is :enrolled")
    abstract fun getAllEnrolledCourses(enrolled: Boolean = true): Flow<List<CollegeCourse>>

    @Query("UPDATE courses SET userEnrolled = :enroll WHERE id = :id")
    abstract suspend fun enrollCourseById(id: Long, enroll: Boolean)

    @Query("DELETE FROM courses")
    abstract suspend fun deleteAll()

}