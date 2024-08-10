package com.weskley.hdc_app.controller

sealed class ScreenController(val route: String) {
    data object Home: ScreenController("home")
    data object Alarm: ScreenController("alarm")
    data object Prescription: ScreenController("prescription")
    data object Profile: ScreenController("profile")
}