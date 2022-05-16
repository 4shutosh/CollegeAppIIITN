package com.college.app.ui.notification

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.college.app.data.repositories.announcements.AnnouncementRepository
import com.college.app.ui.notification.list.NotificationsAdapter.NotificationsListViewState
import com.college.app.utils.extensions.toLiveData
import com.college.base.AppCoroutineDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationsViewModel @Inject constructor(
    private val appCoroutineDispatcher: AppCoroutineDispatcher,
    private val announcementsRepository: AnnouncementRepository,
) : ViewModel() {

    init {
        viewModelScope.launch(appCoroutineDispatcher.io) {
            announcementsRepository.updateLocalAnnouncements()
        }
    }

    fun swipeActionFetchAgainFromNetwork(){
        viewModelScope.launch(appCoroutineDispatcher.io) {
            announcementsRepository.updateLocalAnnouncements()
        }
    }

    private val _notificationsList =
        MutableLiveData<MutableList<NotificationsListViewState>>(mutableListOf())
    val notificationsList = _notificationsList.toLiveData()

    val announcementsFlow = announcementsRepository.getAllAnnouncements().map { list ->
        list.map {
            NotificationsListViewState(
                title = it.title,
                message = it.message,
                url = it.link
            )
        }
    }.asLiveData(appCoroutineDispatcher.io)

    private fun addPseudoData() {
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