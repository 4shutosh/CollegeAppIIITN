package com.college.app.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.college.app.databinding.ELibraryFragmentBinding

class ELibraryFragment : Fragment() {
    private var eLibraryFragmentBinding: ELibraryFragmentBinding? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        eLibraryFragmentBinding = ELibraryFragmentBinding.inflate(inflater)
        val h = activity as HolderActivity?
        if (h != null) {
            h.setSupportActionBar(eLibraryFragmentBinding!!.toolbar)
            h.supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            h.supportActionBar!!.title = "ELibrary"
            setHasOptionsMenu(true)
        }
        return eLibraryFragmentBinding!!.root
    }
}