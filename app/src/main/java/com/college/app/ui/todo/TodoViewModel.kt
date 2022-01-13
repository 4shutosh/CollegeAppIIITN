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
import com.college.app.utils.extensions.toLiveData
import com.college.app.utils.extensions.updateDate
import com.college.app.utils.extensions.updateTime
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
import javax.inject.Inject
import kotlin.math.ceil

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

        class ShowEditTodoDatePicker(val itemId: Long, val dateTimeStamp: Long) : Command()
        class ShowEditTodoTimerPicker(val itemId: Long, val timeStampMillis: Long) : Command()
        class ShowEditTodoDetailsFragment(val id : Long, val title: String, val description: String, val timeStampMillis: Long) : Command()
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
        val currentWeek = ceil(dayOfMonth.toDouble() / 7) // not the perfect solution but okay
        val currentMonthNumber = now.monthNumber

        todoRepository.getAllTodoFlowSorted().cancellable()
            .map { todoDbList ->
                todoDbList.filter { item ->
                    val itemLocalTime = Instant.fromEpochMilliseconds(item.timeStampMilliSeconds)
                        .toLocalDateTime(timeZone)
                    when (type) {
                        ALL -> true
                        TODAY -> itemLocalTime.date == todayDate && item.timeStampMilliSeconds > currentTimeStamp
                        WEEK -> {
                            val week = ceil(itemLocalTime.dayOfMonth.toDouble() / 7)
                            week == currentWeek && item.timeStampMilliSeconds > currentTimeStamp
                        }
                        MONTH -> itemLocalTime.monthNumber == currentMonthNumber && item.timeStampMilliSeconds > currentTimeStamp
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

            command.postValue(Command.ShowSnackBar("Todo Deleted"))
        }
    }

    fun addNewTodo(
        itemId: Long = 0L,
        title: String,
        description: String,
        timeStampMillis: Long = -1L
    ) {
        viewModelScope.launch(appCoroutineDispatcher.io) {
            val item = if (itemId != 0L) {
                // update
                todoRepository.getTodoWithId(itemId).copy(name = title, description = description)
            } else {
                // insert
                TodoItem(
                    name = title,
                    description = description,
                    timeStampMilliSeconds = timeStampMillis
                )
            }

            todoRepository.insertOrUpdateTodo(item)
            command.postValue(Command.ShowSnackBar("New TODO Added!"))
        }
    }

    fun newTodoDateSelected(dateTimeStamp: Long) {
        viewModelScope.launch(appCoroutineDispatcher.main) {
            command.value = Command.ShowAddTodoTimePicker(dateTimeStamp)
        }
    }

    fun newTodoTimeSelected(dateTimeStamp: Long, hour: Int, minute: Int) {
        viewModelScope.launch(appCoroutineDispatcher.io) {
            val newTimeStamp = updateTime(dateTimeStamp, hour, minute)

            command.postValue(Command.ShowAddTodoDetailsDialog(newTimeStamp))
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

    fun actionEditTodoItemDate(viewState: TodoListViewState) {
        viewModelScope.launch(appCoroutineDispatcher.io) {
            val item = todoRepository.getTodoWithId(viewState.id)
            command.postValue(Command.ShowEditTodoDatePicker(item.id, item.timeStampMilliSeconds))
        }
    }

    fun actionEditTodoItemTime(viewState: TodoListViewState) {
        viewModelScope.launch(appCoroutineDispatcher.io) {
            val item = todoRepository.getTodoWithId(viewState.id)
            command.postValue(Command.ShowEditTodoTimerPicker(item.id, item.timeStampMilliSeconds))
        }
    }

    fun actionEditItemDetails(viewState: TodoListViewState) {
        viewModelScope.launch(appCoroutineDispatcher.io) {

            val item = todoRepository.getTodoWithId(viewState.id)

            command.postValue(
                Command.ShowEditTodoDetailsFragment(
                    id = viewState.id,
                    title = viewState.title,
                    description = viewState.description.orEmpty(),
                    timeStampMillis = item.timeStampMilliSeconds
                )
            )
        }
    }

    fun todoItemDateUpdated(itemId: Long, newDateStamp: Long) {
        viewModelScope.launch(appCoroutineDispatcher.io) {
            val item = todoRepository.getTodoWithId(itemId)

            val newTimeMilliSeconds = updateDate(item.timeStampMilliSeconds, newDateStamp)
            todoRepository.insertOrUpdateTodo(item.copy(timeStampMilliSeconds = newTimeMilliSeconds))

            command.postValue(Command.ShowSnackBar("Date Updated!"))
        }
    }

    fun todoItemTimeUpdated(itemId: Long, hour: Int, minute: Int) {
        viewModelScope.launch(appCoroutineDispatcher.io) {
            val item = todoRepository.getTodoWithId(itemId)

            val newTimeStamp = updateTime(item.timeStampMilliSeconds, hour, minute)

            todoRepository.insertOrUpdateTodo(item.copy(timeStampMilliSeconds = newTimeStamp))

            command.postValue(Command.ShowSnackBar("Time Updated!"))
        }
    }
}