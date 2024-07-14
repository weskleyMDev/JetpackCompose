package com.weskley.navigation

const val DETAIL_ARGUMENT_KEY = "id"
const val DETAIL_ARGUMENT_NAME = "name"

sealed class Screen(val route: String) {
    data object Home : Screen(route = "home_screen")
    data object Detail : Screen(
        route =
        //"detail_screen/{$DETAIL_ARGUMENT_KEY}/{$DETAIL_ARGUMENT_NAME}"
        //"detail_screen?id={id}"
        "detail_screen?id={$DETAIL_ARGUMENT_KEY}&name={$DETAIL_ARGUMENT_NAME}"
    ) {
        /*fun passId(id: Int): String {
            return this.route.replace(
                oldValue = "{$DETAIL_ARGUMENT_KEY}",
                newValue = id.toString()
            )
        }

        fun passIdName(id: Int, name: String): String {
            return this.route.replace(
                oldValue =
                "{$DETAIL_ARGUMENT_KEY}/{$DETAIL_ARGUMENT_NAME}", newValue = "$id/$name"
            )
        }*/

        /*fun passOptionalArg(id: Int = 0): String {
            return "detail_screen?id=$id"
        }*/
        fun passOptionalArgs(id: Int = 0, name: String = "Marie"): String {
            //return "detail_screen?id=$id&name=$name"
            return this.route
                .replace(oldValue = "{$DETAIL_ARGUMENT_KEY}", newValue = id.toString())
                .replace(oldValue = "{$DETAIL_ARGUMENT_NAME}", newValue = name)
        }
    }
}