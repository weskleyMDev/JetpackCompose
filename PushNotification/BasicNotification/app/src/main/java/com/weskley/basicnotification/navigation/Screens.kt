package com.weskley.basicnotification.navigation

const val DETAIL_ARGUMENT_KEY = "id"

sealed class Screens(val route: String) {
    data object Details: Screens("details/${DETAIL_ARGUMENT_KEY}") {
        fun optionalArgs(msg: String = ""): String {
            return this.route
                .replace("{$DETAIL_ARGUMENT_KEY}", msg)
        }
    }
    data object Notification: Screens("notification")
}