package com.college.app.ui.books

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.college.app.R
import com.college.app.databinding.FragmentOfflineLibraryBinding
import com.college.app.ui.books.LibraryViewModel.Command.ShowBarcodeScannerFragment
import com.college.app.ui.books.list.LibraryListAdapter
import com.college.app.ui.books.scanner.LibraryBarcodeScannerFragment
import com.college.app.ui.books.scanner.LibraryBarcodeScannerFragment.Companion.BARCODE_SCANNER_FRAGMENT_KEY
import com.college.app.utils.PermissionRequester
import com.college.app.utils.extensions.gone
import com.college.app.utils.extensions.showDialogFragmentIfNotPresent
import com.college.app.utils.extensions.showToast
import com.college.app.utils.extensions.visible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LibraryFragment : Fragment(), LibraryListAdapter.LibraryListOnItemTouchListener {

    private lateinit var binding: FragmentOfflineLibraryBinding

    private val libraryListAdapter by lazy { LibraryListAdapter(this) }

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
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentOfflineLibraryBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupViews()
        setUpObservers()
    }

    private fun setupViews() {
        binding.fragmentLibraryList.apply {
            adapter = libraryListAdapter
        }
        binding.swipeLayout.setOnRefreshListener {
            viewModel.getIssuedBooks()
        }
    }

    private fun setUpObservers() {
        viewModel.command.observe(viewLifecycleOwner) {
            processCommand(it)
        }

        viewModel.libraryListViewState.observe(viewLifecycleOwner) {
            binding.swipeLayout.isRefreshing = it.isLoading

            if (it.viewList.isNotEmpty()) {
                libraryListAdapter.submitList(it.viewList)
                libraryListAdapter.notifyDataSetChanged()
            }
        }
    }

    private fun processCommand(it: LibraryViewModel.Command) {
        when (it) {
            is ShowBarcodeScannerFragment -> openBarcodeScannerFragment()
            is LibraryViewModel.Command.ShowToastMessage -> requireActivity().showToast(it.message)
            LibraryViewModel.Command.CloseBarcodeScannerFragment -> {
                childFragmentManager.popBackStackImmediate()
            }
        }
    }

    private fun openBarcodeScannerFragment() {
        // open the qr code scanner
        requester.runWithPermission {
            childFragmentManager.showDialogFragmentIfNotPresent<LibraryBarcodeScannerFragment>(
                LibraryBarcodeScannerFragment::class.java,
                BARCODE_SCANNER_FRAGMENT_KEY,
                null)
        }
    }

    companion object {
        const val OFFLINE_LIBRARY_FRAGMENT_ID = R.layout.fragment_offline_library
    }

    override fun issueBookClicked() {
        viewModel.actionIssueBookClicked()
    }

    override fun issuedBookItemClicked() {

    }
}