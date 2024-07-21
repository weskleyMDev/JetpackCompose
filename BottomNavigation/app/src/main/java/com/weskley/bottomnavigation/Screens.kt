package com.weskley.bottomnavigation

sealed class Screens(val screen: String) {
    data object Home: Screens("home")
    data object Search: Screens("search")
    data object Notification: Screens("notification")
    data object Profile: Screens("profile")
}