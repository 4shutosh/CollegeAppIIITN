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

        if (timeStamp == 0L) {
            dismiss()
        }

        binding.dialogAddTodoSaveButton.setOnClickListener {
            val title = binding.dialogAddTodoTitleInput.text.toString()
            val description = binding.dialogAddTodoDescriptionInput.text.toString()

            if (parentViewModel.validateInputNewTodoDetails(title)) {
                parentViewModel.addNewTodo(title, description, timeStamp)
                dismiss()
            }
        }
    }


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