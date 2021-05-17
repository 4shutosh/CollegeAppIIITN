package com.college.app.Notification

import android.content.ContentValues
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.college.app.R
import com.college.app.databinding.FragmentNotificationsBinding
import com.college.app.utils.SwipeToDeleteCallbackNotifications
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.snackbar.Snackbar
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import java.util.*
import kotlin.collections.ArrayList

class NotificationsFragment() : Fragment() {
    private lateinit var bsNotifications: ConstraintLayout
    private lateinit var bottomSheetBehaviorNotifications: BottomSheetBehavior<ConstraintLayout>
    private lateinit var notificationAdapter: NotificationAdapter
    private val notificationList: ArrayList<Notification> = arrayListOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentNotificationsBinding =
            FragmentNotificationsBinding.inflate(inflater, container, false)
        EventBus.getDefault().register(this)
        setHasOptionsMenu(true)
        attachRecyclerView(activity)
        notificationLayout = fragmentNotificationsBinding!!.notificationsLayout
        notificationAdapter.updateNotificationView()
        enableSwipeToDeleteAndUndo()
        bsNotifications = activity!!.findViewById(R.id.bottom_sheet_notifications)
        bottomSheetBehaviorNotifications = BottomSheetBehavior.from(bsNotifications)
        fragmentNotificationsBinding!!.cardViewNotiInfo.setOnClickListener {
            if (bottomSheetBehaviorNotifications.state != BottomSheetBehavior.STATE_EXPANDED) {
                bottomSheetBehaviorNotifications.setState(BottomSheetBehavior.STATE_EXPANDED)
            } else {
                bottomSheetBehaviorNotifications.setState(BottomSheetBehavior.STATE_COLLAPSED)
            }
        }
        return fragmentNotificationsBinding!!.root

        //
    }

    @Subscribe
    fun onEvent(notification: Notification) {
//        Notification n = FirebaseDataReceiver.getNotification();
//        if (n != null) {
        activity!!.runOnUiThread {
            Log.d(ContentValues.TAG, "run: success")
            notificationAdapter.addNotification(
                notification.title,
                notification.description
            )
            notificationAdapter.notifyDataSetChanged()
        }
    }

    private fun enableSwipeToDeleteAndUndo() {
        val swipeToDeleteCallback: SwipeToDeleteCallbackNotifications =
            object : SwipeToDeleteCallbackNotifications(
                context
            ) {
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, i: Int) {
                    val position = viewHolder.adapterPosition
                    val item = notificationAdapter!!.data[position]
                    notificationAdapter!!.removeItem(position)
                    val snackbar = Snackbar
                        .make(
                            (notificationLayout)!!,
                            "Item was removed from the list.",
                            Snackbar.LENGTH_LONG
                        )
                    snackbar.setAction("UNDO") {
                        notificationAdapter!!.restoreItem(item, position)
                        recyclerView!!.scrollToPosition(position)
                    }
                    snackbar.duration = 1000
                    snackbar.setActionTextColor(Color.GREEN)
                    snackbar.show()
                }
            }
        val itemTouchhelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchhelper.attachToRecyclerView(recyclerView)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.notifications_fragment_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.notification_remove_all -> notificationAdapter!!.removeAll()
        }
        return super.onOptionsItemSelected(item)
    }
    private fun attachRecyclerView(context: Context?) {
        ////////////////////////Recycler View
        recyclerView = fragmentNotificationsBinding!!.notificationRecyclerView
        notificationAdapter = NotificationAdapter(context, notificationList)
        linearLayoutManager = LinearLayoutManager(context)
        recyclerView!!.layoutManager =
            linearLayoutManager
        linearLayoutManager!!.orientation = RecyclerView.VERTICAL
        recyclerView!!.adapter =
            notificationAdapter
        Log.d(ContentValues.TAG, "attachRecyclerView: view attached")
        ////////////////////////
    }

    companion object {
        private var fragmentNotificationsBinding: FragmentNotificationsBinding? = null
        private var linearLayoutManager: LinearLayoutManager? = null
        private var recyclerView: RecyclerView? = null
        private var notificationLayout: ConstraintLayout? = null

    }
}