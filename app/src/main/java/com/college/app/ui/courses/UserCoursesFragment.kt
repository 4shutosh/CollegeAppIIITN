package com.college.app.ui.courses

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.college.app.R
import com.college.app.databinding.FragmentUserCoursesBinding
import com.college.app.ui.courses.adapter.CourseListAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserCoursesFragment : Fragment(),
    CourseListAdapter.CourseListItemClickListener {

    private lateinit var binding: FragmentUserCoursesBinding

    private val parentViewModel: CoursesViewModel by viewModels(
        ownerProducer = { requireParentFragment() }
    )

    private val listAdapter by lazy {
        CourseListAdapter(this)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentUserCoursesBinding.inflate(inflater)
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
        parentViewModel.userCoursesFlow.observe(viewLifecycleOwner) {
            listAdapter.submitList(it)
        }
    }

    override fun courseItemClicked(item: CourseListAdapter.CourseViewState) {
    }

    override fun courseEnrollStateClicked(
        enroll: Boolean,
        item: CourseListAdapter.CourseViewState,
    ) {
    }

}