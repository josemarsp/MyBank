package com.example.mybank.selfievalidation.presentation.ui

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.result.ActivityResultLauncher
import androidx.core.content.ContextCompat

class SelfiePermissionController(
    private val context: Context,
    private val permissionLauncher: ActivityResultLauncher<String>
) {
    private var permissionRequested = false

    fun hasCameraPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun ensureCameraPermission() {
        if (hasCameraPermission()) return
        if (permissionRequested) return
        permissionRequested = true
        permissionLauncher.launch(Manifest.permission.CAMERA)
    }
}

