package com.weskley.hdc_app.controller

sealed class ScreenController(val route: String) {
    data object Home: ScreenController("home")
    data object Alarm: ScreenController("alarm")
    data object Prescription: ScreenController("prescription")
    data object Profile: ScreenController("profile")
    data object Treatment: ScreenController("treatment")
    data object Feedback: ScreenController("feedback?medicine={$MEDICINE}&time={$TIME}&id={$MEDICINE_ID}&amount={$MEDICINE_AMOUNT}")
        fun passArgument(name: String = "WESKLEY"): String {
            return this.route.replace(
                oldValue = "{$MEDICINE}",
                newValue = name
            )
        }
}