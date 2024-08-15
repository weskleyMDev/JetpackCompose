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

const val NAME = "name"
const val LNAME = "lname"
const val URI = "https://weskley.com"

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
                navArgument(NAME) {
                    type = NavType.StringType
                    defaultValue = ""
                },
                navArgument(LNAME) {
                    type = NavType.StringType
                    defaultValue = ""
                }
            ),
            deepLinks = listOf(navDeepLink { uriPattern = "$URI/$NAME={$NAME}&$LNAME={$LNAME}" })
        ) {
            val name = it.arguments?.getString(NAME)
            val lname = it.arguments?.getString(LNAME)
            DetailsScreen(name = name!!, lastName = lname!!)
        }
    }
}