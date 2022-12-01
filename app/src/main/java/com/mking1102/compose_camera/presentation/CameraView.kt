package com.mking1102.compose_camera.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.mking1102.compose_camera.presentation.component.CameraLauncher
import com.mking1102.compose_camera.camera.domain.models.CurrentCameraView
import com.mking1102.compose_camera.presentation.component.PicturePreview

@Composable
fun CameraView(
    navHostController: NavHostController,
    viewModel: CameraViewModel = hiltViewModel(),
) {
    val state = viewModel.state.collectAsState()
    when (state.value.currentScreen) {
        CurrentCameraView.Camera -> CameraLauncher()
        CurrentCameraView.Preview -> PicturePreview(navHostController = navHostController)
    }

}