package com.mking1102.compose_camera.camera.domain.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import androidx.navigation.compose.rememberNavController
import com.mking1102.compose_camera.CameraNavigation
import com.mking1102.compose_camera.LocalActivity
import com.mking1102.compose_camera.ui.theme.ComposeCameraTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val permissions = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.CAMERA,
    )




    private val newRequest =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
            if (it.values.contains(false)) {
                requestPermissions()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!hasPermissions(this, *permissions)) {
            requestPermissions()
        }
        setContent {
            ComposeCameraTheme {
                CompositionLocalProvider(LocalActivity provides this) {
                    val navController = rememberNavController()
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colors.background
                    ) {
                        CameraNavigation(navController = navController)
                    }
                }
                // A surface container using the 'background' color from the theme

            }
        }
    }

    private fun hasPermissions(context: Context, vararg permissions: String): Boolean =
        permissions.all {
            ActivityCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }


    private fun requestPermissions() {
        newRequest.launch(permissions)
    }
}
