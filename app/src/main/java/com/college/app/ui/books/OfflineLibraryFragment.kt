package com.college.app.ui.books

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.college.app.databinding.FragmentOfflineLibraryBinding
import com.college.app.ui.books.scanner.BarcodeScannerFragment
import com.college.app.ui.books.scanner.BarcodeScannerFragment.Companion.BARCODE_SCANNER_FRAGMENT_KEY
import com.college.app.utils.PermissionRequester
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OfflineLibraryFragment : Fragment(), LibraryListAdapter.LibraryListOnItemTouchListener {

    private lateinit var binding: FragmentOfflineLibraryBinding

    private val listAdapter by lazy { LibraryListAdapter(this) }

    private val viewModel: LibraryViewModel by viewModels()

    private val requester = PermissionRequester(
            this, Manifest.permission.CAMERA, onDenied = {
                Toast.makeText(
                    requireContext(),
                    "Camera Permission Is required to SCAN Barcode!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOfflineLibraryBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupViews()
    }

    private fun setupViews() {
        binding.fragmentLibraryList.apply {
            adapter = listAdapter
        }
    }

    companion object {
        const val OFFLINE_LIBRARY_FRAGMENT_ID: Long = 228L
    }

    override fun issueBookClicked() {
        // open the qr code scanner
        requester.runWithPermission {
            BarcodeScannerFragment().show(childFragmentManager, BARCODE_SCANNER_FRAGMENT_KEY)
        }
    }
}