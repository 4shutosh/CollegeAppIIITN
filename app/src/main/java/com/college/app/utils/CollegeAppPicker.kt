package com.college.app.utils

import androidx.annotation.StringRes
import androidx.fragment.app.FragmentManager
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker

// todo set custom themes here
class CollegeAppPicker {

    private val constraintsBuilder =
        CalendarConstraints.Builder().setValidator(DateValidatorPointForward.now())

    companion object {
        private const val DATE_PICKER_TAG = "CollegeAppDatePickerTag"
    }


    fun showDatePickerDialog(
        @StringRes titleText: Int,
        fragmentManager: FragmentManager,
        onSelected: (Long) -> Unit,
        onDismiss: (() -> Unit)? = null
    ) {

        val materialDatePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText(titleText)
            .setCalendarConstraints(constraintsBuilder.build())
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
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


}