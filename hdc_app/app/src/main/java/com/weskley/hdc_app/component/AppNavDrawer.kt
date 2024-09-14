package com.weskley.hdc_app.component

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.weskley.hdc_app.R
import com.weskley.hdc_app.constant.Constants
import com.weskley.hdc_app.controller.ScreenController
import com.weskley.hdc_app.ui.theme.DarkBlue
import com.weskley.hdc_app.viewmodel.AlarmViewModel
import kotlinx.coroutines.launch

@Composable
fun AppNavDrawer(
    mainViewModel: AlarmViewModel = hiltViewModel(),
) {
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
                        .height(200.dp)
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.logo_circ_pretog),
                        contentDescription = "logo"
                    )
                }
                HorizontalDivider()
                NavigationDrawerItem(
                    label = { Text(text = "PERFIL") },
                    selected = false,
                    icon = {
                        Icon(
                            painterResource(id = R.drawable.outline_account_circle_24),
                            contentDescription = null
                        )
                    },
                    onClick = {
                        coroutineScope.launch {
                            drawerState.close()
                            mainViewModel.selected.value = Constants.ICON_PROFILE
                        }
                        navController.navigate(ScreenController.Profile.route) {
                            popUpTo(0)
                        }
                    }
                )
                NavigationDrawerItem(
                    label = { Text(text = "ALARMES") },
                    selected = false,
                    icon = {
                        Icon(
                            painterResource(id = R.drawable.outline_alarm_24),
                            contentDescription = null
                        )
                    },
                    onClick = {
                        coroutineScope.launch {
                            drawerState.close()
                            mainViewModel.selected.value = Constants.ICON_ALARM
                        }
                        navController.navigate(ScreenController.Alarm.route) {
                            popUpTo(0)
                        }
                    }
                )
                NavigationDrawerItem(
                    label = { Text(text = "RECEITAS") },
                    selected = false,
                    icon = {
                        Icon(
                            painterResource(id = R.drawable.outline_clinical_notes_24),
                            contentDescription = null
                        )
                    },
                    onClick = {
                        coroutineScope.launch {
                            drawerState.close()
                            mainViewModel.selected.value = Constants.ICON_PRESCRIPTION
                        }
                        navController.navigate(ScreenController.Prescription.route) {
                            popUpTo(0)
                        }
                    }
                )
                Spacer(modifier = Modifier.weight(1f))
                NavigationDrawerItem(
                    label = { Text(text = "SAIR") },
                    selected = false,
                    icon = {
                        Icon(
                            painterResource(id = R.drawable.outline_logout_24),
                            contentDescription = null
                        )
                    },
                    onClick = {
                        coroutineScope.launch { drawerState.close() }
                        Toast.makeText(context, "Logout Successful", Toast.LENGTH_SHORT)
                            .show()
                    }
                )
            }
        }
    ) {
        AppBarTop(drawerState, navController)
    }
}