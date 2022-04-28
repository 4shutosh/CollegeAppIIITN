package com.college.app.ui.courses

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.college.app.databinding.FragmentAllCoursesBinding
import com.college.app.ui.courses.adapter.CourseListAdapter
import com.college.app.utils.extensions.gone
import com.college.app.utils.extensions.visible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AllCoursesFragment : Fragment(), CourseListAdapter.CourseListItemClickListener {

    private val parentViewModel: CoursesViewModel by viewModels(
        ownerProducer = { requireParentFragment() }
    )

    private lateinit var binding: FragmentAllCoursesBinding

    private val listAdapter by lazy {
        CourseListAdapter(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentAllCoursesBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        setupObservers()
    }

    private fun setupViews() {
        binding.list.adapter = listAdapter
    }

    private fun setupObservers() {
        parentViewModel.allCoursesFlow.observe(viewLifecycleOwner) {
            if (it.isEmpty()) {
                binding.noData.visible()
                binding.list.gone()
            } else {
                binding.noData.gone()
                listAdapter.submitList(it)
                binding.list.visible()
            }
        }
    }

    override fun courseItemClicked(item: CourseListAdapter.CourseViewState) {
        parentViewModel.actionCourseItemClicked(item)
    }

    override fun courseEnrollStateClicked(
        enroll: Boolean,
        item: CourseListAdapter.CourseViewState,
    ) {
        parentViewModel.actionCourseEnrollmentChange(enroll, item)
    }
}