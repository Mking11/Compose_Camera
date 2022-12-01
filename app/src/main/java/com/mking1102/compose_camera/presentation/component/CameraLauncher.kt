package com.mking1102.compose_camera.presentation.component

import android.provider.MediaStore
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.Lens
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.mking1102.compose_camera.camera.domain.models.CameraEvents
import com.mking1102.compose_camera.camera.domain.utils.getCurrentDate
import com.mking1102.compose_camera.presentation.CameraViewModel
import java.util.concurrent.Executors

var imageCapture: ImageCapture? = null

@Composable
fun CameraLauncher(
    viewModel: CameraViewModel = hiltViewModel()
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val lensFacing = CameraSelector.LENS_FACING_BACK
    val context = LocalContext.current
    val executor = Executors.newSingleThreadExecutor()


    val outputFileOptions =
        ImageCapture.OutputFileOptions.Builder(
            context.contentResolver,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            viewModel.getContentValues(getCurrentDate())
        ).build()

    val previewView = remember { PreviewView(context) }
    LaunchedEffect(lensFacing) {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
        cameraProviderFuture.addListener(
            {
                val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
                val preview = Preview.Builder().build()
                imageCapture = ImageCapture.Builder().build()
                preview.setSurfaceProvider(previewView.surfaceProvider)
                val cameraSelector = CameraSelector.Builder()
                    .requireLensFacing(lensFacing)
                    .build()
                try {
                    cameraProvider.unbindAll()

                    cameraProvider.bindToLifecycle(
                        lifecycleOwner,
                        cameraSelector,
                        preview,
                        imageCapture
                    )
                } catch (exc: Exception) {
                    viewModel.handleEvents(CameraEvents.HandleError(exc, exc.message))
                }
            },
            ContextCompat.getMainExecutor(context)
        )
    }

    Box(contentAlignment = Alignment.BottomCenter, modifier = Modifier.fillMaxSize()) {

        AndroidView(
            { previewView.rootView }, modifier = Modifier
                .fillMaxSize()
                .align(Alignment.Center)
        )

        IconButton(
            modifier = Modifier.padding(bottom = 20.dp),
            onClick = {
                viewModel.handleEvents(
                    CameraEvents.TakePicture(
                        outputDirectory = outputFileOptions,
                        imageCapture = imageCapture,
                        executor = executor,
                    )
                )
            },
            content = {
                Icon(
                    imageVector = Icons.Sharp.Lens,
                    contentDescription = "Take picture",
                    tint = Color.White,
                    modifier = Modifier
                        .size(70.dp)
                        .padding(1.dp)
                        .border(1.dp, Color.White, CircleShape)
                )
            }
        )
    }
}