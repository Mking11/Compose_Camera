package com.mking1102.compose_camera.camera.domain.models

import android.net.Uri

data class CameraStates(
    val picture: Uri? = null,
    val currentScreen: CurrentCameraView = CurrentCameraView.Camera,
    val exception: Exception? = null,
    val errorMessage: String? = null
)