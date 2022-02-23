package com.college.app.ui.books.scanner

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.camera.core.AspectRatio.RATIO_4_3
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.fragment.app.viewModels
import com.college.app.R
import com.college.app.databinding.BottomsheetBarcodeScannerBinding
import com.college.app.ui.books.LibraryViewModel
import com.college.app.utils.extensions.bindWithLifecycleOwner
import com.college.base.logger.CollegeLogger
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.Executors
import javax.inject.Inject

@AndroidEntryPoint
class BarcodeScannerFragment : BottomSheetDialogFragment() {

    @Inject
    lateinit var logger: CollegeLogger

    private lateinit var globalCameraProcessProvider: ProcessCameraProvider
    private lateinit var globalAnalysisUseCase: ImageAnalysis

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
        setUpCameraAndAnalyze()
    }

    private fun setUpCameraAndAnalyze() {
        val cameraSelector =
            CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_BACK).build()
        parentViewModel.processCameraProvider.observe(viewLifecycleOwner) { provider ->
            if (provider == null) {
                return@observe
            } else {
                globalCameraProcessProvider = provider
                val previewUseCase = Preview.Builder().apply {
                    setTargetAspectRatio(RATIO_4_3)
                    if (binding.barcodeScannerCameraView.display != null)
                        setTargetRotation(binding.barcodeScannerCameraView.display.rotation)
                }.build()

                previewUseCase.setSurfaceProvider(binding.barcodeScannerCameraView.surfaceProvider)

                provider.bindToLifecycle(
                    viewLifecycleOwner,
                    cameraSelector,
                    previewUseCase
                )

                val barcodeScanner = BarcodeScanning.getClient()

                val analysisUseCase = ImageAnalysis.Builder()
                    .setTargetAspectRatio(RATIO_4_3)
                    .build()

                globalAnalysisUseCase = analysisUseCase

                if (binding.barcodeScannerCameraView.display != null)
                    analysisUseCase.targetRotation =
                        binding.barcodeScannerCameraView.display.rotation


                val cameraExecutor = Executors.newSingleThreadExecutor()

                analysisUseCase.setAnalyzer(cameraExecutor) {
                    processImageProxy(barcodeScanner, it, previewUseCase, provider)
                }

                provider.bindToLifecycle(viewLifecycleOwner,
                    cameraSelector,
                    analysisUseCase
                )
            }
        }
    }

    @SuppressLint("UnsafeOptInUsageError")
    private fun processImageProxy(
        barcodeScanner: BarcodeScanner,
        imageProxy: ImageProxy,
        previewUseCase: Preview,
        provider: ProcessCameraProvider,
    ) {
        imageProxy.image?.let {
            val inputImage = InputImage.fromMediaImage(it, imageProxy.imageInfo.rotationDegrees)
            barcodeScanner.process(inputImage)
                .addOnSuccessListener { barcodes ->
                    if (barcodes.isNotEmpty())
                        barcodes[0].let { barcode ->
                            logger.d("found barcode ${barcode.rawValue}")
                            provider.unbind(previewUseCase)
                            provider.unbind(globalAnalysisUseCase)
                            parentViewModel.getBookByLibraryNumber(barcode.rawValue?.toLong() ?: 0L)
                        }
                }.addOnFailureListener { exception ->
                    logger.e(exception)
                }.addOnCompleteListener {
                    imageProxy.close()
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