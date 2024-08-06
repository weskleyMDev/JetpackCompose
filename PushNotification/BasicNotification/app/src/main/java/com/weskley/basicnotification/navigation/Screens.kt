package com.weskley.basicnotification.navigation

sealed class Screens(val route: String) {

    data object Notification: Screens("notification")
    data object Details: Screens("details?name={$DETAIL_ARGUMENT_KEY}") {
        fun passArgument(msg: String = "JAVA"): String {
            return this.route.replace(
                oldValue = "{$DETAIL_ARGUMENT_KEY}",
                newValue = msg
            )
        }
    }
}