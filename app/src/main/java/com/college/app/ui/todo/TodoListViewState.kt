package com.college.app.ui.todo

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.library.baseAdapters.BR
import com.college.app.data.entities.TodoItem
import com.college.app.utils.extensions.DateTimeDifferenceUnit
import com.college.app.utils.extensions.calculateTimeLeft
import com.college.app.utils.extensions.getFormattedDate
import com.college.app.utils.extensions.getFormattedTime
import kotlin.properties.Delegates

data class TodoListViewState(
    var id: Long,
    val title: String,
    val description: String?,
    var timeLeft: String,
    var timeLeftUnit: String,
    var time: String,
    var date: String,
    var isNotifyOn: Boolean = false,
    var isCompleted: Boolean = false,

    var _isExpanded: Boolean = false,
) : BaseObservable() {


    @get:Bindable
    var isExpanded: Boolean by Delegates.observable(false) { _, _, _ ->
        notifyPropertyChanged(BR.expanded)
    }

}

fun List<TodoItem>.toListOfViewState(): List<TodoListViewState> {
    val newList = mutableListOf<TodoListViewState>()
    forEach {
        newList.add(
            it.toViewState()
        )
    }
    return newList
}

fun TodoItem.toViewState(): TodoListViewState {

    val timeLeftPair = calculateTimeLeft(timeStampMilliSeconds)

    val timeLeft = when (timeLeftPair.second) {
        DateTimeDifferenceUnit.SECONDS -> "${timeLeftPair.first}!!!"
        DateTimeDifferenceUnit.MINUTES -> "${timeLeftPair.first}!!"
        DateTimeDifferenceUnit.HOURS -> "${timeLeftPair.first}!"
        DateTimeDifferenceUnit.DAYS -> "${timeLeftPair.first}"
        DateTimeDifferenceUnit.MONTHS -> "${timeLeftPair.first}"
        DateTimeDifferenceUnit.YEARS -> "${timeLeftPair.first}"

        DateTimeDifferenceUnit.PAST -> "DEAD"
    }

//    val date = itemInstant.getCalendarFormattedDate()
    val date = getFormattedDate(timeStampMilliSeconds, "EEE, MMM d, ''yy")
    val time = getFormattedTime(timeStampMilliSeconds)

    return TodoListViewState(
        id = id,
        title = name,
        description = description,
        isCompleted = isCompleted,
        date = date,
        time = time,
        timeLeft = timeLeft,
        timeLeftUnit = "${timeLeftPair.second.unit} left",
    )
}