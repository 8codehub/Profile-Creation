package com.dev.profilecreation.presentation.camera

import android.content.Context
import android.content.pm.PackageManager
import android.Manifest
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import java.io.File
import javax.inject.Inject

class CameraViewModel @Inject constructor(): ViewModel() {

    val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)

    fun allPermissionsGranted(context: Context): Boolean {
        return REQUIRED_PERMISSIONS.all {
            ContextCompat.checkSelfPermission(
                context, it
            ) == PackageManager.PERMISSION_GRANTED
        }
    }

    fun createImageFile(context: Context): File {
        val fileName = "your_file_name"
        val outputDirectory = context.getExternalFilesDir("images")!!
        return File(outputDirectory, "$fileName.jpg")
    }
}
