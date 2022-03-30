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
import com.college.app.models.local.CollegeBarcodeData
import com.college.app.ui.books.LibraryViewModel
import com.college.app.utils.extensions.*
import com.college.app.utils.isJSONValid
import com.college.base.logger.CollegeLogger
import com.college.base.result.DataUiResult
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import com.squareup.moshi.Moshi
import dagger.hilt.android.AndroidEntryPoint
import java.lang.Exception
import java.util.concurrent.Executors
import javax.inject.Inject

@AndroidEntryPoint
class LibraryBarcodeScannerFragment : BottomSheetDialogFragment() {

    @Inject
    lateinit var logger: CollegeLogger

    private lateinit var globalCameraProcessProvider: ProcessCameraProvider

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
        setUpListeners()
    }

    private fun setUpViews() {
        isCancelable = true
        setUpCameraAndAnalyze()
    }

    private fun setUpListeners() {
        parentViewModel.barcodeScannerViewState.observe(viewLifecycleOwner) {
            binding.barcodeScannerConfirmButton.isEnabled = it.issueBookButtonEnabled
            when (it.scannedCollegeBook) {
                is DataUiResult.Success,
                -> {
                    binding.barcodeScannerProgressBar.gone()
                    binding.barcodeScannerBookDetails.text = it.scannedCollegeBook.data.bookName

                    val availableText =
                        if (it.scannedCollegeBook.data.isAvailableToIssue) "Available till ${
                            getDateInFuture(it.scannedCollegeBook.data.maxDaysAllowed)
                        }"
                        else {
                            "Not Available to Issue! \n Issued by ${it.scannedCollegeBook.data.ownerData?.email}"
                        }

                    if (it.scannedCollegeBook.data.isAvailableToIssue) {
                        binding.barcodeScannerConfirmButton.visible()
                        binding.barcodeScannerConfirmButton.setOnClickListener { view ->
                            parentViewModel.actionIssueBookButtonClicked(it.scannedCollegeBook.data.libraryBookNumber)
                        }
                    } else binding.barcodeScannerConfirmButton.gone()

                    binding.barcodeScannerBookAvailability.text = availableText


                }
                is DataUiResult.Error -> {
                    showToast("No Book Found ${it.scannedCollegeBook.exception}")
                }
                is DataUiResult.Loading -> {
                    if (it.scannedCollegeBook.loading) binding.barcodeScannerProgressBar.visible()
                    else binding.barcodeScannerProgressBar.gone()
                }
            }

        }
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

                if (binding.barcodeScannerCameraView.display != null)
                    analysisUseCase.targetRotation =
                        binding.barcodeScannerCameraView.display.rotation


                val cameraExecutor = Executors.newSingleThreadExecutor()

                analysisUseCase.setAnalyzer(cameraExecutor) {
                    processImageProxy(barcodeScanner, it, previewUseCase, provider, analysisUseCase)
                }

                provider.bindToLifecycle(
                    viewLifecycleOwner,
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
        analysisUseCase: ImageAnalysis,
    ) {
        imageProxy.image?.let {
            val inputImage = InputImage.fromMediaImage(it, imageProxy.imageInfo.rotationDegrees)
            barcodeScanner.process(inputImage)
                .addOnSuccessListener { barcodes ->
                    if (barcodes.isNotEmpty())
                        barcodes[0].let { barcode ->
                            logger.d("found barcode ${barcode.rawValue}")

                            try {
                                val moshi: Moshi = Moshi.Builder().build()
                                val jsonAdapter =
                                    moshi.adapter(CollegeBarcodeData::class.java).lenient()
                                val dataString = barcode.rawValue.orEmpty()
                                if (dataString.isNotEmpty() && isJSONValid(dataString)) {
                                    val barcodeData = jsonAdapter.fromJson(dataString)
                                    if (barcodeData != null && barcodeData.identifier == BARCODE_IDENTIFIER) {
                                        provider.unbind(previewUseCase)
                                        provider.unbind(analysisUseCase)
                                        parentViewModel.getBookByLibraryNumber(barcodeData.data.toLong())
                                    }
                                }
                            } catch (exception: Exception) {
                                logger.d("Something wrong happened ${exception.message}")
                            }

                        }
                }.addOnFailureListener { exception ->
                    logger.e(exception)
                }.addOnCompleteListener {
                    imageProxy.close()
                }
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        parentViewModel.barcodeScannerDialogClosed()
        globalCameraProcessProvider.unbindAll()
        super.onDismiss(dialog)
    }

    companion object {
        const val BARCODE_SCANNER_FRAGMENT_KEY = "BARCODE_SCANNER_FRAGMENT_KEY"
        const val BARCODE_IDENTIFIER = "69694242"
    }

}