package com.weskley.hdc_app.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.weskley.hdc_app.R
import com.weskley.hdc_app.controller.ScreenController
import com.weskley.hdc_app.ui.theme.DarkBlue
import com.weskley.hdc_app.ui.theme.White

@Composable
fun AppBarBottom(
    navController: NavController
) {
    BottomAppBar(
        containerColor = DarkBlue,
        contentColor = White
    ) {
        IconButton(
            onClick = {
                navController.navigate(route = ScreenController.Home.route) {
                    popUpTo(0)
                }
            },
            modifier = Modifier.weight(1f)
        ) {
            Icon(imageVector = Icons.Default.Home, contentDescription = null)
        }
        IconButton(
            onClick = {
                navController.navigate(route = ScreenController.Alarm.route) {
                    popUpTo(0)
                }
            },
            modifier = Modifier.weight(1f)
        ) {
            Icon(imageVector = Icons.Default.Notifications, contentDescription = null)
        }
        IconButton(onClick = {
            navController.navigate(route = ScreenController.Prescription.route) {
                popUpTo(0)
            }
        }, modifier = Modifier.weight(1f)) {
            Icon(
                painter = painterResource(id = R.drawable.outline_clinical_notes_24),
                contentDescription = null
            )
        }
        IconButton(onClick = {
            navController.navigate(route = ScreenController.Profile.route) {
                popUpTo(0)
            }
        }, modifier = Modifier.weight(1f)) {
            Icon(imageVector = Icons.Default.AccountCircle, contentDescription = null)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewAppBarBottom() {
    AppBarBottom(rememberNavController())
}