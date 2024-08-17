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
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
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
            val postNotificationPermissionState = rememberPermissionState(
                permission = android.Manifest.permission.POST_NOTIFICATIONS
            )
            LaunchedEffect(key1 = true) {
                if (!postNotificationPermissionState.status.isGranted) {
                    postNotificationPermissionState.launchPermissionRequest()
                }
            }
            Hdc_appTheme {
                AppNavDrawer()
            }
        }
    }
}