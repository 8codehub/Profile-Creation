package com.dev.profilecreation.presentation.camera

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import androidx.navigation.fragment.findNavController
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.dev.profilecreation.databinding.FragmentCameraBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import com.dev.profilecreation.presentation.core.BaseBindingFragment


@AndroidEntryPoint
class CameraFragment : BaseBindingFragment<FragmentCameraBinding>() {

    private lateinit var cameraExecutor: ExecutorService
    private lateinit var imageCapture: ImageCapture

    private val viewModel: CameraViewModel by viewModels()

    private val requestPermissionsLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            if (permissions.all { it.value }) {
                startCamera()
            } else {
                Log.e(TAG, "Permissions not granted.")
            }
        }

    override fun provideBinding(inflater: LayoutInflater, container: ViewGroup?) = FragmentCameraBinding.inflate(inflater, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cameraExecutor = Executors.newSingleThreadExecutor()

        if (viewModel.allPermissionsGranted(requireContext())) {
            startCamera()
        } else {
            requestPermissionsLauncher.launch(viewModel.REQUIRED_PERMISSIONS)
        }

    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())

        cameraProviderFuture.addListener(Runnable {
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(binding.viewFinder.surfaceProvider)
            }

            imageCapture = ImageCapture.Builder().build()

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageCapture
                )

            } catch (exc: Exception) {
                Log.e(TAG, "Use case binding failed", exc)
            }

            binding.captureButton.setOnClickListener {
                val photoFile = viewModel.createImageFile(requireContext())

                val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

                showLoading(true)

                imageCapture.takePicture(outputOptions, cameraExecutor, object : ImageCapture.OnImageSavedCallback {
                    override fun onError(exception: ImageCaptureException) {
                        showLoading(false)
                    }

                    override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                        showLoading(false)
                        val savedUri = Uri.fromFile(photoFile)
                        val navController = findNavController();
                        lifecycleScope.launch {
                            navController.previousBackStackEntry?.savedStateHandle?.set(
                                "image_path", savedUri.path
                            )
                            navController.popBackStack()
                        }

                    }
                })
            }

        }, ContextCompat.getMainExecutor(requireContext()))
    }

    private fun showLoading(isLoading: Boolean) {
        lifecycleScope.launch {
            if (isLoading) {
                binding.captureButton.visibility = View.GONE
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.captureButton.visibility = View.VISIBLE
                binding.progressBar.visibility = View.GONE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        cameraExecutor.shutdown()
    }

    companion object {
        private const val TAG = "CameraFragment"
    }
}
