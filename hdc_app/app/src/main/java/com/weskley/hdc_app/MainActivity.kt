package com.weskley.hdc_app

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.runtime.LaunchedEffect
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.weskley.hdc_app.component.AppNavDrawer
import com.weskley.hdc_app.ui.theme.Hdc_appTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().setKeepOnScreenCondition { false }
        enableEdgeToEdge()
        setContent {
            val multiplePermissionsState = rememberMultiplePermissionsState(
                permissions = listOf(
                    android.Manifest.permission.POST_NOTIFICATIONS,
                    android.Manifest.permission.CAMERA,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                )
            )
            LaunchedEffect(Unit) {
                if (!multiplePermissionsState.allPermissionsGranted) {
                    multiplePermissionsState.launchMultiplePermissionRequest()
                }
            }
            Hdc_appTheme {
                AppNavDrawer()
            }
        }
    }
}