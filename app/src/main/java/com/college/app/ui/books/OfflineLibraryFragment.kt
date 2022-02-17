package com.college.app.ui.books

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.college.app.databinding.FragmentOflineLibraryBinding

class OfflineLibraryFragment : Fragment() {

    private lateinit var binding: FragmentOflineLibraryBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOflineLibraryBinding.inflate(layoutInflater)
        return binding.root
    }




}