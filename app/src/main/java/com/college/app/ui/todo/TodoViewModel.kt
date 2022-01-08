package com.college.app.ui.todo

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.college.app.data.entities.TodoItem
import com.college.app.data.repositories.todo.TodoRepository
import com.college.app.ui.todo.TodoListTypes.ALL
import com.college.app.ui.todo.TodoListTypes.DEAD
import com.college.app.ui.todo.TodoListTypes.LATER
import com.college.app.ui.todo.TodoListTypes.MONTH
import com.college.app.ui.todo.TodoListTypes.TODAY
import com.college.app.ui.todo.TodoListTypes.WEEK
import com.college.app.utils.extensions.getFormattedDate
import com.college.app.utils.extensions.getFormattedTime
import com.college.app.utils.extensions.toLiveData
import com.college.base.AppCoroutineDispatcher
import com.college.base.SingleLiveEvent
import com.college.base.logger.CollegeLogger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.toLocalDateTime
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

    val todoListStateTypes by lazy { enumValues<TodoListTypes>().toList() }

    sealed class Command {
        object ShowAddTodoDatePicker : Command()
        class ShowAddTodoTimePicker(val dateTimeStamp: Long) : Command()
        class ShowAddTodoDetailsDialog(val dateAndTimeStamp: Long) : Command()
        class ShowSnackBar(val message: String, val showAction: Boolean = false) : Command()
    }

    init {
        viewModelScope.launch(appCoroutineDispatcher.io) {
            getAllTodo()
        }
    }

    private suspend fun getAllTodo(type: TodoListTypes = ALL) {
        val timeZone = kotlinx.datetime.TimeZone.currentSystemDefault()
        val now = Clock.System.now().toLocalDateTime(timeZone)
        val currentTimeStamp = Clock.System.now().toEpochMilliseconds()

        val dayOfMonth = now.dayOfMonth
        val todayDate = now.date
        val currentWeek = dayOfMonth / 7
        val currentMonthNumber = now.monthNumber

        todoRepository.getAllTodoFlowSorted().cancellable()
            .map { todoDbList ->
                todoDbList.filter { item ->
                    val itemLocalTime = Instant.fromEpochMilliseconds(item.timeStampMilliSeconds)
                        .toLocalDateTime(timeZone)
                    when (type) {
                        ALL -> true
                        TODAY -> itemLocalTime.date == todayDate
                        WEEK -> {
                            val week = itemLocalTime.dayOfMonth / 7
                            week == currentWeek
                        }
                        MONTH -> itemLocalTime.monthNumber == currentMonthNumber
                        LATER -> (itemLocalTime.monthNumber != currentMonthNumber && item.timeStampMilliSeconds > currentTimeStamp)
                        DEAD -> item.timeStampMilliSeconds < currentTimeStamp
                    }
                }.toListOfViewState()
            }.collect {
                logger.d(it.toString())
                _todoList.postValue(it)
            }
    }

    fun actionTodoDelete(itemTodo: TodoListViewState, position: Int) {
        viewModelScope.launch(appCoroutineDispatcher.main) {
            withContext(appCoroutineDispatcher.io) {
                todoRepository.deleteTodo(itemTodo.id)
            }

            command.value = Command.ShowSnackBar("Todo Deleted")

        }
    }

    fun addNewTodo(title: String, description: String, timeStampMillis: Long) {
        viewModelScope.launch(appCoroutineDispatcher.io) {
            todoRepository.insertTodo(
                TodoItem(
                    name = title,
                    description = description,
                    timeStampMilliSeconds = timeStampMillis
                )
            )

            withContext(appCoroutineDispatcher.main) {
                command.value = Command.ShowSnackBar("New TODO Added!")
            }
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

    fun validateInputNewTodoDetails(string: String): Boolean = string.isNotEmpty()

    fun actionCheckedChipGroupChanged(checkedId: Int) {
        if (checkedId == -1) return
        viewModelScope.launch(appCoroutineDispatcher.io) {
            getAllTodo(todoListStateTypes[checkedId])
        }
    }
}