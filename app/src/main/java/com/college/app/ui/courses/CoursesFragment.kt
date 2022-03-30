package com.college.app.ui.courses

import ViewPagerAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.college.app.R
import com.college.app.databinding.FragmentCoursesBinding
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CoursesFragment() : Fragment() {

    private lateinit var binding: FragmentCoursesBinding

    private val viewModel by viewModels<CoursesViewModel>()

    private val viewPagerAdapter by lazy {
        ViewPagerAdapter(this, listOf(UserCoursesFragment(), AllCoursesFragment()))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentCoursesBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViews()
    }

    private fun setUpViews() {
        binding.viewPager.apply {
            adapter = viewPagerAdapter
        }
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = titleList[position]
        }.attach()
    }

    companion object {
        const val COURSES_FRAGMENT: Int = R.layout.fragment_courses

        val titleList = arrayListOf("Enrolled", "All")
    }

}