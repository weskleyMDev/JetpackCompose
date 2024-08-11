package com.weskley.hdc_app.component

import androidx.compose.foundation.layout.size
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
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
        IconButton(
            onClick = {
//                isSelect = Constants.ICON_HOME
                mainViewModel.selected.intValue = Constants.ICON_HOME
                navController.navigate(route = ScreenController.Home.route) {
                    popUpTo(0)
                }
            },
            modifier = Modifier.weight(1f)
        ) {
            Icon(
                painterResource(id = R.drawable.outline_home_health_24),
                contentDescription = null,
                tint = if (mainViewModel.selected.intValue == Constants.ICON_HOME) Blue else White,
                modifier = if (mainViewModel.selected.intValue == Constants.ICON_HOME) Modifier
                    .size(36.dp) else Modifier.size(26.dp)
            )
        }
        IconButton(
            onClick = {
                mainViewModel.selected.intValue = Constants.ICON_ALARM
                navController.navigate(route = ScreenController.Alarm.route) {
                    popUpTo(0)
                }
            },
            modifier = Modifier.weight(1f)
        ) {
            Icon(
                painterResource(id = R.drawable.outline_alarm_24),
                contentDescription = null,
                tint = if (mainViewModel.selected.intValue == Constants.ICON_ALARM) Blue else White,
                modifier = if (mainViewModel.selected.intValue == Constants.ICON_ALARM) Modifier
                    .size(36.dp) else Modifier.size(26.dp)
            )
        }
        IconButton(onClick = {
            mainViewModel.selected.intValue = Constants.ICON_PRESCRIPTION
            navController.navigate(route = ScreenController.Prescription.route) {
                popUpTo(0)
            }
        }, modifier = Modifier.weight(1f)) {
            Icon(
                painterResource(id = R.drawable.outline_clinical_notes_24),
                contentDescription = null,
                tint = if (mainViewModel.selected.intValue == Constants.ICON_PRESCRIPTION) Blue else White,
                modifier = if (mainViewModel.selected.intValue == Constants.ICON_PRESCRIPTION) Modifier
                    .size(36.dp) else Modifier.size(26.dp)
            )
        }
        IconButton(onClick = {
            mainViewModel.selected.intValue = Constants.ICON_PROFILE
            navController.navigate(route = ScreenController.Profile.route) {
                popUpTo(0)
            }
        }, modifier = Modifier.weight(1f)) {
            Icon(
                painterResource(id = R.drawable.outline_account_circle_24),
                contentDescription = null,
                tint = if (mainViewModel.selected.intValue == Constants.ICON_PROFILE) Blue else White,
                modifier = if (mainViewModel.selected.intValue == Constants.ICON_PROFILE) Modifier
                    .size(36.dp) else Modifier.size(26.dp)
            )
        }
    }
}