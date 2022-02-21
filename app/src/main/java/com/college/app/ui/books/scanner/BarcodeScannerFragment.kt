package com.college.app.ui.books.scanner

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.camera.core.AspectRatio.RATIO_4_3
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.fragment.app.viewModels
import com.college.app.R
import com.college.app.databinding.BottomsheetBarcodeScannerBinding
import com.college.app.ui.books.LibraryViewModel
import com.college.app.utils.extensions.bindWithLifecycleOwner
import com.college.base.logger.CollegeLogger
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class BarcodeScannerFragment : BottomSheetDialogFragment() {


    @Inject
    lateinit var logger: CollegeLogger

    private lateinit var globalCameraProcessProvider : ProcessCameraProvider

    private val parentViewModel: LibraryViewModel by viewModels(
        ownerProducer = { requireParentFragment() }
    )

    private lateinit var binding: BottomsheetBarcodeScannerBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = bindWithLifecycleOwner(inflater, R.layout.bottomsheet_barcode_scanner, container)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setUpViews()
    }

    private fun setUpViews() {
        val cameraSelector =
            CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_BACK).build()
        parentViewModel.processCameraProvider.observe(viewLifecycleOwner) { provider ->
            if (provider == null) {
                return@observe
            } else {
                globalCameraProcessProvider = provider
                val previewUseCase = Preview.Builder()
                    .setTargetAspectRatio(RATIO_4_3)
                    .build()

                if (binding.barcodeScannerCameraView.display != null) previewUseCase.targetRotation =
                    binding.barcodeScannerCameraView.display.rotation

                previewUseCase.setSurfaceProvider(binding.barcodeScannerCameraView.surfaceProvider)

                provider.bindToLifecycle(viewLifecycleOwner,
                    cameraSelector,
                    previewUseCase
                )
            }
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        globalCameraProcessProvider.unbindAll()
    }


    companion object {
        const val BARCODE_SCANNER_FRAGMENT_KEY = "BARCODE_SCANNER_FRAGMENT_KEY"
    }


}