package com.weskley.basicnotification.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.weskley.basicnotification.screens.DetailsScreen
import com.weskley.basicnotification.screens.NotificationScreen

const val DETAIL_ARGUMENT_KEY = "name"
const val DETAIL_ARGUMENT_URI = "https://weskley.com"

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
                    type = NavType.StringType
                    defaultValue = ""
                }
            ),
            deepLinks = listOf(navDeepLink { uriPattern = "$DETAIL_ARGUMENT_URI/$DETAIL_ARGUMENT_KEY={$DETAIL_ARGUMENT_KEY}" })
        ) {
            val args = it.arguments
            args?.getString(DETAIL_ARGUMENT_KEY)?.let {name ->
                DetailsScreen(argument = name)
            }
        }
    }
}