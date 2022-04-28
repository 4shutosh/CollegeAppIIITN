package com.college.app.ui.courses.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import com.college.app.R
import com.college.app.databinding.FragmentCourseDetailBinding
import com.college.app.utils.extensions.executeAfter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CourseDetailFragment : Fragment() {

    private lateinit var binding: FragmentCourseDetailBinding

    private val viewModel by viewModels<CourseDetailViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentCourseDetailBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViews()
        setUpObservers()
    }

    private fun setUpViews() {
        requireActivity().onBackPressedDispatcher.addCallback(onBackPress)

        val courseCode = arguments?.getString(COURSE_CODE_KEY) ?: ""
        if (courseCode.isEmpty()) {
            requireActivity().supportFragmentManager.popBackStack()
        } else {
            viewModel.actionCourseViewStateFromCode(courseCode)
        }
        activity?.let {
            (it as AppCompatActivity).apply {
                setSupportActionBar(binding.toolbar)
                supportActionBar?.setDisplayHomeAsUpEnabled(true)
            }
        }
    }

    private fun setUpObservers() {
        viewModel.courseDetailFragment.observe(viewLifecycleOwner) {
            binding.toolbar.title = it.name
            binding.scrollLayout.executeAfter {
                viewState = it
            }
        }
    }

    private val onBackPress = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            activity?.let {
                val host =
                    it.supportFragmentManager.findFragmentById(R.id.activity_holder_fragment) as NavHostFragment
                host.navController.popBackStack()
            }
        }
    }

    companion object {

        const val FRAGMENT_COURSE_DETAIL_TAG = "FRAGMENT_COURSE_DETAIL_TAG"
        const val COURSE_CODE_KEY = "COURSE_CODE_KEY"
    }
}