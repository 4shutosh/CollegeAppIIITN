package com.college.app.ui.courses

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.college.app.data.repositories.courses.CoursesRepository
import com.college.app.models.local.CollegeCourse
import com.college.app.ui.courses.adapter.CourseListAdapter
import com.college.base.AppCoroutineDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class CoursesViewModel @Inject constructor(
    private val coursesRepository: CoursesRepository,
    private val appCoroutineDispatcher: AppCoroutineDispatcher,
) : ViewModel() {

    val userCoursesFlow = coursesRepository.getAllUserCourses().map { list ->
        list.map {
            CourseListAdapter.CourseViewState(
                id = it.id,
                courseId = it.courseId,
                name = it.name,
                facultyName = it.facultyName,
                description = it.description,
                code = it.code,
                userEnrolled = it.userEnrolled
            )
        }
    }.asLiveData(appCoroutineDispatcher.io)

    val allCoursesFlow = coursesRepository.getAllCourses().map { list ->
        list.map {
            CourseListAdapter.CourseViewState(
                id = it.id,
                courseId = it.courseId,
                name = it.name,
                facultyName = it.facultyName,
                description = it.description,
                code = it.code,
                userEnrolled = it.userEnrolled
            )
        }
    }.asLiveData(appCoroutineDispatcher.io)


}