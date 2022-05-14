package com.college.app.ui.notification

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.college.app.databinding.BottomsheetNotificationsBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotificationsInfoFragment : BottomSheetDialogFragment() {

    private lateinit var binding: BottomsheetNotificationsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = BottomsheetNotificationsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isCancelable = true
    }

    companion object {
        const val NOTIFICATIONS_INFO_KEY = "NOTIFICATIONS_INFO_KEY"
    }


}