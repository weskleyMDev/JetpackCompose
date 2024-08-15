package com.weskley.hdc_app.controller

const val ARG = "arg"

sealed class ScreenController(val route: String) {
    data object Home: ScreenController("home")
    data object Alarm: ScreenController("alarm")
    data object Prescription: ScreenController("prescription")
    data object Profile: ScreenController("profile")
    data object Feedback: ScreenController("feedback?arg={$ARG}")
        fun passArgument(name: String = "WESKLEY"): String {
            return this.route.replace(
                oldValue = "{$ARG}",
                newValue = name
            )
        }
}