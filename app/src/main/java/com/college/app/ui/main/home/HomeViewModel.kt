package com.college.app.ui.main.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.college.app.R
import com.college.app.ui.books.OfflineLibraryFragment.Companion.OFFLINE_LIBRARY_FRAGMENT_ID
import com.college.app.ui.main.home.HomeFragment.Companion.HOME_WEBSITE_FRAGMENT_ID
import com.college.app.ui.main.home.HomeFragment.Companion.HOME_WEBSITE_URL
import com.college.app.ui.main.home.HomeViewModel.Command.*
import com.college.app.ui.main.home.list.HomeFeatureListViewState
import com.college.app.utils.extensions.toLiveData
import com.college.base.AppCoroutineDispatcher
import com.college.base.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val appCoroutineDispatcher: AppCoroutineDispatcher

) : ViewModel() {

    private val _homeFeatureList = MutableLiveData(homeFeatureList)
    val homeFeatureListLiveData = _homeFeatureList.toLiveData()

    val command: SingleLiveEvent<Command> = SingleLiveEvent()

    sealed class Command {
        class NavigateToFeatureScreen(val fragmentId: Long) : Command()
        class OpenWebViewWithUrl(val url: String) : Command()
    }

    fun actionFeatureItemClick(fragmentId: Long) {
        viewModelScope.launch(appCoroutineDispatcher.main) {
            command.postValue(
                when (fragmentId) {
                    HOME_WEBSITE_FRAGMENT_ID -> {
                        OpenWebViewWithUrl(HOME_WEBSITE_URL)
                    }
                    else -> NavigateToFeatureScreen(fragmentId)
                }
            )
        }
    }


    companion object {
        private val homeFeatureList = mutableListOf(
            HomeFeatureListViewState(
                id = HOME_WEBSITE_FRAGMENT_ID,
                iconRes = R.drawable.ic_website_round,
                titleRes = R.string.collegeWebsite,
                itemBackgroundColorRes = R.color.icon_circle_8,
                textColorRes = R.color.icon_inside_8
            ),
            HomeFeatureListViewState(
                id = 1L,
                iconRes = R.drawable.ic_courses_round,
                titleRes = R.string.courses,
                itemBackgroundColorRes = R.color.icon_circle_0,
                textColorRes = R.color.icon_inside_0
            ),
            HomeFeatureListViewState(
                id = 3,
                iconRes = R.drawable.ic_events_round,
                titleRes = R.string.events,
                itemBackgroundColorRes = R.color.icon_circle_9,
                textColorRes = R.color.icon_inside_9
            ),
            HomeFeatureListViewState(
                id = 4,
                iconRes = R.drawable.ic_clubs_round,
                titleRes = R.string.clubs,
                itemBackgroundColorRes = R.color.icon_circle_5,
                textColorRes = R.color.icon_inside_5
            ),
            HomeFeatureListViewState(
                id = OFFLINE_LIBRARY_FRAGMENT_ID,
                iconRes = R.drawable.ic_library_round,
                titleRes = R.string.library,
                itemBackgroundColorRes = R.color.icon_circle_12,
                textColorRes = R.color.icon_inside_12
            ),
            HomeFeatureListViewState(
                id = 5,
                iconRes = R.drawable.ic_assistant_round,
                titleRes = R.string.services,
                itemBackgroundColorRes = R.color.icon_circle_3,
                textColorRes = R.color.icon_inside_3
            ),
            HomeFeatureListViewState(
                id = 5,
                iconRes = R.drawable.ic_attendance_round,
                titleRes = R.string.checkYourAttendance,
                itemBackgroundColorRes = R.color.icon_circle_4,
                textColorRes = R.color.icon_inside_4
            )
        )
    }


}