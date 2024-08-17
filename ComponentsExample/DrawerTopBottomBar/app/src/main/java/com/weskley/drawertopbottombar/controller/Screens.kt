package com.weskley.drawertopbottombar.controller

sealed class Screens(val screen: String) {
    data object Home: Screens("home")
    data object Search: Screens("search")
    data object Notification: Screens("notification")
    data object Setting: Screens("setting")
    data object Profile: Screens("profile")
    data object Post: Screens("post")
}