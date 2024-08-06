package com.weskley.basicnotification.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.weskley.basicnotification.screens.DetailsScreen
import com.weskley.basicnotification.screens.NotificationScreen

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screens.Notification.route
        ) {
        composable(route = Screens.Notification.route) {
            NotificationScreen(navController = navController)
        }
        composable(
            route = Screens.Details.route,
            arguments = listOf(
                navArgument(DETAIL_ARGUMENT_KEY) {
                    type = NavType.IntType
                    defaultValue = 0
                }
            )
        ) {
            val args = it.arguments
            DetailsScreen(argument = args?.getString(DETAIL_ARGUMENT_KEY).toString())
        }
    }
}