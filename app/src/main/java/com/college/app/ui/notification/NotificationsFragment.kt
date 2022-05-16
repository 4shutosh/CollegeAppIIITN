package com.college.app.ui.notification

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.college.app.databinding.FragmentNotificationsBinding
import com.college.app.ui.notification.list.NotificationsAdapter
import com.college.app.utils.extensions.gone
import com.college.app.utils.extensions.showDialogFragmentIfNotPresent
import com.college.app.utils.openURL
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotificationsFragment : Fragment(), NotificationsAdapter.ItemClickListener {

    private lateinit var binding: FragmentNotificationsBinding

    private val listAdapter by lazy { NotificationsAdapter(this) }
    private val viewModel by viewModels<NotificationsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentNotificationsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        setUpObservers()
    }

    private fun setupViews() {
        binding.infoCard.setOnClickListener {
            childFragmentManager.showDialogFragmentIfNotPresent<NotificationsInfoFragment>(
                NotificationsInfoFragment::class.java,
                NotificationsInfoFragment.NOTIFICATIONS_INFO_KEY,
                null
            )
        }
        binding.notificationRecyclerView.apply {
            adapter = listAdapter
        }

        binding.swipeRefresh.setOnClickListener {
            viewModel.swipeActionFetchAgainFromNetwork()
        }
    }

    private fun setUpObservers() {
        viewModel.notificationsList.observe(viewLifecycleOwner) {
            listAdapter.submitList(it)
        }

        viewModel.announcementsFlow.observe(viewLifecycleOwner) {
            binding.swipeRefresh.isRefreshing = false
            if (it.isEmpty()) {
                binding.notificationRecyclerView.gone()
            } else {
                listAdapter.submitList(it)
            }
        }
    }

    override fun onItemLinkClick(url: String) {
        openURL(requireContext(), url)
    }
}