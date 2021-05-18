package com.college.app.todo

import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.Constraints
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.college.app.R
import com.college.app.databinding.FragmentTodoBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.bottomsheet_todo.*
import java.util.*

class TodoFragment : Fragment() {
    private var fragmentTodoBinding: FragmentTodoBinding? = null
    private val listTodo: List<Todo> = ArrayList()
    private var savedRecyclerState: Parcelable? = null
    private var adapter: TodoAdapter? = null
    private var recyclerView: RecyclerView? = null
    private var bottomSheet: ConstraintLayout? = null
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<*>
    private var linearLayoutManager: LinearLayoutManager? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentTodoBinding = FragmentTodoBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        ///////////////////////////////////////////////// Recycler View
        val recyclerView = fragmentTodoBinding!!.todoRecyclerView
        adapter = activity?.let { TodoAdapter(it, listTodo) }
        adapter!!.updateTodoView()
        retainInstance = true
        linearLayoutManager = LinearLayoutManager(activity)
//        recyclerItemTouchBack()
        fragmentTodoBinding!!.todoRecyclerView.layoutManager = linearLayoutManager
        linearLayoutManager!!.orientation = RecyclerView.VERTICAL
        fragmentTodoBinding!!.todoRecyclerView.adapter = adapter
        fragmentTodoBinding!!.todoFab.setOnClickListener { adapter!!.showDialog(TodoAdapter.MODE_ADD, 0, false) }
        Log.d(Constraints.TAG, "onCreateView: started")
//        bottomSheet = activity!!.findViewById(R.id.bottom_sheet_todo)
        bottomSheetBehavior = BottomSheetBehavior.from(activity!!.bottom_sheet_todo)

        fragmentTodoBinding!!.cardViewTodoInfo.setOnClickListener {
            if (bottomSheetBehavior.state != BottomSheetBehavior.STATE_EXPANDED) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED)
            } else {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED)
            }
        }
        return fragmentTodoBinding!!.root
    }

//    private fun recyclerItemTouchBack() {
//        val _ithCallback: ItemTouchHelper.Callback = object : ItemTouchHelper.Callback() {
//            var dragFrom = -1
//            var dragTo = -1
//
//            //and in your imlpementaion of
//            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
//                // get the viewHolder's and target's positions in your adapter data, swap them
//                if (viewHolder.itemViewType != target.itemViewType) {
//                    return false
//                }
//                val fromPosition = viewHolder.adapterPosition
//                val toPosition = target.adapterPosition
//                if (dragFrom == -1) {
//                    dragFrom = fromPosition
//                }
//                dragTo = toPosition
//                if (dragFrom != -1 && dragTo != -1 && dragFrom != dragTo) {
//                    reallyMoved(dragFrom, dragTo)
//                    dragTo = -1
//                    dragFrom = dragTo
//                }
//
//                // and notify the adapter that its dataset has changed
//                adapter!!.notifyItemMoved(viewHolder.adapterPosition, target.adapterPosition)
//                //nestedScrollView.requestDisallowInterceptTouchEvent(false);
//                //recyclerView.setNestedScrollingEnabled(false);
//                return true
//            }
//
//            private fun reallyMoved(dragFrom: Int, dragTo: Int) {
//                if (dragFrom == 0 || dragTo == adapter?.itemCount ?: 0) {
//                    return
//                }
//                Collections.swap(listTodo, dragFrom - 1, dragTo - 1)
//            }
//
//            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
//
//
//            }
//
//            //defines the enabled move directions in each state (idle, swiping, dragging).
//            override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
//                if (viewHolder.itemViewType == 0) {
//                    return makeMovementFlags(0, 0)
//                }
//                val dragflags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
//                return makeMovementFlags(dragflags, 0)
//            }
//
//        }
//        val itemTouchHelper = ItemTouchHelper(_ithCallback)
//        itemTouchHelper.attachToRecyclerView(fragmentTodoBinding!!.todoRecyclerView)
//
//    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentTodoBinding = null
    }

    override fun onResume() {
        super.onResume()
        if (savedRecyclerState != null) {
            linearLayoutManager!!.onRestoreInstanceState(savedRecyclerState)
        }
        Log.d(Constraints.TAG, "onResume: Started")
        adapter!!.notifyDataSetChanged()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        savedRecyclerState = linearLayoutManager!!.onSaveInstanceState()
        outState.putParcelable(LIST_STATE, savedRecyclerState)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        if (savedInstanceState != null) {
            savedRecyclerState = savedInstanceState.getParcelable(LIST_STATE)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.todo_fragement_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.deleteAllTodo -> adapter!!.deleteAll()
            R.id.deleteMarkedAsDone -> adapter!!.deleteMarkedAsDone()
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        private const val LIST_STATE = "list State"
    }
}