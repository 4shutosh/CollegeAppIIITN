package com.college.app.ui.courses

import ViewPagerAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.college.app.R
import com.college.app.databinding.FragmentCoursesBinding
import com.college.app.ui.courses.detail.CourseDetailFragment.Companion.COURSE_CODE_KEY
import com.college.app.utils.extensions.showToast
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CoursesFragment : Fragment() {

    private lateinit var binding: FragmentCoursesBinding

    private val viewModel by viewModels<CoursesViewModel>()

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
        setUpObservers()
    }

    private fun setUpViews() {
        activity?.onBackPressedDispatcher?.addCallback(onBackPress)
        binding.viewPager.adapter =
            ViewPagerAdapter(this, listOf(UserCoursesFragment(), AllCoursesFragment()))

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = titleList[position]
        }.attach()

        activity?.let {
            binding.toolbar.setTitle(R.string.courses)
            (it as AppCompatActivity).apply {
                setSupportActionBar(binding.toolbar)
                supportActionBar?.setDisplayHomeAsUpEnabled(true)
            }
        }
    }

    private fun setUpObservers() {
        viewModel.coursesFragmentCommand.observe(viewLifecycleOwner) {
            processCommand(it)
        }
    }

    private fun processCommand(it: CoursesViewModel.CoursesFragmentCommand) {
        when (it) {
            is CoursesViewModel.CoursesFragmentCommand.ShowToast -> showToast(it.message)
            is CoursesViewModel.CoursesFragmentCommand.OpenCourseDetailFragment -> {
                findNavController().navigate(R.id.action_coursesFragment_to_courseDetailFragment,
                    Bundle().apply {
                        putString(COURSE_CODE_KEY, it.courseViewState.code)
                    })
            }
        }
    }

    private val onBackPress = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            activity?.finish()
        }
    }

    companion object {
        const val COURSES_FRAGMENT: Int = R.layout.fragment_courses
        val titleList = arrayListOf("Enrolled", "All")
    }

}