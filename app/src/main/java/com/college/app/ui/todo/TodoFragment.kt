package com.college.app.ui.todo

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.SimpleItemAnimator
import com.college.app.R
import com.college.app.databinding.FragmentTodoBinding
import com.college.app.ui.todo.TodoListAdapter.Companion.TODO_LIST_VIEW_TYPE_COMPLETE
import com.college.app.ui.todo.TodoViewModel.Command
import com.college.app.ui.todo.TodoViewModel.Command.ShowAddTodoDatePicker
import com.college.app.ui.todo.broadcast.TodoBroadcastReceiver
import com.college.app.ui.todo.broadcast.TodoBroadcastReceiver.Companion.KEY_TODO_DESCRIPTION
import com.college.app.ui.todo.broadcast.TodoBroadcastReceiver.Companion.KEY_TODO_ID
import com.college.app.ui.todo.broadcast.TodoBroadcastReceiver.Companion.KEY_TODO_TITLE
import com.college.app.ui.todo.broadcast.TodoBroadcastReceiver.Companion.TODO_ACTION_SEND_NOTIFICATION
import com.college.app.ui.todo.newTodo.AddTodoDetailsDialogFragment
import com.college.app.utils.CollegeAppPicker
import com.college.app.utils.SwipeActionRecyclerViewItem
import com.college.app.utils.extensions.bringItemToView
import com.college.app.utils.extensions.gone
import com.college.app.utils.extensions.visible
import com.college.base.logger.CollegeLogger
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipDrawable
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class TodoFragment : Fragment(), TodoListAdapter.TodoItemClickListener,
    SwipeActionRecyclerViewItem.SwipeActionCallback {

    @Inject
    lateinit var logger: CollegeLogger

    private lateinit var binding: FragmentTodoBinding

    private val viewModel: TodoViewModel by viewModels()

    private val todoAdapter by lazy { TodoListAdapter(this) }

    private val itemTouchHelper by lazy {
        SwipeActionRecyclerViewItem(
            requireContext(),
            swipeLeftIcon = R.drawable.ic_delete_sweep_24dp,
            swipeRightIcon = R.drawable.ic_baseline_done_all_24,
            swipeLeftBgColor = R.color.green400,
            swipeRightBgColor = android.R.color.holo_red_dark,
            swipeActionCallback = this
        )
    }

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
        val touchHelper = ItemTouchHelper(itemTouchHelper)
        touchHelper.attachToRecyclerView(binding.fragmentTodoList)
        (binding.fragmentTodoList.itemAnimator as SimpleItemAnimator).supportsChangeAnimations =
            true

        binding.fragmentTodoAddFab.setOnClickListener {
            showAddTodoDatePickerDialog()
        }

        viewModel.todoListStateTypes.forEachIndexed { index, it ->
            val chipToAdd = Chip(requireContext())
            val chipDrawable = ChipDrawable.createFromAttributes(
                requireContext(),
                null,
                0,
                R.style.CollegeAppTodoChipItem
            )
            chipToAdd.apply {
                setChipDrawable(chipDrawable)
                setTextAppearance(R.style.CollegeAppTodoChipItemText)
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

    private fun processCommand(it: Command) {
        when (it) {
            is ShowAddTodoDatePicker -> showAddTodoDatePickerDialog()
            is Command.ShowAddTodoTimePicker -> showAddTodoTimePickerDialog(it.dateTimeStamp)
            is Command.ShowAddTodoDetailsDialog -> showAddTodoDetailsDialog(dateAndTimeStamp = it.dateAndTimeStamp)
            is Command.ShowSnackBar -> showSnackBar(it.message, it.showAction)

            is Command.ShowEditTodoDatePicker -> showEditTodoDatePickerDialog(
                it.itemId,
                it.dateTimeStamp
            )
            is Command.ShowEditTodoTimerPicker -> showEditTodoTimePickerDialog(
                it.itemId,
                it.timeStampMillis
            )
            is Command.ShowEditTodoDetailsFragment -> showAddTodoDetailsDialog(
                id = it.id,
                title = it.title,
                description = it.description,
                dateAndTimeStamp = it.timeStampMillis
            )
            is Command.ActionTodoNotification -> processTodoNotification(
                it.start,
                it.timeStampMillis,
                it.title,
                it.description,
                it.itemId
            )
        }
    }

    override fun onTodoItemClick(viewState: TodoListViewState, position: Int) {
        binding.fragmentTodoList.bringItemToView(position)
    }

    override fun onTodoItemDelete(viewState: TodoListViewState, position: Int) {
        viewModel.actionTodoDelete(viewState.id)
    }

    override fun onTodoItemEdit(viewState: TodoListViewState, position: Int) {
        viewModel.actionEditItemDetails(viewState)
    }

    override fun onTodoItemEditDate(viewState: TodoListViewState, position: Int) {
        viewModel.actionEditTodoItemDate(viewState)
    }

    override fun onTodoItemEditTime(viewState: TodoListViewState, position: Int) {
        viewModel.actionEditTodoItemTime(viewState)
    }

    override fun onTodoItemNotifyClicked(viewState: TodoListViewState, notify: Boolean) {
        viewModel.todoItemNotifyUpdated(viewState, notify)
    }

    override fun onTodoItemMarkAsDone(viewState: TodoListViewState, position: Int) {
        viewModel.todoItemMarkAsDone(viewState.id)
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

    private fun showEditTodoDatePickerDialog(itemId: Long, initialDate: Long) {
        CollegeAppPicker().showDatePickerDialog(
            R.string.add_title_todo,
            onSelected = {
                viewModel.todoItemDateUpdated(itemId, it)
            },
            initialDate = initialDate,
            fragmentManager = childFragmentManager
        )
    }

    private fun showEditTodoTimePickerDialog(itemId: Long, initialTime: Long) {
        CollegeAppPicker().showTimePickerDialog(
            R.string.add_title_todo,
            onSelected = {
                viewModel.todoItemTimeUpdated(itemId, it.hour, it.minute)
            },
            initialTime = initialTime,
            fragmentManager = childFragmentManager
        )
    }

    private fun showAddTodoDetailsDialog(
        id: Long = 0L,
        title: String = "",
        description: String = "",
        dateAndTimeStamp: Long
    ) {
        val addTodoDetailsDialog = AddTodoDetailsDialogFragment.instance(
            id = id,
            title = title,
            description = description,
            todoDateAndTimeStamp = dateAndTimeStamp
        )
        addTodoDetailsDialog.show(childFragmentManager, ADD_TODO_DETAILS_DIALOG_TAG)
    }

    private fun showSnackBar(message: String, showAction: Boolean) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT)
            .show()
    }

    private fun processTodoNotification(
        start: Boolean, timeStamp: Long, title: String,
        description: String, todoItemId: Long
    ) {

        val alarmManager = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager

        // create new intent
        val intent = Intent(requireContext(), TodoBroadcastReceiver::class.java)
        intent.action = TODO_ACTION_SEND_NOTIFICATION
        intent.putExtra(KEY_TODO_ID, todoItemId.toInt())
        intent.putExtra(KEY_TODO_TITLE, title)
        if (description.isNotEmpty()) intent.putExtra(KEY_TODO_DESCRIPTION, description)

        val pendingIntent = PendingIntent.getBroadcast(
            requireContext(),
            todoItemId.toInt(),
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        if (start) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    timeStamp,
                    pendingIntent
                )
            } else {
                alarmManager.setExact(
                    AlarmManager.RTC_WAKEUP,
                    timeStamp,
                    pendingIntent
                )
            }
        } else {
            alarmManager.cancel(pendingIntent)
        }
    }

    companion object {
        private const val ADD_TODO_DETAILS_DIALOG_TAG = "ADD_TODO_DETAILS_DIALOG_TAG"
    }

    override fun onLeftSwipe(position: Int) {
        logger.d("onLeftSwipe")
        if (todoAdapter.getItemViewType(position) != TODO_LIST_VIEW_TYPE_COMPLETE)
            viewModel.todoItemMarkAsDone(todoAdapter.getItemId(position))
        else todoAdapter.notifyItemChanged(position) // in order to remake the view and
    }

    override fun onRightSwipe(position: Int) {
        logger.d("onRightSwipe")
        viewModel.actionTodoDelete(todoAdapter.getItemId(position))
    }

}