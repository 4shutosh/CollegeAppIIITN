package com.college.app.ui.todo.newTodo

import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.college.app.R
import com.college.app.ui.todo.TodoViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddTodoDetailsDialogFragment : DialogFragment(R.layout.todo_dailog) {

    private val parentViewModel: TodoViewModel by viewModels(
        ownerProducer = { requireParentFragment() }
    )


    companion object {
        private const val KEY_NEW_TODO_TIMESTAMP = "KEY_NEW_TODO_TIMESTAMP"

        fun instance(todoDateAndTimeStamp: Long): AddTodoDetailsDialogFragment {
            val dialogFragment = AddTodoDetailsDialogFragment()

            val bundle = Bundle()
            bundle.putLong(KEY_NEW_TODO_TIMESTAMP, todoDateAndTimeStamp)
            dialogFragment.arguments = bundle
            return dialogFragment
        }
    }
}