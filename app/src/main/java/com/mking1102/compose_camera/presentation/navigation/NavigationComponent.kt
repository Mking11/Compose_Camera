package com.mking1102.compose_camera.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.core.net.toUri
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.mking1102.compose_camera.camera.domain.utils.Screens
import com.mking1102.compose_camera.presentation.CameraBaseScreen
import com.mking1102.compose_camera.presentation.CameraView

@Composable
fun CameraNavigation(navController: NavHostController) {
    NavHost(
        startDestination = Screens.CameraBaseScreen + "?uri={uri}",
        navController = navController
    ) {
        composable(
            route = Screens.CameraBaseScreen + "?uri={uri}",
            arguments = listOf(navArgument(name = "uri") {
                type = NavType.StringType
                nullable = true
            })
        ) {
            val uri: String? = it.arguments?.getString("uri")
            CameraBaseScreen(uri = uri?.toUri(), navController)
        }
        composable(route = Screens.SelectionScreen) {
            CameraView(navHostController = navController)
        }
    }
}