package com.weskley.drawertopbottombar.controller

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.weskley.drawertopbottombar.screens.HomeScreen
import com.weskley.drawertopbottombar.screens.NotificationScreen
import com.weskley.drawertopbottombar.screens.PostScreen
import com.weskley.drawertopbottombar.screens.ProfileScreen
import com.weskley.drawertopbottombar.screens.SearchScreen
import com.weskley.drawertopbottombar.screens.SettingsScreen

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screens.Home.screen
    ) {
        composable(Screens.Home.screen) { HomeScreen() }
        composable(Screens.Search.screen) { SearchScreen() }
        composable(Screens.Notification.screen) { NotificationScreen() }
        composable(Screens.Setting.screen) { SettingsScreen() }
        composable(Screens.Profile.screen) { ProfileScreen() }
        composable(Screens.Post.screen) { PostScreen() }
    }
}