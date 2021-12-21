package com.college.app.ui.todo

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.college.app.data.repositories.todo.TodoRepository
import com.college.app.utils.toLiveData
import com.college.base.AppCoroutineDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class TodoViewModel @Inject constructor(
    private val appCoroutineDispatcher: AppCoroutineDispatcher,
    private val todoRepository: TodoRepository,
) : ViewModel() {


    private val _todoList = MutableLiveData<List<TodoListViewState>>()
    val todoList = _todoList.toLiveData()


    init {
        populateTodo()
    }

    private fun populateTodo() {
        viewModelScope.launch(appCoroutineDispatcher.io) {
            val list = mutableListOf<TodoListViewState>()
            for (i in 0..10) {
                list.add(
                    TodoListViewState(
                        id = i.toLong(),
                        title = "DSA Assignment $i",
                        description = "This is a demo description for $i",
                        timeLeft = "44!!",
                        timeLeftUnit = "minutes left",
                        time = "10:00 am",
                        date = "22 January, 2022",
                        isNotifyOn = false
                    )
                )
            }
            _todoList.postValue(list)
        }
    }

    private fun addNewTodo() {
        viewModelScope.launch(appCoroutineDispatcher.main) {
            withContext(appCoroutineDispatcher.io) {
                val list = mutableListOf<TodoListViewState>()
                list.add(
                    TodoListViewState(
                        id = Math.random().toLong(),
                        title = "DSA Assignment #",
                        description = "This is a demo description for #",
                        timeLeft = "44!!",
                        timeLeftUnit = "minutes left",
                        time = "10:00 am",
                        date = "22 January, 2022",
                        isNotifyOn = false
                    )
                )
                _todoList.postValue(list)
            }
        }
    }
}