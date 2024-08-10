package com.weskley.hdc_app.controller

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.weskley.hdc_app.screen.AlarmScreen
import com.weskley.hdc_app.screen.HomeScreen
import com.weskley.hdc_app.screen.PrescriptionScreen
import com.weskley.hdc_app.screen.ProfileScreen

@Composable
fun NavGraphController(navController: NavHostController) {
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
    }
}