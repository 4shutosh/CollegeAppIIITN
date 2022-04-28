package com.college.app.ui.courses.detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.college.app.data.repositories.courses.CoursesRepository
import com.college.app.ui.courses.adapter.CourseListAdapter
import com.college.app.utils.extensions.toLiveData
import com.college.base.AppCoroutineDispatcher
import com.college.base.logger.CollegeLogger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CourseDetailViewModel @Inject constructor(
    private val coursesRepository: CoursesRepository,
    private val appCoroutineDispatcher: AppCoroutineDispatcher,
    private val logger: CollegeLogger
) : ViewModel() {

    private val _courseDetailFragmentViewState: MutableLiveData<CourseListAdapter.CourseViewState> =
        MutableLiveData()
    val courseDetailFragment = _courseDetailFragmentViewState.toLiveData()

    fun actionCourseViewStateFromCode(courseCode: String) {
        viewModelScope.launch(appCoroutineDispatcher.io) {
            val courseFromDb = coursesRepository.getCourseByCode(courseCode)
            if (courseFromDb != null) {
                val viewState = CourseListAdapter.CourseViewState(
                    id = courseFromDb.id,
                    courseId = courseFromDb.courseId,
                    name = courseFromDb.name,
                    facultyName = courseFromDb.facultyName,
                    description = courseFromDb.description,
                    code = courseFromDb.code,
                    userEnrolled = courseFromDb.userEnrolled
                )
                _courseDetailFragmentViewState.postValue(viewState)
            } else {
                logger.d("no course found from the code")
            }
        }
    }

}