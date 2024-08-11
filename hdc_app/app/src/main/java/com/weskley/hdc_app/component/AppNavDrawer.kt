package com.weskley.hdc_app.component

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.navigation.compose.rememberNavController
import com.weskley.hdc_app.R
import com.weskley.hdc_app.controller.ScreenController
import com.weskley.hdc_app.ui.theme.DarkBlue
import kotlinx.coroutines.launch

@Composable
fun AppNavDrawer() {
    val coroutineScope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val context = LocalContext.current.applicationContext
    val navController = rememberNavController()
    
    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = true,
        drawerContent = {
            ModalDrawerSheet {
                Box(
                    modifier = Modifier
                        .background(DarkBlue)
                        .fillMaxWidth()
                        .height(IntrinsicSize.Min),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Text(
                        text = "Hora de Cuidar",
                        style = MaterialTheme.typography.titleLarge
                    )
                }
                HorizontalDivider()
                NavigationDrawerItem(
                    label = { Text(text = "Profile") },
                    selected = false,
                    icon = { Icon(painterResource(id = R.drawable.outline_account_circle_24), contentDescription = null) },
                    onClick = {
                        coroutineScope.launch { drawerState.close() }
                        navController.navigate(ScreenController.Profile.route) {
                            popUpTo(0)
                        }
                    }
                )
                NavigationDrawerItem(
                    label = { Text(text = "Alarm") },
                    selected = false,
                    icon = { Icon(painterResource(id = R.drawable.outline_alarm_24), contentDescription = null) },
                    onClick = {
                        coroutineScope.launch { drawerState.close() }
                        navController.navigate(ScreenController.Alarm.route) {
                            popUpTo(0)
                        }
                    }
                )
                NavigationDrawerItem(
                    label = { Text(text = "Prescription") },
                    selected = false,
                    icon = { Icon(painterResource(id = R.drawable.outline_clinical_notes_24), contentDescription = null) },
                    onClick = {
                        coroutineScope.launch { drawerState.close() }
                        navController.navigate(ScreenController.Prescription.route) {
                            popUpTo(0)
                        }
                    }
                )
                NavigationDrawerItem(
                    label = { Text(text = "Logout") },
                    selected = false,
                    icon = { Icon(painterResource(id = R.drawable.outline_logout_24), contentDescription = null) },
                    onClick = {
                        coroutineScope.launch { drawerState.close() }
                        Toast.makeText(context, "Logout Successful", Toast.LENGTH_SHORT).show()
                    }
                )
            }
        }
    ) {
        AppBarTop(drawerState, navController)
    }
}