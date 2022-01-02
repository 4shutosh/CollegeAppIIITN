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
import com.college.app.utils.CollegeAppPicker
import com.college.app.utils.extensions.bringItemToView
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initViews()
        setObservers()
    }

    private fun initViews() {
        binding.fragmentTodoList.adapter = todoAdapter
        (binding.fragmentTodoList.itemAnimator as SimpleItemAnimator).supportsChangeAnimations =
            true

        binding.fragmentTodoAddFab.setOnClickListener {
            showAddTodoDatePickerDialog()
        }
    }

    private fun setObservers() {
        viewModel.todoList.observe(viewLifecycleOwner) {
            todoAdapter.submitList(ArrayList(it))
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
                Timber.d("selected something $it")
            },
            fragmentManager = childFragmentManager
        )
    }

}