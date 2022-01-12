package com.college.app.ui.todo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.SimpleItemAnimator
import com.college.app.R
import com.college.app.databinding.FragmentTodoBinding
import com.college.app.ui.todo.TodoViewModel.Command
import com.college.app.ui.todo.TodoViewModel.Command.ShowAddTodoDatePicker
import com.college.app.ui.todo.newTodo.AddTodoDetailsDialogFragment
import com.college.app.utils.CollegeAppPicker
import com.college.app.utils.extensions.bringItemToView
import com.college.app.utils.extensions.gone
import com.college.app.utils.extensions.visible
import com.google.android.material.chip.Chip
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TodoFragment : Fragment(), TodoListAdapter.TodoItemClickListener {

    private lateinit var binding: FragmentTodoBinding

    private val viewModel: TodoViewModel by viewModels()

    private val todoAdapter by lazy { TodoListAdapter(this) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTodoBinding.inflate(inflater)
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initViews()
        setObservers()
    }

    private fun initViews() {
        binding.fragmentTodoList.adapter = todoAdapter
        binding.fragmentTodoList.setHasFixedSize(true)
        (binding.fragmentTodoList.itemAnimator as SimpleItemAnimator).supportsChangeAnimations =
            true

        binding.fragmentTodoAddFab.setOnClickListener {
            showAddTodoDatePickerDialog()
        }

        viewModel.todoListStateTypes.forEachIndexed { index, it ->
            val chipToAdd = Chip(requireContext())
            chipToAdd.apply {
                text = it.title
                isChipIconVisible = false
                isCheckedIconVisible = false
                checkedIcon
                isClickable = true
                isCheckable = true
                id = index
            }
            binding.fragmentTodoChipGroup.addView(chipToAdd)
        }
        binding.fragmentTodoChipGroup.check(0)

        binding.fragmentTodoChipGroup.setOnCheckedChangeListener { _, checkedId ->
            viewModel.actionCheckedChipGroupChanged(checkedId)
        }
    }

    private fun setObservers() {
        viewModel.todoList.observe(viewLifecycleOwner) {
            if (it.isEmpty()) {
                binding.fragmentTodoList.gone()
                binding.fragmentTodoNoDataImage.visible()
                binding.fragmentTodoNoDataMessage.visible()
            } else {
                binding.fragmentTodoNoDataMessage.gone()
                binding.fragmentTodoNoDataImage.gone()
                todoAdapter.submitList(it)
                binding.fragmentTodoList.visible()
            }
        }

        viewModel.command.observe(
            viewLifecycleOwner
        ) {
            processCommand(it)
        }
    }

    private fun processCommand(command: Command) {
        when (command) {
            is ShowAddTodoDatePicker -> showAddTodoDatePickerDialog()
            is Command.ShowAddTodoTimePicker -> showAddTodoTimePickerDialog(command.dateTimeStamp)
            is Command.ShowAddTodoDetailsDialog -> showAddTodoDetailsDialog(command.dateAndTimeStamp)
            is Command.ShowSnackBar -> showSnackBar(command.message, command.showAction)
        }
    }

    override fun onTodoItemClick(viewState: TodoListViewState, position: Int) {
        binding.fragmentTodoList.bringItemToView(position)
    }

    override fun onTodoItemDelete(viewState: TodoListViewState, position: Int) {
        viewModel.actionTodoDelete(viewState, position)
    }

    override fun onTodoItemEdit(viewState: TodoListViewState, position: Int) {

    }

    private fun showAddTodoDatePickerDialog() {
        CollegeAppPicker().showDatePickerDialog(
            R.string.add_title_todo,
            onSelected = {
                viewModel.newTodoDateSelected(it)
            },
            fragmentManager = childFragmentManager
        )
    }

    private fun showAddTodoTimePickerDialog(dateTimestamp: Long) {
        CollegeAppPicker().showTimePickerDialog(
            R.string.add_title_todo,
            onSelected = {
                viewModel.newTodoTimeSelected(dateTimestamp, it.hour, it.minute)
            },
            fragmentManager = childFragmentManager
        )
    }

    private fun showAddTodoDetailsDialog(dateAndTimeStamp: Long) {
        val addTodoDetailsDialog = AddTodoDetailsDialogFragment.instance(
            todoDateAndTimeStamp = dateAndTimeStamp
        )
        addTodoDetailsDialog.show(childFragmentManager, ADD_TODO_DETAILS_DIALOG_TAG)
    }

    private fun showSnackBar(message: String, showAction: Boolean) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT)
            .show()
    }

    companion object {
        private const val ADD_TODO_DETAILS_DIALOG_TAG = "ADD_TODO_DETAILS_DIALOG_TAG"
    }

}