package com.college.app.ui.main.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.college.app.R
import com.college.app.ui.main.home.list.HomeFeatureListViewState
import com.college.app.utils.extensions.toLiveData
import com.college.base.AppCoroutineDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val appCoroutineDispatcher: AppCoroutineDispatcher

) : ViewModel() {

    private val _homeFeatureList = MutableLiveData(homeFeatureList)
    val homeFeatureListLiveData = _homeFeatureList.toLiveData()


    init {
        viewModelScope.launch(appCoroutineDispatcher.io) {
            _homeFeatureList.postValue(homeFeatureList)
        }
    }


    companion object {
        private val homeFeatureList = mutableListOf(
            HomeFeatureListViewState(
                id = 0,
                iconRes = R.drawable.ic_website_round,
                titleRes = R.string.collegeWebsite,
                itemBackgroundColorRes = R.color.icon_circle_8,
                textColorRes = R.color.icon_inside_8
            ),
            HomeFeatureListViewState(
                id = 1,
                iconRes = R.drawable.ic_school_round,
                titleRes = R.string.courses,
                itemBackgroundColorRes = R.color.icon_circle_0,
                textColorRes = R.color.icon_inside_0
            ),
            HomeFeatureListViewState(
                id = 2,
                iconRes = R.drawable.ic_services_round,
                titleRes = R.string.services,
                itemBackgroundColorRes = R.color.icon_circle_4,
                textColorRes = R.color.icon_inside_4
            ),
            HomeFeatureListViewState(
                id = 3,
                iconRes = R.drawable.ic_events_round,
                titleRes = R.string.events,
                itemBackgroundColorRes = R.color.icon_circle_9,
                textColorRes = R.color.icon_inside_9
            )
        )
    }


}