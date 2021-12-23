package com.college.app.ui.todo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.SimpleItemAnimator
import com.college.app.databinding.FragmentTodoBinding
import com.college.app.utils.extensions.bringItemToView
import dagger.hilt.android.AndroidEntryPoint
import kotlin.time.ExperimentalTime

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
            false

        binding.fragmentTodoAddFab.setOnClickListener {
            viewModel.addNewTodo()
        }
    }

    private fun setObservers() {
        viewModel.todoList.observe(viewLifecycleOwner) {
            todoAdapter.submitList(it)
        }
    }

    override fun onTodoItemClickListener(viewState: TodoListViewState, position: Int) {
        binding.fragmentTodoList.bringItemToView(position)
    }

}