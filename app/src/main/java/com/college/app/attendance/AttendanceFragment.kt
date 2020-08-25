package com.college.app.attendance

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.college.app.databinding.AttendanceFragmentBinding
import com.college.app.fragment.HolderActivity

class AttendanceFragment : Fragment() {
    private var attendanceFragmentBinding: AttendanceFragmentBinding? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        attendanceFragmentBinding = AttendanceFragmentBinding.inflate(inflater)
        val h = activity as HolderActivity?
        if (h != null) {
            h.setSupportActionBar(attendanceFragmentBinding!!.toolbar)
            h.supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            h.supportActionBar!!.title = "Attendance Calculator"
            setHasOptionsMenu(true)
        }
        return attendanceFragmentBinding!!.root
    }
}