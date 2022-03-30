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

    fun getAllCourses(): Flow<List<CollegeCourse>>

    fun getAllUserCourses(): Flow<List<CollegeCourse>>

    suspend fun enrollACourse(id: Long, enroll: Boolean = true)

}


class CoursesRepositoryImpl @Inject constructor(
    private val collegeAppService: CollegeAppService,
    private val coursesDao: CoursesDao,
    private val logger: CollegeLogger,
) : CoursesRepository {

    override fun getAllCourses(): Flow<List<CollegeCourse>> = flow {
        emit(coursesDao.getAllCourses())

        try {
            val networkCoursesResponse = collegeAppService.getAllCourses()
            if (networkCoursesResponse.isSuccess()) {
                coursesDao.deleteAll()
                coursesDao.insertAll(networkCoursesResponse.data)
                emit(coursesDao.getAllCourses())
            } else {
                logger.d("network response failed: ${networkCoursesResponse.serverException()}")
            }
        } catch (e: Exception) {
            logger.d("something wrong: ${e.message}")
        }
    }

    override fun getAllUserCourses(): Flow<List<CollegeCourse>> {
        return coursesDao.getAllEnrolledCourses()
    }

    override suspend fun enrollACourse(id: Long, enroll: Boolean) {
        return coursesDao.enrollCourseById(id, enroll)
    }
}