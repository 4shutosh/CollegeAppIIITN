package com.college.app.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.college.app.data.repositories.DataStoreRepository
import com.college.app.utils.extensions.toLiveData
import com.college.base.AppCoroutineDispatcher
import com.college.base.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileSettingsViewModel @Inject constructor(
    private val appCoroutineDispatcher: AppCoroutineDispatcher,
    private val dataStoreRepository: DataStoreRepository,
) : ViewModel() {

    sealed class Command {
        class LoadInitialData(val userImage: String, val userName: String) :
            Command()

        object LogOut : Command()
        object Exit : Command()
    }

    private val _command = SingleLiveEvent<Command>()
    val command = _command.toLiveData()

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        viewModelScope.launch(appCoroutineDispatcher.io) {
            val userImage = dataStoreRepository.getUserImageUrl().orEmpty()
            val userName = dataStoreRepository.getUserName()
            val userEmail = dataStoreRepository.getUserEmail()

            _command.postValue(Command.LoadInitialData(
                userImage = userImage,
                userName = "$userName\n$userEmail"
            ))
        }
    }

    fun actionLogOut() {
        viewModelScope.launch(appCoroutineDispatcher.main) {
            dataStoreRepository.logOut()
            _command.postValue(Command.LogOut)
        }
    }

    fun actionExit() {
        viewModelScope.launch(appCoroutineDispatcher.main) {
            _command.postValue(Command.Exit)
        }
    }

}