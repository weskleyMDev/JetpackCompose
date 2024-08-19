package com.weskley.hdc_app.controller

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.weskley.hdc_app.screen.AlarmScreen
import com.weskley.hdc_app.screen.Feedback
import com.weskley.hdc_app.screen.HomeScreen
import com.weskley.hdc_app.screen.PrescriptionScreen
import com.weskley.hdc_app.screen.ProfileScreen

const val MY_URI = "https://weskley.com"

@Composable
fun NavGraphController(
    navController: NavHostController,
) {
    NavHost(
        navController = navController,
        startDestination = ScreenController.Home.route
    ) {
        composable(route = ScreenController.Home.route) {
            HomeScreen()
        }
        composable(route = ScreenController.Alarm.route) {
            AlarmScreen()
        }
        composable(route = ScreenController.Prescription.route) {
            PrescriptionScreen()
        }
        composable(route = ScreenController.Profile.route) {
            ProfileScreen()
        }
        composable(
            route = ScreenController.Feedback.route,
            arguments = listOf(
                navArgument(ARG) {
                    type = NavType.StringType
                    defaultValue = ""
                }
            ),
            deepLinks = listOf(navDeepLink { uriPattern = "$MY_URI/$ARG={$ARG}" })) {
                it.arguments?.getString(ARG)?.let { argument ->
                    Feedback(argument)
                }
            }
    }
}