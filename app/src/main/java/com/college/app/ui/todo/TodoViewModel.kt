package com.college.app.ui.todo

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.college.app.data.entities.TodoItem
import com.college.app.data.repositories.todo.TodoRepository
import com.college.app.utils.extensions.toLiveData
import com.college.base.AppCoroutineDispatcher
import com.college.base.logger.CollegeLogger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

// add make this testable use MVI and send triggers from UI to the view state then back to the UI

@HiltViewModel
class TodoViewModel @Inject constructor(
    private val appCoroutineDispatcher: AppCoroutineDispatcher,
    private val todoRepository: TodoRepository,
    private val logger: CollegeLogger
) : ViewModel() {


    private val _todoList = MutableLiveData<List<TodoListViewState>>()
    val todoList = _todoList.toLiveData()

    init {
        viewModelScope.launch(appCoroutineDispatcher.io) {
            getAllTodo()
        }
    }

    private suspend fun getAllTodo() {
        todoRepository.getAllTodoFlow()
            .map { todoDbList ->
                todoDbList.toListOfViewState()
            }.collect {
                logger.d(it.toString())
                _todoList.postValue(it)
            }
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

    fun actionTodoDelete(itemTodo: TodoListViewState, position: Int) {
        viewModelScope.launch(appCoroutineDispatcher.io) {
            todoRepository.deleteTodo(itemTodo.id)
        }
    }

    fun addNewTodo() {
        viewModelScope.launch(appCoroutineDispatcher.io) {
            val temp = todoRepository.insertTodo(
                TodoItem(
                    name = "Temp TODO",
                    description = "Demo Description",
                    timeStampMilliSeconds = 1640449510000
                )
            )
        }
    }
}