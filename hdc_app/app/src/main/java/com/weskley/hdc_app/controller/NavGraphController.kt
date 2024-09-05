package com.weskley.hdc_app.controller

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.weskley.hdc_app.screen.AlarmScreen
import com.weskley.hdc_app.screen.FeedbackScreen
import com.weskley.hdc_app.screen.HomeScreen
import com.weskley.hdc_app.screen.PrescriptionScreen
import com.weskley.hdc_app.screen.ProfileScreen
import com.weskley.hdc_app.screen.TreatmentScreen

const val MY_URI = "https://weskley.com"
const val MEDICINE = "medicine"
const val TIME = "time"

@Composable
fun NavGraphController(
    navController: NavHostController,
) {
    NavHost(
        navController = navController,
        startDestination = ScreenController.Home.route
    ) {
        composable(route = ScreenController.Home.route) {
            HomeScreen(navController)
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
        composable(route = ScreenController.Treatment.route) {
            TreatmentScreen()
        }
        composable(
            route = ScreenController.Feedback.route,
            arguments = listOf(
                navArgument(MEDICINE) {
                    type = NavType.StringType
                    defaultValue = ""
                },
                navArgument(TIME) {
                    type = NavType.StringType
                    defaultValue = ""
                }
            ),
            deepLinks = listOf(navDeepLink {
                uriPattern = "$MY_URI/$MEDICINE={$MEDICINE}&$TIME={$TIME}"
            })
        ) {
            val medicine = it.arguments?.getString(MEDICINE)
            val time = it.arguments?.getString(TIME)
            FeedbackScreen(medicine!!, time!!)
        }
    }
}