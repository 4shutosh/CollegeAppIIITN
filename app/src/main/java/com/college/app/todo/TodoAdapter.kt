package com.college.app.todo
//
//import android.animation.ObjectAnimator
//import android.app.AlarmManager
//import android.app.DatePickerDialog
//import android.app.PendingIntent
//import android.app.TimePickerDialog
//import android.content.*
//import android.graphics.*
//import android.util.Log
//import android.view.*
//import android.widget.CheckBox
//import android.widget.EditText
//import android.widget.Toast
//import androidx.constraintlayout.widget.Constraints
//import androidx.core.content.ContextCompat
//import androidx.recyclerview.widget.RecyclerView
//import com.college.app.CollegeApplication
//import com.college.app.MainActivity
//import com.college.app.R
//import com.college.app.databinding.TodoItemBinding
//import com.college.app.utils.*
//import com.google.android.material.button.MaterialButton
//import com.google.android.material.dialog.MaterialAlertDialogBuilder
//import com.google.android.material.snackbar.Snackbar
//import java.util.*
//
//class TodoAdapter constructor(
//    context: Context,
//    private var list: List<Todo>
//) : RecyclerView.Adapter<TodoAdapter.Item>() {
////    private var mContext: Context? = null
////    private var boxStore: BoxStore? = null
////    private var adapterBox: Box<Todo>? = null
//    var mYear = 0
//    var mMonth = 0
//    var mDay = 0
//    var _myPendingIntent: PendingIntent? = null
//    var mHour = 0
//    var mMin = 0
//    var todoYear = 0
//    var todoMonth = 0
//    var todoDay = 0
//    var todoHour = 0
//    var todoMin = 0
//    private var reminder: Calendar
//
//    init {
//        boxStore = ((context as MainActivity).application as CollegeApplication).boxStore
//        adapterBox = boxStore!!.boxFor(Todo::class.java)
//        reminder = Calendar.getInstance()
//    }
//
//    inner class Item internal constructor(binding: TodoItemBinding) :
//        RecyclerView.ViewHolder(binding.root), View.OnClickListener {
//        var checkBox: CheckBox? = null
//        val todoItemBinding: TodoItemBinding
//        override fun onClick(v: View) {
//            when (v.id) {
//                R.id.editTodoItem -> showDialog(
//                    MODE_EDIT,
//                    list!![adapterPosition].id,
//                    list!![adapterPosition].completed
//                )
//                R.id.deleteTodoItem -> {
//                    deleteTodo(list!![adapterPosition].id)
//                    updateTodoView()
//                    Snackbar.make(v, "Todo Deleted", Snackbar.LENGTH_SHORT)
//                        .setAction("Okay") { view: View? -> }
//                        .show() // TODO: 26/4/20 think of toast & snackbar
//                }
//                R.id.todo_title_checkbox -> {
//                    val todo = getTodoById(list!![adapterPosition].id)
//                    isChecked = todoItemBinding.todoTitleCheckbox.isChecked
//                    if (isChecked!!) {
//                        list!![adapterPosition].completed = true
//                        todoItemBinding.todoTitleCheckbox.isChecked = true
//                        if (todo != null) {
//                            addOrUpdateTodo(
//                                Todo(
//                                    todo.id,
//                                    todo.title,
//                                    todo.description,
//                                    true,
//                                    false
//                                )
//                            )
//                        }
//                        todoItemBinding.todoTitle.paintFlags =
//                            todoItemBinding.todoTitle.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
//                    } else {
//                        todoItemBinding.todoTitleCheckbox.isChecked = false
//                        list!![adapterPosition].completed = false
//                        if (todo != null) {
//                            addOrUpdateTodo(
//                                Todo(
//                                    todo.id,
//                                    todo.title,
//                                    todo.description,
//                                    false,
//                                    false
//                                )
//                            )
//                        }
//                        todoItemBinding.todoTitle.paintFlags =
//                            todoItemBinding.todoTitle.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
//                    }
//                }
//                R.id.clock -> {
//                    val t = getTodoById(list!![adapterPosition].id)
//                    val alarmManager =
//                        mContext!!.getSystemService(Context.ALARM_SERVICE) as AlarmManager
//                    val _myIntent = Intent(mContext, TodoBroadcast::class.java)
//                    if (!todoItemBinding.todoDate.text.toString()
//                            .isEmpty() && !todoItemBinding.todoTime.text.toString().isEmpty()
//                    ) {
//                        if (todoItemBinding.clock.isChecked) {
//                            // saving the todo details
//                            t?.let {
//                                Todo(
//                                    it.id,
//                                    it.title,
//                                    it.description,
//                                    false,
//                                    true,
//                                    todoDay,
//                                    todoMonth,
//                                    todoYear,
//                                    todoHour,
//                                    todoMin
//                                )
//                            }?.let {
//                                addOrUpdateTodo(
//                                    it
//                                )
//                            }
//                            // for notification purpose
//                            //                            reminder.setTimeInMillis(System.currentTimeMillis());
//                            Log.d(Constraints.TAG, "onClick: " + t?.month)
//                            if (t != null) {
//                                reminder.set(
//                                    t.year,
//                                    t.month - 1,
//                                    t.day,
//                                    t.hour,
//                                    t.min,
//                                    0
//                                )
//                            }
//                            _myIntent.putExtra("MyMessage", t?.title)
//                            _myIntent.putExtra("todoId", t?.id)
//                            _myPendingIntent = PendingIntent.getBroadcast(
//                                mContext,
//                                123,
//                                _myIntent,
//                                PendingIntent.FLAG_UPDATE_CURRENT
//                            )
//                            alarmManager[AlarmManager.RTC_WAKEUP, reminder.getTimeInMillis()] =
//                                _myPendingIntent
//                            Toast.makeText(
//                                mContext, "You will be notified on " + todoItemBinding.todoDate.text
//                                        + " on " + todoItemBinding.todoTime.text, Toast.LENGTH_LONG
//                            ).show()
//                            if (t != null) {
//                                Log.d(Constraints.TAG, "onClick: " + t.title + t.id)
//                            }
//                        } else {
//                            alarmManager.cancel(_myPendingIntent)
//                            Toast.makeText(mContext, "Notification Stopped", Toast.LENGTH_SHORT)
//                                .show()
//                        }
//                    } else {
//                        todoItemBinding.clock.isChecked = false
//                        Toast.makeText(mContext, "Date and Time are required!", Toast.LENGTH_SHORT)
//                            .show()
//                    }
//                }
//            }
//        }
//
//        fun toggleDetailsVisibility() {
//            if (todoItemBinding.details.visibility == View.GONE) {
//                ObjectAnimator.ofFloat(todoItemBinding.expandIcon, "rotation", 0f, 180f).start()
//                AnimationUtils.expand(todoItemBinding.details)
//            } else {
//                ObjectAnimator.ofFloat(todoItemBinding.expandIcon, "rotation", 180f, 0f).start()
//                AnimationUtils.collapse(todoItemBinding.details)
//            }
//        }
//
//        fun datePickerDialog() {
//            val todo = getTodoById(list!![adapterPosition].id)
//            val c = Calendar.getInstance()
//            mYear = c[Calendar.YEAR]
//            mMonth = c[Calendar.MONTH]
//            mDay = c[Calendar.DAY_OF_MONTH]
//            val datePickerDialogRecieved =
//                DatePickerDialog(mContext!!, { view, year, month, dayOfMonth ->
//                    todoYear = year
//                    todoMonth = month
//                    todoDay = dayOfMonth
//                    //                    todo.setYear(year);
////                    todo.setMonth(month);
////                    todo.setDay(dayOfMonth);
//                    todoItemBinding.todoDate.text =
//                        "Date: " + dayOfMonth + "-" + (month + 1) + "-" + year
//                }, mYear, mMonth, mDay)
//            val positiveColor = ContextCompat.getColor(mContext!!, R.color.colorOnSurface)
//            datePickerDialogRecieved.show()
//            datePickerDialogRecieved.getButton(DatePickerDialog.BUTTON_POSITIVE)
//                .setTextColor(positiveColor)
//            datePickerDialogRecieved.getButton(DatePickerDialog.BUTTON_NEGATIVE)
//                .setTextColor(positiveColor)
//        }
//
//        private fun timePickerDialog() {
//            val todo = getTodoById(list!![adapterPosition].id)
//            val c = Calendar.getInstance()
//            mHour = c[Calendar.HOUR_OF_DAY]
//            mMin = c[Calendar.MINUTE]
//            val timePickerDialog = TimePickerDialog(mContext, { view, hourOfDay, minute ->
//                todoHour = hourOfDay
//                todoMin = minute
//                //                    todo.setHour(hourOfDay);
////                    todo.setMin(minute);
//                todoItemBinding.todoTime.text = "Time: $hourOfDay:$minute"
//            }, mHour, mMin, true)
//            val positiveColor = ContextCompat.getColor(mContext!!, R.color.colorOnSurface)
//            timePickerDialog.show()
//            timePickerDialog.getButton(TimePickerDialog.BUTTON_POSITIVE).setTextColor(positiveColor)
//            timePickerDialog.getButton(TimePickerDialog.BUTTON_NEGATIVE).setTextColor(positiveColor)
//        }
//
//        /// check this for checkbox issue
//        fun bindCheck(n: Int) {
//            Log.d(Constraints.TAG, "bindCheck: started")
//            val status = list!![n].completed
//            if (status!!) {
//                todoItemBinding.todoTitleCheckbox.isChecked = true
//                todoItemBinding.todoTitle.paintFlags =
//                    todoItemBinding.todoTitle.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
//            } else {
//                Log.d(Constraints.TAG, "bindCheck: false executed")
//                todoItemBinding.todoTitleCheckbox.isChecked = false
//                todoItemBinding.todoTitle.paintFlags =
//                    todoItemBinding.todoTitle.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
//            }
//        }
//
//        //                        todoItemBinding.todoTitle.setPaintFlags(todoItemBinding.todoTitle.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
//        init {
//            setIsRecyclable(false)
//            todoItemBinding = binding
//            todoItemBinding.editTodoItem.setOnClickListener(this)
//            todoItemBinding.deleteTodoItem.setOnClickListener(this)
//            todoItemBinding.todoTitleCheckbox.setOnClickListener(this)
//            todoItemBinding.clock.setOnClickListener(this)
//            todoItemBinding.expandIcon.setOnClickListener { v: View? -> toggleDetailsVisibility() }
//            todoItemBinding.todoTitle.setOnClickListener { v: View? -> toggleDetailsVisibility() }
//            todoItemBinding.todoDate.setOnClickListener { v: View? -> datePickerDialog() }
//            todoItemBinding.todoTime.setOnClickListener { v: View? -> timePickerDialog() }
//        }
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Item {
//        val layoutInflater = LayoutInflater.from(parent.context)
//        val itemBinding = TodoItemBinding.inflate(layoutInflater, parent, false)
//        return Item(itemBinding)
//    }
//
//    override fun onBindViewHolder(holder: Item, position: Int) {
//        holder.todoItemBinding.todoTitle.text = list!![position].title
//        holder.todoItemBinding.todoTitleCheckbox.isChecked = list!![position].completed
//        holder.bindCheck(position)
//        holder.todoItemBinding.clock.isChecked = list!![position].reminderStatus
//        if (list!![position].reminderStatus) {
//            holder.todoItemBinding.todoTime.text =
//                "Time: " + list!![position].hour + ":" + list!![position].min
//            holder.todoItemBinding.todoDate.text =
//                "Date: " + list!![position].day + "-" + list!![position].month + "-" + list!![position].year
//        }
//        //        Log.d(TAG, "onBindViewHolder: status checkbox " + mList.get(position).getCompleted());
//        holder.todoItemBinding.todoDescription.text = list!![position].description
//        Log.d(Constraints.TAG, "onBindViewHolder: Yes")
//    }
//
//    override fun getItemCount(): Int {
//        return list!!.size
//    }
//
//    fun showDialog(mode: Int, id: Long, status: Boolean) {
////        ContextThemeWrapper ctw  = new ContextThemeWrapper(mContext,);
//        val builder = MaterialAlertDialogBuilder(mContext!!)
//        val dialogView =
//            (mContext as MainActivity?)!!.layoutInflater.inflate(R.layout.todo_dailog, null)
//        builder.setView(dialogView)
//        val todoTitleAdd = dialogView.findViewById<EditText>(R.id.todo_titile_add)
//        val todoDescriptionAdd = dialogView.findViewById<EditText>(R.id.todo_details_add)
//        val addTodoButton: MaterialButton = dialogView.findViewById(R.id.addTodoButton)
//        when (mode) {
//            MODE_ADD -> {
//            }
//            MODE_EDIT -> {
//                val todoEdit = getTodoById(id)
//                todoTitleAdd.setText(todoEdit?.title)
//                todoDescriptionAdd.setText(todoEdit?.description)
//            }
//            else -> {
//            }
//        }
//        val dialog = builder.create()
//        dialog.show()
//        addTodoButton.setOnClickListener { v: View? ->
//            when (mode) {
//                MODE_ADD -> {
//                    if (todoTitleAdd.text.toString().isEmpty()) {
//                        todoTitleAdd.error = mContext!!.getString(R.string.todoTitleError)
//                        todoTitleAdd.requestFocus()
//                        return@setOnClickListener
//                    }
//                    addOrUpdateTodo(
//                        Todo(
//                            id,
//                            todoTitleAdd.text.toString(),
//                            todoDescriptionAdd.text.toString(),
//                            false,
//                            false
//                        )
//                    )
//                    updateTodoView()
//                    todoTitleAdd.text.clear()
//                    todoDescriptionAdd.text.clear()
//                    Toast.makeText(mContext, "Todo Added", Toast.LENGTH_SHORT).show()
//                    dialog.cancel()
//                }
//                MODE_DELETE -> {
//                    deleteTodo(id)
//                    updateTodoView()
//                }
//                MODE_EDIT -> {
//                    if (todoTitleAdd.text.toString().isEmpty()) {
//                        todoTitleAdd.error = mContext!!.getString(R.string.todoTitleError)
//                        todoTitleAdd.requestFocus()
//                        return@setOnClickListener
//                    }
//                    addOrUpdateTodo(
//                        Todo(
//                            id,
//                            todoTitleAdd.text.toString(),
//                            todoDescriptionAdd.text.toString(),
//                            false,
//                            false
//                        )
//                    )
//                    updateTodoView()
//                    dialog.cancel()
//                }
//                else -> {
//                }
//            }
//        }
//    }
//
//    private fun getTodoById(id: Long): Todo? {
//        return adapterBox!!.query().equal(Todo_.id, id).build().findUnique()
//    }
//
//    private fun addOrUpdateTodo(todo: Todo) {
//        adapterBox!!.put(todo)
//    }
//
//    private fun setTodo(list: List<Todo>) {
//        this.list = list
//        notifyDataSetChanged()
//    }
//
//    private fun deleteTodo(id: Long) {
//        val todo = getTodoById(id)
//        if (todo != null) {
//            adapterBox!!.remove(id)
//        }
//    }
//
//    fun updateTodoView() {
//        val todos = adapterBox!!.query().build().find()
//        setTodo(todos)
//    }
//
//    fun deleteAll() {
//        if (!adapterBox!!.isEmpty) {
//            MaterialAlertDialogBuilder(mContext!!, R.style.MaterialAlertDialog)
//                .setTitle(mContext!!.getString(R.string.delete_all_todo_dialog))
//                .setPositiveButton(mContext!!.getString(R.string.yes)) { dialog: DialogInterface?, which: Int ->
//                    adapterBox!!.removeAll()
//                    Toast.makeText(mContext, "Deleted!", Toast.LENGTH_SHORT).show()
//                    updateTodoView()
//                }
//                .setNegativeButton(mContext!!.getString(R.string.cancel)) { dialog: DialogInterface?, which: Int -> }
//                .show()
//        } else {
//            MaterialAlertDialogBuilder(mContext!!, R.style.MaterialAlertDialog)
//                .setTitle(mContext!!.getString(R.string.no_todo_found))
//                .setPositiveButton(mContext!!.getString(R.string.add_one)) { dialog: DialogInterface?, which: Int ->
//                    showDialog(
//                        MODE_ADD, 0, false
//                    )
//                }
//                .show()
//        }
//    }
//
//    fun deleteMarkedAsDone() {
//        if (!adapterBox!!.isEmpty) {
//            MaterialAlertDialogBuilder(mContext!!, R.style.MaterialAlertDialog)
//                .setTitle(mContext!!.getString(R.string.delete_all__marked_todo_dialog))
//                .setPositiveButton(mContext!!.getString(R.string.yes)) { dialog: DialogInterface?, which: Int ->
//                    for (t in adapterBox!!.all) {
//                        if (t.completed) {
//                            deleteTodo(t.id)
//                        }
//                    }
//                    Toast.makeText(mContext, "Removed!", Toast.LENGTH_SHORT).show()
//                    updateTodoView()
//                }
//                .setNegativeButton(mContext!!.getString(R.string.cancel)) { dialog: DialogInterface?, which: Int -> }
//                .show()
//        } else {
//            MaterialAlertDialogBuilder(mContext!!, R.style.MaterialAlertDialog)
//                .setTitle(mContext!!.getString(R.string.no_todo_found))
//                .setPositiveButton(mContext!!.getString(R.string.add_one)) { dialog: DialogInterface?, which: Int ->
//                    showDialog(
//                        MODE_ADD, 0, false
//                    )
//                }
//                .show()
//        }
//    }
//
//    fun markAsDoneNotification(id: Long) {
//        adapterBox = boxStore!!.boxFor(Todo::class.java)
//        val t = getTodoById(id)
//        if (t != null) {
//            addOrUpdateTodo(
//                Todo(
//                    t.id,
//                    t.title,
//                    t.description,
//                    true,
//                    true,
//                    todoDay,
//                    todoMonth,
//                    todoYear,
//                    todoHour,
//                    todoMin
//                )
//            )
//        }
//        Toast.makeText(mContext, "Done!", Toast.LENGTH_SHORT).show()
//    }
//
//    fun snoozeNotificationAction(id: Long) {
//        Log.d(Constraints.TAG, "snoozeNotificationAction: $id")
//        val t = getTodoById(id)
//        val alarmManager = mContext!!.getSystemService(Context.ALARM_SERVICE) as AlarmManager
//        val _myIntent = Intent(mContext, TodoBroadcast::class.java)
//        val _myPendingIntent =
//            PendingIntent.getBroadcast(mContext, 123, _myIntent, PendingIntent.FLAG_UPDATE_CURRENT)
//        reminder = Calendar.getInstance()
//        reminder.setTimeInMillis(System.currentTimeMillis())
//        if (t != null) {
//            Log.d(Constraints.TAG, "onClick: " + t.month)
//            reminder.set(t.year, t.month - 1, t.day, t.hour, t.min, 0)
//            _myIntent.putExtra("MyMessage", t.title)
//            _myIntent.putExtra("todoId", t.id)
//            alarmManager[AlarmManager.RTC_WAKEUP, reminder.getTimeInMillis()] = _myPendingIntent
//            Toast.makeText(mContext, "You'll be snoozed after 10 mins.", Toast.LENGTH_SHORT).show()
//        }
//    } //    @Override
//
//    //    public int getItemViewType(int position) {
//    //        if (position == 0) {
//    //            return 0;
//    //        } else
//    //            return 1;
//    //    }
//    companion object {
//        var isChecked: Boolean? = null
//        const val MODE_ADD = 0
//        private const val MODE_EDIT = 1
//        private const val MODE_DELETE = 2
//    }
//}