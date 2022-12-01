package com.mking1102.compose_camera.presentation

import android.Manifest
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.mking1102.compose_camera.camera.domain.utils.Screens

@Composable
fun CameraBaseScreen(
    uri: Uri?,
    navController: NavController
) {

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            navController.navigate(Screens.SelectionScreen)
        }
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        uri?.let {
            AsyncImage(
                model = uri,
                contentDescription = "Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp)
                    .height(250.dp),
                contentScale = ContentScale.FillBounds
            )

        }


        Button(onClick = {
            permissionLauncher.launch(Manifest.permission.CAMERA)
        }, modifier = Modifier.fillMaxWidth(0.8f)) {
            Text(text = "Open Camera")
        }


    }
}


@Preview
@Composable
fun PreviewBaseScreen() {
    CameraBaseScreen(null, navController = NavController(LocalContext.current))
}
