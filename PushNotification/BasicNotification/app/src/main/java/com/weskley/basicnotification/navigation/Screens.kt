package com.weskley.basicnotification.navigation

sealed class Screens(val route: String) {

    data object Notification: Screens("notification")
    data object Details: Screens("details?name={$NAME}&lname={$LNAME}") {
        fun passArgument(name: String = "WESKLEY", lname: String = "MOREIRA"): String {
            return this.route.replace(
                oldValue = "{$NAME}",
                newValue = name
            ).replace(
                oldValue = "{$LNAME}",
                newValue = lname
            )
        }
    }
}