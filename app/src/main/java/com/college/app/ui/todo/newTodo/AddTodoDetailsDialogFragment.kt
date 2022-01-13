package com.college.app.ui.todo.newTodo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.college.app.R
import com.college.app.databinding.DialogAddTodoDetailsBinding
import com.college.app.ui.todo.TodoViewModel
import com.college.app.utils.extensions.createBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddTodoDetailsDialogFragment : DialogFragment(R.layout.dialog_add_todo_details) {

    private val parentViewModel: TodoViewModel by viewModels(
        ownerProducer = { requireParentFragment() }
    )

    private lateinit var binding: DialogAddTodoDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = createBinding(inflater, R.layout.dialog_add_todo_details, container)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViews()
    }

    private fun setUpViews() {

        val timeStamp = arguments?.getLong(KEY_NEW_TODO_TIMESTAMP) ?: 0
        val title = arguments?.getString(KEY_NEW_TODO_TITLE) ?: ""
        val description = arguments?.getString(KEY_NEW_TODO_DESCRIPTION) ?: ""
        val id = arguments?.getLong(KEY_NEW_TODO_ID) ?: 0L

        if (timeStamp == 0L) {
            dismiss()
        }

        binding.dialogAddTodoTitleInput.setText(title)
        binding.dialogAddTodoDescriptionInput.setText(description)

        binding.dialogAddTodoSaveButton.setOnClickListener {
            val title = binding.dialogAddTodoTitleInput.text.toString()
            val description = binding.dialogAddTodoDescriptionInput.text.toString()

            if (parentViewModel.validateInputNewTodoDetails(title)) {
                parentViewModel.addNewTodo(
                    itemId = id,
                    title = title,
                    description = description,
                    timeStampMillis = timeStamp
                )
                dismiss()
            }
        }
    }


    companion object {
        private const val KEY_NEW_TODO_TIMESTAMP = "KEY_NEW_TODO_TIMESTAMP"
        private const val KEY_NEW_TODO_ID = "KEY_NEW_TODO_ID"
        private const val KEY_NEW_TODO_TITLE = "KEY_NEW_TODO_TITLE"
        private const val KEY_NEW_TODO_DESCRIPTION = "KEY_NEW_TODO_DESCRIPTION"

        fun instance(
            id: Long = 0L,
            title: String = "",
            description: String = "",
            todoDateAndTimeStamp: Long,
        ): AddTodoDetailsDialogFragment {
            val dialogFragment = AddTodoDetailsDialogFragment()

            val bundle = Bundle()
            bundle.putLong(KEY_NEW_TODO_TIMESTAMP, todoDateAndTimeStamp)
            bundle.putString(KEY_NEW_TODO_TITLE, title)
            bundle.putString(KEY_NEW_TODO_DESCRIPTION, description)
            bundle.putLong(KEY_NEW_TODO_ID, id)
            dialogFragment.arguments = bundle
            return dialogFragment
        }
    }
}