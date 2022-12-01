package com.mking1102.compose_camera.camera.domain.models

import android.net.Uri
import androidx.camera.core.ImageCapture
import java.util.concurrent.Executor

sealed class CameraEvents {
    object OpenCamera : CameraEvents()
    data class TakePicture(
        val outputDirectory: ImageCapture.OutputFileOptions,
        val imageCapture: ImageCapture?,
        val executor: Executor,
    ) : CameraEvents()

    data class HandleError(
        val exception: java.lang.Exception,
        val message: String?
    ) : CameraEvents()

    data class SavePicture(val uri: Uri) : CameraEvents()

}
