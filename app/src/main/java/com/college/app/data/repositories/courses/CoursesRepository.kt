package com.college.app.data.repositories.courses

import com.college.app.data.daos.CoursesDao
import com.college.app.models.local.CollegeCourse
import com.college.app.network.CollegeAppService
import com.college.base.domain.isSuccess
import com.college.base.domain.serverException
import com.college.base.logger.CollegeLogger
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.lang.Exception
import javax.inject.Inject

interface CoursesRepository {

    suspend fun updateLocalCourses()

    fun getAllCourses(): Flow<List<CollegeCourse>>
    fun getAllUserCourses(): Flow<List<CollegeCourse>>

    fun enrollACourse(courseCode: String, enroll: Boolean = true)

    suspend fun getCourseByCode(courseCode: String): CollegeCourse?

}


class CoursesRepositoryImpl @Inject constructor(
    private val collegeAppService: CollegeAppService,
    private val coursesDao: CoursesDao,
    private val logger: CollegeLogger,
) : CoursesRepository {

    override suspend fun updateLocalCourses() {
        try {
            val networkCoursesResponse = collegeAppService.getAllCourses()
            if (networkCoursesResponse.isSuccess()) {
                networkCoursesResponse.data.forEach {
                    coursesDao.insertOrUpdateCourse(it)
                }
            } else {
                logger.d("network response failed: ${networkCoursesResponse.serverException()}")
            }
        } catch (e: Exception) {
            logger.d("something wrong: ${e.message}")
        }
    }

    override fun getAllCourses(): Flow<List<CollegeCourse>> {
        return coursesDao.getAllCourses()
    }

    override fun getAllUserCourses(): Flow<List<CollegeCourse>> {
        return coursesDao.getAllEnrolledCourses()
    }

    override fun enrollACourse(courseCode: String, enroll: Boolean) {
        return coursesDao.enrollCourseById(courseCode, enroll)
    }

    override suspend fun getCourseByCode(courseCode: String): CollegeCourse? {
        return coursesDao.getCourseByCode(courseCode)
    }
}