package com.college.app.utils

import androidx.annotation.StringRes
import androidx.fragment.app.FragmentManager
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

// todo set custom themes here
class CollegeAppPicker {

    private val constraintsBuilder =
        CalendarConstraints.Builder().setValidator(DateValidatorPointForward.now())

    companion object {
        private const val DATE_PICKER_TAG = "CollegeAppDatePickerTag"
        private const val TIME_PICKER_TAG = "CollegeAppTimePickerTag"
    }


    fun showDatePickerDialog(
        @StringRes titleText: Int,
        fragmentManager: FragmentManager,
        initialDate: Long = -1L,
        onSelected: (Long) -> Unit,
        onDismiss: (() -> Unit)? = null
    ) {

        val materialDatePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText(titleText)
            .setCalendarConstraints(constraintsBuilder.build())
            .setSelection(if (initialDate == -1L) MaterialDatePicker.todayInUtcMilliseconds() else initialDate)
            .build()

        if (onDismiss != null) {
            materialDatePicker.addOnDismissListener {
                onDismiss()
            }
        }

        materialDatePicker.addOnPositiveButtonClickListener {
            onSelected(it)
        }

        materialDatePicker.show(fragmentManager, DATE_PICKER_TAG)
    }

    fun showTimePickerDialog(
        @StringRes titleText: Int,
        fragmentManager: FragmentManager,
        initialTime: Long = -1L,
        onSelected: (CollegeAppTimePickerReturn) -> Unit,
        is24HourFormat: Boolean = false,
        onDismiss: (() -> Unit)? = null
    ) {

        val clockFormat = if (is24HourFormat) TimeFormat.CLOCK_24H else TimeFormat.CLOCK_12H
        val now = Clock.System.now()

        val timeToSet = if (initialTime != -1L) Instant.fromEpochMilliseconds(initialTime)
            .toLocalDateTime(TimeZone.currentSystemDefault())
        else {
            now.toLocalDateTime(TimeZone.currentSystemDefault())
        }

        val materialTimePicker = MaterialTimePicker.Builder()
            .setTitleText(titleText)
            .setHour(timeToSet.hour + 1)
            .setMinute(timeToSet.minute)
            .setTimeFormat(clockFormat)
            .build()

        if (onDismiss != null) {
            materialTimePicker.addOnDismissListener {
                onDismiss()
            }
        }

        materialTimePicker.addOnPositiveButtonClickListener {
            onSelected(
                CollegeAppTimePickerReturn(
                    materialTimePicker.hour,
                    materialTimePicker.minute
                )
            )
        }

        materialTimePicker.show(fragmentManager, DATE_PICKER_TAG)
    }

    data class CollegeAppTimePickerReturn(
        val hour: Int,
        val minute: Int
    )

}