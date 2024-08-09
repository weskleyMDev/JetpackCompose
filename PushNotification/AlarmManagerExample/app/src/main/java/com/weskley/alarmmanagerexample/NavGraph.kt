package com.weskley.alarmmanagerexample

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink

const val MY_URI = "https://weskley.com"

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screens.Alarm.route
    ) {
        composable(Screens.Alarm.route) {
            MyAlarm()
        }
        composable(
            route = Screens.Feedback.route,
            deepLinks = listOf(navDeepLink { uriPattern = MY_URI })
        ) {
            Feedback()
        }
    }
}