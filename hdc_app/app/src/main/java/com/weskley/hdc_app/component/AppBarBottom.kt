package com.weskley.hdc_app.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.weskley.hdc_app.R
import com.weskley.hdc_app.constant.Constants
import com.weskley.hdc_app.controller.ScreenController
import com.weskley.hdc_app.ui.theme.Blue
import com.weskley.hdc_app.ui.theme.DarkBlue
import com.weskley.hdc_app.ui.theme.White
import com.weskley.hdc_app.viewmodel.AlarmViewModel

@Composable
fun AppBarBottom(
    navController: NavController,
    mainViewModel: AlarmViewModel = hiltViewModel()
) {
    BottomAppBar(
        containerColor = DarkBlue,
        contentColor = White
    ) {
        TextButton(
            onClick = {
                mainViewModel.selected.intValue = Constants.ICON_HOME
                navController.navigate(route = ScreenController.Home.route) {
                    popUpTo(0)
                }
            },
            modifier = Modifier.weight(1f)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    painterResource(id = R.drawable.outline_home_health_24),
                    contentDescription = null,
                    tint = if (mainViewModel.selected.intValue == Constants.ICON_HOME) Blue else White,
                    modifier = if (mainViewModel.selected.intValue == Constants.ICON_HOME) Modifier
                        .size(30.dp) else Modifier.size(24.dp)
                )
                Text(
                    text = "INICIO",
                    color = if (mainViewModel.selected.intValue == Constants.ICON_HOME) Blue else White,
                    style = MaterialTheme.typography.labelSmall
                )
            }
        }
        TextButton(
            onClick = {
                mainViewModel.selected.intValue = Constants.ICON_ALARM
                navController.navigate(route = ScreenController.Alarm.route) {
                    popUpTo(0)
                }
            },
            modifier = Modifier.weight(1f),
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    painterResource(id = R.drawable.outline_alarm_24),
                    contentDescription = null,
                    tint = if (mainViewModel.selected.intValue == Constants.ICON_ALARM) Blue else White,
                    modifier = if (mainViewModel.selected.intValue == Constants.ICON_ALARM) Modifier
                        .size(30.dp) else Modifier.size(24.dp)
                )
                Text(
                    text = "ALARMES",
                    color = if (mainViewModel.selected.intValue == Constants.ICON_ALARM) Blue else White,
                    style = MaterialTheme.typography.labelSmall
                )
            }
        }
        TextButton(onClick = {
            mainViewModel.selected.intValue = Constants.ICON_PRESCRIPTION
            navController.navigate(route = ScreenController.Prescription.route) {
                popUpTo(0)
            }
        }, modifier = Modifier.weight(1f)) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    painterResource(id = R.drawable.outline_clinical_notes_24),
                    contentDescription = null,
                    tint = if (mainViewModel.selected.intValue == Constants.ICON_PRESCRIPTION) Blue else White,
                    modifier = if (mainViewModel.selected.intValue == Constants.ICON_PRESCRIPTION) Modifier
                        .size(30.dp) else Modifier.size(24.dp)
                )
                Text(
                    text = "RECEITAS",
                    color = if (mainViewModel.selected.intValue == Constants.ICON_PRESCRIPTION) Blue else White,
                    style = MaterialTheme.typography.labelSmall
                )
            }
        }
        TextButton(onClick = {
            mainViewModel.selected.intValue = Constants.ICON_PROFILE
            navController.navigate(route = ScreenController.Profile.route) {
                popUpTo(0)
            }
        }, modifier = Modifier.weight(1f)) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                Icon(
                    painterResource(id = R.drawable.outline_account_circle_24),
                    contentDescription = null,
                    tint = if (mainViewModel.selected.intValue == Constants.ICON_PROFILE) Blue else White,
                    modifier = if (mainViewModel.selected.intValue == Constants.ICON_PROFILE) Modifier
                        .size(30.dp) else Modifier.size(24.dp)
                )
                Text(
                    text = "PERFIL",
                    color = if (mainViewModel.selected.intValue == Constants.ICON_PROFILE) Blue else White,
                    style = MaterialTheme.typography.labelSmall
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AppBarBottomPreview() {
    AppBarBottom(navController = NavController(LocalContext.current))
}