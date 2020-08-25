package com.college.app.todo;

import android.animation.ObjectAnimator;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.college.app.AppClass;
import com.college.app.MainActivity;
import com.college.app.R;
import com.college.app.databinding.TodoItemBinding;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;

import java.util.Calendar;
import java.util.List;

import io.objectbox.Box;
import io.objectbox.BoxStore;

import static androidx.constraintlayout.widget.Constraints.TAG;
import static com.college.app.utils.AnimationUtils.collapse;
import static com.college.app.utils.AnimationUtils.expand;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.Item> {

    private Context mContext;
    private List<Todo> mList;
    private BoxStore boxStore;
    private Box<Todo> adapterBox;
    static Boolean isChecked;
    int mYear, mMonth, mDay;
    PendingIntent _myPendingIntent;
    int mHour, mMin;
    int todoYear, todoMonth, todoDay, todoHour, todoMin;
    Calendar reminder;
    static final int MODE_ADD = 0;
    private static final int MODE_EDIT = 1;
    private static final int MODE_DELETE = 2;

    TodoAdapter(Context mContext, List<Todo> mData) {
        this.mContext = mContext;
        this.mList = mData;
        boxStore = ((AppClass) ((MainActivity) mContext).getApplication()).getBoxStore();
        adapterBox = boxStore.boxFor(Todo.class);
    }

    public TodoAdapter() {
    }

    public class Item extends RecyclerView.ViewHolder implements View.OnClickListener {
        CheckBox checkBox;
        private TodoItemBinding todoItemBinding;

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.editTodoItem:
                    showDialog(MODE_EDIT, mList.get(getAdapterPosition()).getId(), mList.get(getAdapterPosition()).getCompleted());
                    break;
                case R.id.deleteTodoItem:
                    deleteTodo(mList.get(getAdapterPosition()).getId());
                    updateTodoView();
                    Snackbar.make(v, "Todo Deleted", Snackbar.LENGTH_SHORT).setAction("Okay", view -> {
                    }).show();        // TODO: 26/4/20 think of toast & snackbar
                    break;
                case R.id.todo_title_checkbox:
                    Todo todo = getTodoById(mList.get(getAdapterPosition()).getId());
                    isChecked = todoItemBinding.todoTitleCheckbox.isChecked();
                    if (isChecked) {
                        mList.get(getAdapterPosition()).setCompleted(true);
                        todoItemBinding.todoTitleCheckbox.setChecked(true);
                        addOrUpdateTodo(new Todo(todo.getId(), todo.getTitle(), todo.getDescription(), true, false));
                        todoItemBinding.todoTitle.setPaintFlags(todoItemBinding.todoTitle.getPaintFlags() | (Paint.STRIKE_THRU_TEXT_FLAG));
                    } else {
                        todoItemBinding.todoTitleCheckbox.setChecked(false);
                        mList.get(getAdapterPosition()).setCompleted(false);
                        addOrUpdateTodo(new Todo(todo.getId(), todo.getTitle(), todo.getDescription(), false, false));
                        todoItemBinding.todoTitle.setPaintFlags(todoItemBinding.todoTitle.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                    }
                    break;
                case R.id.clock:
                    Todo t = getTodoById(mList.get(getAdapterPosition()).getId());
                    AlarmManager alarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
                    Intent _myIntent = new Intent(mContext, TodoBroadcast.class);
                    if (!todoItemBinding.todoDate.getText().toString().isEmpty() && !todoItemBinding.todoTime.getText().toString().isEmpty()) {
                        if (todoItemBinding.clock.isChecked()) {
                            // saving the todo details
                            addOrUpdateTodo(new Todo(t.getId(), t.getTitle(), t.getDescription(), false, true, todoDay, todoMonth, todoYear, todoHour, todoMin));
                            // for notification purpose
                            reminder = Calendar.getInstance();
//                            reminder.setTimeInMillis(System.currentTimeMillis());
                            Log.d(TAG, "onClick: " + t.getMonth());
                            reminder.set(t.getYear(), t.getMonth() - 1, t.getDay(), t.getHour(), t.getMin(), 0);
                            _myIntent.putExtra("MyMessage", t.getTitle());
                            _myIntent.putExtra("todoId", t.getId());
                            _myPendingIntent = PendingIntent.getBroadcast(mContext, 123, _myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                            alarmManager.set(AlarmManager.RTC_WAKEUP, reminder.getTimeInMillis(), _myPendingIntent);
                            Toast.makeText(mContext, "You will be notified on " + todoItemBinding.todoDate.getText()
                                    + " on " + todoItemBinding.todoTime.getText(), Toast.LENGTH_LONG).show();
                            Log.d(TAG, "onClick: " + t.getTitle() + t.getId());
                        } else {
                            alarmManager.cancel(_myPendingIntent);
                            Toast.makeText(mContext, "Notification Stopped", Toast.LENGTH_SHORT).show();

                        }
                    } else {
                        todoItemBinding.clock.setChecked(false);
                        Toast.makeText(mContext, "Date and Time are required!", Toast.LENGTH_SHORT).show();
                    }

//                    reminder = Calendar.getInstance();
//                    reminder.setTimeInMillis(System.currentTimeMillis());
////                    reminder.set(t.getYear(), t.getMonth(), t.getDay(), t.getHour(), t.getMin(), 0);
//                    AlarmManager alarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
//                    Intent _myIntent = new Intent(mContext, TodoBroadcast.class);
////                    _myIntent.putExtra("MyMessage", t.getTitle());
//                    PendingIntent _myPendingIntent = PendingIntent.getBroadcast(mContext, 123, _myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//                    alarmManager.set(AlarmManager.RTC_WAKEUP, reminder.getTimeInMillis(), _myPendingIntent);
                    break;
            }
        }
//                        todoItemBinding.todoTitle.setPaintFlags(todoItemBinding.todoTitle.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));

        Item(TodoItemBinding binding) {
            super(binding.getRoot());
            this.setIsRecyclable(false);
            this.todoItemBinding = binding;
            todoItemBinding.editTodoItem.setOnClickListener(this);
            todoItemBinding.deleteTodoItem.setOnClickListener(this);
            todoItemBinding.todoTitleCheckbox.setOnClickListener(this);
            todoItemBinding.clock.setOnClickListener(this);
            todoItemBinding.expandIcon.setOnClickListener(v -> toggleDetailsVisibility());
            todoItemBinding.todoTitle.setOnClickListener(v -> toggleDetailsVisibility());
            todoItemBinding.todoDate.setOnClickListener(v -> datePickerDialog());
            todoItemBinding.todoTime.setOnClickListener(v -> timePickerDialog());

        }

        void toggleDetailsVisibility() {
            if (todoItemBinding.details.getVisibility() == View.GONE) {
                ObjectAnimator.ofFloat(todoItemBinding.expandIcon, "rotation", 0, 180).start();
                expand(todoItemBinding.details);
            } else {
                ObjectAnimator.ofFloat(todoItemBinding.expandIcon, "rotation", 180, 0).start();
                collapse(todoItemBinding.details);
            }
        }

        void datePickerDialog() {
            Todo todo = getTodoById(mList.get(getAdapterPosition()).getId());
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePickerDialogRecieved = new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    todoYear = year;
                    todoMonth = month;
                    todoDay = dayOfMonth;
//                    todo.setYear(year);
//                    todo.setMonth(month);
//                    todo.setDay(dayOfMonth);
                    todoItemBinding.todoDate.setText("Date: " + dayOfMonth + "-" + (month + 1) + "-" + year);
                }
            }, mYear, mMonth, mDay);
            int positiveColor = ContextCompat.getColor(mContext, R.color.colorOnSurface);
            datePickerDialogRecieved.show();
            datePickerDialogRecieved.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(positiveColor);
            datePickerDialogRecieved.getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(positiveColor);
        }

        void timePickerDialog() {
            Todo todo = getTodoById(mList.get(getAdapterPosition()).getId());
            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMin = c.get(Calendar.MINUTE);
            TimePickerDialog timePickerDialog = new TimePickerDialog(mContext, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    todoHour = hourOfDay;
                    todoMin = minute;
//                    todo.setHour(hourOfDay);
//                    todo.setMin(minute);
                    todoItemBinding.todoTime.setText("Time: " + hourOfDay + ":" + minute);
                }
            }, mHour, mMin, true);
            int positiveColor = ContextCompat.getColor(mContext, R.color.colorOnSurface);
            timePickerDialog.show();
            timePickerDialog.getButton(TimePickerDialog.BUTTON_POSITIVE).setTextColor(positiveColor);
            timePickerDialog.getButton(TimePickerDialog.BUTTON_NEGATIVE).setTextColor(positiveColor);
        }

        /// check this for checkbox issue
        void bindCheck(int n) {
            Log.d(TAG, "bindCheck: started");
            Boolean status = mList.get(n).getCompleted();
            if (status) {
                todoItemBinding.todoTitleCheckbox.setChecked(true);
                todoItemBinding.todoTitle.setPaintFlags(todoItemBinding.todoTitle.getPaintFlags() | (Paint.STRIKE_THRU_TEXT_FLAG));
            } else {
                Log.d(TAG, "bindCheck: false executed");
                todoItemBinding.todoTitleCheckbox.setChecked(false);
                todoItemBinding.todoTitle.setPaintFlags(todoItemBinding.todoTitle.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            }
        }
    }

    @NonNull
    @Override
    public Item onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        TodoItemBinding itemBinding = TodoItemBinding.inflate(layoutInflater, parent, false);
        return new Item(itemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull Item holder, int position) {

        holder.todoItemBinding.todoTitle.setText(mList.get(position).getTitle());
        holder.todoItemBinding.todoTitleCheckbox.setChecked(mList.get(position).getCompleted());
        holder.bindCheck(position);
        holder.todoItemBinding.clock.setChecked(mList.get(position).getReminderStatus());
        if (mList.get(position).getReminderStatus()) {
            holder.todoItemBinding.todoTime.setText("Time: " + mList.get(position).getHour() + ":" + mList.get(position).getMin());
            holder.todoItemBinding.todoDate.setText("Date: " + mList.get(position).getDay() + "-" + mList.get(position).getMonth() + "-" + mList.get(position).getYear());
        }
//        Log.d(TAG, "onBindViewHolder: status checkbox " + mList.get(position).getCompleted());
        holder.todoItemBinding.todoDescription.setText(mList.get(position).getDescription());
        Log.d(TAG, "onBindViewHolder: Yes");

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    void showDialog(final int mode, final long id, boolean status) {
//        ContextThemeWrapper ctw  = new ContextThemeWrapper(mContext,);
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(mContext);
        View dialogView = ((MainActivity) mContext).getLayoutInflater().inflate(R.layout.todo_dailog, null);

        builder.setView(dialogView);

        EditText todoTitleAdd = dialogView.findViewById(R.id.todo_titile_add);
        EditText todoDescriptionAdd = dialogView.findViewById(R.id.todo_details_add);
        MaterialButton addTodoButton = dialogView.findViewById(R.id.addTodoButton);

        switch (mode) {
            case MODE_ADD:
                break;
            case MODE_EDIT:
                Todo todoEdit = getTodoById(id);
                todoTitleAdd.setText(todoEdit.getTitle());
                todoDescriptionAdd.setText(todoEdit.getDescription());
                break;
            default:
                break;
        }
        AlertDialog dialog = builder.create();
        dialog.show();

        addTodoButton.setOnClickListener(v -> {
            switch (mode) {
                case MODE_ADD:
                    if (todoTitleAdd.getText().toString().isEmpty()) {
                        todoTitleAdd.setError(mContext.getString(R.string.todoTitleError));
                        todoTitleAdd.requestFocus();
                        return;
                    }
                    addOrUpdateTodo(new Todo(id, todoTitleAdd.getText().toString(), todoDescriptionAdd.getText().toString(), false, false));
                    updateTodoView();
                    todoTitleAdd.getText().clear();
                    todoDescriptionAdd.getText().clear();
                    Toast.makeText(mContext, "Todo Added", Toast.LENGTH_SHORT).show();
                    dialog.cancel();

                    break;

                case MODE_DELETE:
                    deleteTodo(id);
                    updateTodoView();
                    break;

                case MODE_EDIT:
                    if (todoTitleAdd.getText().toString().isEmpty()) {
                        todoTitleAdd.setError(mContext.getString(R.string.todoTitleError));
                        todoTitleAdd.requestFocus();
                        return;
                    }
                    addOrUpdateTodo(new Todo(id, todoTitleAdd.getText().toString(), todoDescriptionAdd.getText().toString(), false, false));
                    updateTodoView();
                    dialog.cancel();
                    break;

                default:
                    break;
            }
        });
    }


    private Todo getTodoById(long id) {
        return adapterBox.query().equal(Todo_.id, id).build().findUnique();
    }

    private void addOrUpdateTodo(Todo todo) {
        adapterBox.put(todo);
    }

    private void setTodo(List<Todo> list) {
        mList = list;
        notifyDataSetChanged();
    }


    private void deleteTodo(long id) {
        Todo todo = getTodoById(id);
        if (todo != null) {
            adapterBox.remove(id);
        }
    }

    void updateTodoView() {
        List<Todo> todos = adapterBox.query().build().find();
        this.setTodo(todos);
    }

    void deleteAll() {
        if (!adapterBox.isEmpty()) {
            new MaterialAlertDialogBuilder(mContext,R.style.MaterialAlertDialog)
                    .setTitle(mContext.getString(R.string.delete_all_todo_dialog))
                    .setPositiveButton(mContext.getString(R.string.yes), (dialog, which) -> {
                        adapterBox.removeAll();
                        Toast.makeText(mContext, "Deleted!", Toast.LENGTH_SHORT).show();
                        updateTodoView();
                    })
                    .setNegativeButton(mContext.getString(R.string.cancel), (dialog, which) -> {
                    })
                    .show();
        } else {
            new MaterialAlertDialogBuilder(mContext,R.style.MaterialAlertDialog)
                    .setTitle(mContext.getString(R.string.no_todo_found))
                    .setPositiveButton(mContext.getString(R.string.add_one), (dialog, which) -> {
                        showDialog(MODE_ADD, 0, false);
                    })

                    .show();
        }
    }

    void deleteMarkedAsDone() {
        if (!adapterBox.isEmpty()) {
            new MaterialAlertDialogBuilder(mContext,R.style.MaterialAlertDialog)
                    .setTitle(mContext.getString(R.string.delete_all__marked_todo_dialog))
                    .setPositiveButton(mContext.getString(R.string.yes), (dialog, which) -> {
                        for (Todo t : adapterBox.getAll()) {
                            if (t.isCompleted) {
                                deleteTodo(t.getId());
                            }
                        }
                        Toast.makeText(mContext, "Removed!", Toast.LENGTH_SHORT).show();
                        updateTodoView();
                    })
                    .setNegativeButton(mContext.getString(R.string.cancel), (dialog, which) -> {
                    })
                    .show();
        } else {
            new MaterialAlertDialogBuilder(mContext,R.style.MaterialAlertDialog)
                    .setTitle(mContext.getString(R.string.no_todo_found))
                    .setPositiveButton(mContext.getString(R.string.add_one), (dialog, which) -> {
                        showDialog(MODE_ADD, 0, false);
                    })
                    .show();
        }
    }

    public void markAsDoneNotification(long id) {
        adapterBox = boxStore.boxFor(Todo.class);
        Todo t = getTodoById(id);
        addOrUpdateTodo(new Todo(t.getId(), t.getTitle(), t.getDescription(), true, true, todoDay, todoMonth, todoYear, todoHour, todoMin));
        Toast.makeText(mContext, "Done!", Toast.LENGTH_SHORT).show();
    }

    public void snoozeNotificationAction(long id) {
        Log.d(TAG, "snoozeNotificationAction: " + id);
        Todo t = getTodoById(id);
        AlarmManager alarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        Intent _myIntent = new Intent(mContext, TodoBroadcast.class);
        PendingIntent _myPendingIntent = PendingIntent.getBroadcast(mContext, 123, _myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        reminder = Calendar.getInstance();
        reminder.setTimeInMillis(System.currentTimeMillis());
        Log.d(TAG, "onClick: " + t.getMonth());
        reminder.set(t.getYear(), t.getMonth() - 1, t.getDay(), t.getHour(), t.getMin(), 0);
        _myIntent.putExtra("MyMessage", t.getTitle());
        _myIntent.putExtra("todoId", t.getId());
        alarmManager.set(AlarmManager.RTC_WAKEUP, reminder.getTimeInMillis(), _myPendingIntent);
        Toast.makeText(mContext, "You'll be snoozed after 10 mins.", Toast.LENGTH_SHORT).show();
    }


//    @Override
//    public int getItemViewType(int position) {
//        if (position == 0) {
//            return 0;
//        } else
//            return 1;
//    }
}


