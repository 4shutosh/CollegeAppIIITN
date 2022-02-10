package com.college.app.ui.main.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.college.app.databinding.FragmentHomeBinding
import com.college.app.ui.main.holder.HolderActivity
import com.college.app.ui.main.home.HomeViewModel.Command.*
import com.college.app.ui.main.home.list.HomeFeatureListAdapter
import com.college.app.ui.main.home.list.HomeFeatureListAdapter.HomeFeatureListClickListener
import com.college.app.utils.openURL
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment(), HomeFeatureListClickListener {

    private lateinit var binding: FragmentHomeBinding

    private val viewModel: HomeViewModel by viewModels()

    private val homeFeatureListAdapter by lazy { HomeFeatureListAdapter(this) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupViews()
        initLiveData()
    }

    private fun setupViews() {
        binding.homeFragmentFeatureList.adapter = homeFeatureListAdapter
    }

    private fun initLiveData() {
        viewModel.homeFeatureListLiveData.observe(viewLifecycleOwner) {
            homeFeatureListAdapter.submitList(it)
        }

        viewModel.command.observe(
            viewLifecycleOwner
        ) {
            processCommand(it)
        }
    }

    private fun processCommand(it: HomeViewModel.Command) {
        when (it) {
            is NavigateToFeatureScreen -> navigateToFeatureScreen(it.fragmentId)
            is OpenWebViewWithUrl -> openURL(requireContext(), it.url)
        }
    }

    private fun navigateToFeatureScreen(fragmentId: Long) {
        val intent = HolderActivity.intent(requireContext(), fragmentId)
        requireContext().startActivity(intent)
    }


    override fun onHomeFeatureListItemClick(itemId: Long) {
        viewModel.actionFeatureItemClick(itemId)
    }

    companion object {
        const val HOME_FRAGMENT_ID = 98798L

        const val HOME_WEBSITE_FRAGMENT_ID: Long = 6731
        const val HOME_WEBSITE_URL = "https://iiitn.ac.in/"
    }

}