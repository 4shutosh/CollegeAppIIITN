package com.college.app.ui.notification

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.college.app.ui.notification.list.NotificationsAdapter
import com.college.app.ui.notification.list.NotificationsAdapter.NotificationsListViewState
import com.college.app.utils.extensions.toLiveData
import com.college.base.AppCoroutineDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationsViewModel @Inject constructor(
    private val appCoroutineDispatcher: AppCoroutineDispatcher,
) : ViewModel() {

    init {
        populateNotificationsList()
    }


    private val _notificationsList =
        MutableLiveData<MutableList<NotificationsListViewState>>(mutableListOf())
    val notificationsList = _notificationsList.toLiveData()


    private fun populateNotificationsList() {
        viewModelScope.launch(appCoroutineDispatcher.io) {
            val list = mutableListOf<NotificationsListViewState>()
            for (i in 0..10) {
                list.add(
                    NotificationsListViewState(
                        title = "This is a demo title $i",
                        message = "This is a sample message for testing the notifications list with the i = $i"
                    )
                )
            }
            _notificationsList.postValue(list)
        }
    }
}