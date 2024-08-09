package com.weskley.alarmmanagerexample

sealed class Screens(val route: String) {
    data object Alarm: Screens("alarm")
    data object Feedback: Screens("feedback")

}