package com.weskley.navigationcomponent

sealed class Routes(val route: String) {
    data object Main: Routes("main")
    data object Home: Routes("home")
}