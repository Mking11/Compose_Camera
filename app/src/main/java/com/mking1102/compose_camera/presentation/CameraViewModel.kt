package com.mking1102.compose_camera.presentation

import android.content.ContentValues
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.core.net.toFile
import androidx.lifecycle.ViewModel
import com.mking1102.compose_camera.camera.domain.models.CameraEvents
import com.mking1102.compose_camera.camera.domain.models.CameraStates
import com.mking1102.compose_camera.camera.domain.models.CurrentCameraView
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import java.util.concurrent.Executor
import javax.inject.Inject

@HiltViewModel
class CameraViewModel
@Inject
constructor() : ViewModel() {

    private val _state = MutableStateFlow(CameraStates())
    val state: StateFlow<CameraStates> = _state


    fun handleEvents(cameraEvents: CameraEvents) {
        when (cameraEvents) {

            CameraEvents.OpenCamera -> {
                val picture = state.value.picture
                try {
                    val file = picture?.toFile()
                    file?.delete()
                } catch (e: Exception) {
                    Log.e("exception", "handleEvents: ", e)
                }

                _state.update {
                    it.copy(currentScreen = CurrentCameraView.Camera, picture = null)
                }
            }
            is CameraEvents.SavePicture -> {
                _state.update {
                    it.copy(cameraEvents.uri, currentScreen = CurrentCameraView.Preview)
                }
            }
            is CameraEvents.HandleError -> {
                Log.e("ComposeCamera", "handleEvents: ", cameraEvents.exception)
            }
            is CameraEvents.TakePicture -> {
                takePhoto(
                    cameraEvents.outputDirectory,
                    cameraEvents.imageCapture,
                    cameraEvents.executor
                )
            }
        }
    }

    fun getContentValues(photoName: String): ContentValues {
        return ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, photoName)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/ComposeImages")
            }
        }
    }


    private fun takePhoto(
        outputDirectory: ImageCapture.OutputFileOptions,
        imageCapture: ImageCapture?,
        executor: Executor,
    ) {

        imageCapture!!.takePicture(
            outputDirectory,
            executor,
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exception: ImageCaptureException) {
                    handleEvents(
                        cameraEvents = CameraEvents.HandleError(
                            exception,
                            "Take Picture Exception"
                        )
                    )
                }

                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    val savedUri = outputFileResults.savedUri
                    if (savedUri != null) {
                        handleEvents(cameraEvents = CameraEvents.SavePicture(savedUri))
                    }
                }
            })
    }


}