package com.college.app.data.daos

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.college.app.models.local.CollegeCourse
import kotlinx.coroutines.flow.Flow

@Dao
abstract class CoursesDao : EntityDao<CollegeCourse>() {

    @Query("SELECT * FROM courses")
    abstract fun getAllCourses(): Flow<List<CollegeCourse>>

    @Query("SELECT * FROM courses WHERE userEnrolled is :enrolled")
    abstract fun getAllEnrolledCourses(enrolled: Boolean = true): Flow<List<CollegeCourse>>

    @Query("UPDATE courses SET userEnrolled = :enroll WHERE code = :courseCode")
    abstract fun enrollCourseById(courseCode: String, enroll: Boolean)

    @Query("SELECT * FROM courses WHERE code = :courseCode")
    abstract fun getCourseByCode(courseCode: String): CollegeCourse?

    suspend fun insertOrUpdateCourse(course: CollegeCourse) {
        val courseFromDb = getCourseByCode(course.code)
        if (courseFromDb == null) {
            insert(course)
        } else {
            update(courseFromDb.copy(
                name = course.name,
                facultyName = course.facultyName,
                description = course.description)
            )
        }
    }

    @Query("DELETE FROM courses")
    abstract suspend fun deleteAll()

}