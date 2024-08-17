package com.weskley.navigationcomponent

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun Home(navController: NavController) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Home Screen", fontSize = 30.sp)
        Button(onClick = {
            navController.popBackStack/*navigate*/(Routes.Main.route
                /*"MainScreen"*/, inclusive = false)
        }) {
            Text(text = "Go to Main")
        }
    }
}