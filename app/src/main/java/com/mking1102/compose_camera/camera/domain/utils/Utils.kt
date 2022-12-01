package com.mking1102.compose_camera.camera.domain.utils

import androidx.activity.ComponentActivity
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import java.text.SimpleDateFormat
import java.util.*


val newFormatter: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
fun getCurrentDate(): String = newFormatter.format(Calendar.getInstance().time)

val LocalActivity: ProvidableCompositionLocal<ComponentActivity> = compositionLocalOf {
    error("No Activity")
}