package com.college.app.ui.courses

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.college.app.data.repositories.courses.CoursesRepository
import com.college.app.models.local.CollegeCourse
import com.college.app.ui.courses.adapter.CourseListAdapter
import com.college.app.utils.extensions.toLiveData
import com.college.base.AppCoroutineDispatcher
import com.college.base.SingleLiveEvent
import com.college.base.logger.CollegeLogger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CoursesViewModel @Inject constructor(
    private val coursesRepository: CoursesRepository,
    private val appCoroutineDispatcher: AppCoroutineDispatcher,
    private val logger: CollegeLogger,
) : ViewModel() {

    sealed class CoursesFragmentCommand {
        class ShowToast(val message: String) : CoursesFragmentCommand()
        class OpenCourseDetailFragment(val courseViewState: CourseListAdapter.CourseViewState) :
            CoursesFragmentCommand()
    }

    val coursesFragmentCommand: SingleLiveEvent<CoursesFragmentCommand> = SingleLiveEvent()

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

    init {
        viewModelScope.launch(appCoroutineDispatcher.io) {
            coursesRepository.updateLocalCourses()
        }
    }

    fun actionCourseEnrollmentChange(enroll: Boolean, course: CourseListAdapter.CourseViewState) {
        viewModelScope.launch(appCoroutineDispatcher.io) {
            coursesRepository.enrollACourse(course.code, enroll)
        }
    }

    fun actionCourseItemClicked(courseViewState: CourseListAdapter.CourseViewState) {
        coursesFragmentCommand.postValue(CoursesFragmentCommand.OpenCourseDetailFragment(
            courseViewState))
    }

}