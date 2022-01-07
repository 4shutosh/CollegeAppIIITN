package com.college.app.ui.todo

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.college.app.data.entities.TodoItem
import com.college.app.data.repositories.todo.TodoRepository
import com.college.app.utils.extensions.getFormattedDate
import com.college.app.utils.extensions.getFormattedTime
import com.college.app.utils.extensions.toLiveData
import com.college.base.AppCoroutineDispatcher
import com.college.base.SingleLiveEvent
import com.college.base.logger.CollegeLogger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class TodoViewModel @Inject constructor(
    private val appCoroutineDispatcher: AppCoroutineDispatcher,
    private val todoRepository: TodoRepository,
    private val logger: CollegeLogger
) : ViewModel() {


    private val _todoList = MutableLiveData<MutableList<TodoListViewState>>()
    val todoList = _todoList.toLiveData()

    val command: SingleLiveEvent<Command> = SingleLiveEvent()

    sealed class Command {
        object ShowAddTodoDatePicker : Command()
        class ShowAddTodoTimePicker(val dateTimeStamp: Long) : Command()
        class ShowAddTodoDetailsDialog(val dateAndTimeStamp: Long) : Command()
    }

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

    fun actionTodoDelete(itemTodo: TodoListViewState, position: Int) {
        viewModelScope.launch(appCoroutineDispatcher.io) {
            todoRepository.deleteTodo(itemTodo.id)
        }
    }

    fun addNewTodo() {
        viewModelScope.launch(appCoroutineDispatcher.io) {
            todoRepository.insertTodo(
                TodoItem(
                    name = "Temp TODO",
                    description = "Demo Description",
                    timeStampMilliSeconds = 1640449510000
                )
            )

            // todo show snack bar
        }
    }

    fun newTodoDateSelected(dateTimeStamp: Long) {
        viewModelScope.launch(appCoroutineDispatcher.main) {
            command.value = Command.ShowAddTodoTimePicker(dateTimeStamp)
        }
    }

    fun newTodoTimeSelected(dateTimeStamp: Long, hour: Int, minute: Int) {
        viewModelScope.launch(appCoroutineDispatcher.io) {
            val calendar = Calendar.getInstance(TimeZone.getDefault())
            calendar.time = Date(dateTimeStamp)
            calendar.set(Calendar.HOUR_OF_DAY, hour)
            calendar.set(Calendar.MINUTE, minute)

            val timeStampMilliSeconds = TimeUnit.MILLISECONDS.toSeconds(calendar.timeInMillis)

            logger.d(
                "with time stamp $timeStampMilliSeconds + ${getFormattedTime(calendar.timeInMillis)} + ${
                    getFormattedDate(
                        calendar.timeInMillis
                    )
                }"
            )
            withContext(appCoroutineDispatcher.main) {
                command.value = Command.ShowAddTodoDetailsDialog(calendar.timeInMillis)
            }
        }
    }

    fun actionAddTodoItemClicked() {
        command.value = Command.ShowAddTodoDatePicker
    }
}