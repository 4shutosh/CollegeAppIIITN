package com.college.app.ui.main.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.college.app.databinding.FragmentHomeBinding
import com.college.app.ui.main.home.list.HomeFeatureListAdapter
import com.college.app.ui.main.home.list.HomeFeatureListAdapter.HomeFeatureListClickListener
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
    }


    override fun onHomeFeatureListItemClick(itemId: Long) {

    }

}