package com.college.app.ui.todo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.college.app.databinding.FragmentTodoBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.abs

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
        super.onViewCreated(view, savedInstanceState)
        initViews()
        setObservers()
    }

    private fun initViews() {
        binding.fragmentTodoList.adapter = todoAdapter
        (binding.fragmentTodoList.itemAnimator as SimpleItemAnimator).supportsChangeAnimations =
            false
    }

    private fun setObservers() {
        viewModel.todoList.observe(viewLifecycleOwner) {
            todoAdapter.submitList(it)
        }
    }

    override fun onTodoItemClickListener(viewState: TodoListViewState, position: Int) {
        bringItemToView(binding.fragmentTodoList, position)
    }

    private fun bringItemToView(recyclerView: RecyclerView, position: Int) {
        val first = recyclerView.getChildAt(0)
        val height = first.height
        val current = recyclerView.getChildAdapterPosition(first)
        val p = abs(position - current)

        val distance = if (p > 2) (p - (p - 2)) * height
        else p * height

        (recyclerView.layoutManager as LinearLayoutManager).scrollToPositionWithOffset(
            position,
            distance
        )
    }

}