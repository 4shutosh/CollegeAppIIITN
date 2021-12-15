package com.college.app.ui.todo

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.college.app.data.entities.TodoItem
import com.college.app.utils.toLiveData
import com.college.base.AppCoroutineDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodoViewModel @Inject constructor(
    private val appCoroutineDispatcher: AppCoroutineDispatcher
) : ViewModel() {


    private val _todoList = MutableLiveData<List<TodoItem>>()
    val todoList = _todoList.toLiveData()


    init {
        populateTodo()
    }

    private fun populateTodo() {
        viewModelScope.launch(appCoroutineDispatcher.io) {
            val list = mutableListOf<TodoItem>()
            for (i in 0..10) {
                list.add(
                    TodoItem(
                        id = i.toLong(),
                        name = "ABC",
                        timeStamp = 1234
                    )
                )
            }
            _todoList.postValue(list)
        }
    }


}