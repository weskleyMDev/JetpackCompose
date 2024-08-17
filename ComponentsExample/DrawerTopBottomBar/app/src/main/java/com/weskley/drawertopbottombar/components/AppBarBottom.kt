package com.weskley.drawertopbottombar.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.weskley.drawertopbottombar.controller.Screens
import com.weskley.drawertopbottombar.ui.theme.Green

@Composable
fun AppBarBottom(navigationController: NavController) {
    var showBottomSheet by remember {
        mutableStateOf(false)
    }
    val selected = remember {
        mutableStateOf(Icons.Default.Home)
    }
    BottomAppBar(
        containerColor = Green
    ) {
        IconButton(
            onClick = {
                selected.value = Icons.Default.Home
                navigationController.navigate(Screens.Home.screen) {
                    popUpTo(0)
                }
            },
            modifier = Modifier.weight(1f)
        ) {
            Icon(
                imageVector = Icons.Default.Home, contentDescription = null,
                tint = if (selected.value == Icons.Default.Home) Color.White
                else Color.DarkGray
            )
        }
        IconButton(
            onClick = {
                selected.value = Icons.Default.Search
                navigationController.navigate(Screens.Search.screen) {
                    popUpTo(0)
                }
            },
            modifier = Modifier.weight(1f)
        ) {
            Icon(
                imageVector = Icons.Default.Search, contentDescription = null,
                tint = if (selected.value == Icons.Default.Search) Color.White
                else Color.DarkGray
            )
        }
        Box(modifier = Modifier
            .weight(1f)
            .padding(16.dp),
            contentAlignment = Alignment.Center) {
            FloatingActionButton(onClick = { showBottomSheet = true }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null)
            }
        }
        IconButton(
            onClick = {
                selected.value = Icons.Default.Notifications
                navigationController.navigate(Screens.Notification.screen) {
                    popUpTo(0)
                }
            },
            modifier = Modifier.weight(1f)
        ) {
            Icon(
                imageVector = Icons.Default.Notifications, contentDescription = null,
                tint = if (selected.value == Icons.Default.Notifications) Color.White
                else Color.DarkGray
            )
        }
        IconButton(
            onClick = {
                selected.value = Icons.Default.Person
                navigationController.navigate(Screens.Profile.screen) {
                    popUpTo(0)
                }
            },
            modifier = Modifier.weight(1f)
        ) {
            Icon(
                imageVector = Icons.Default.Person, contentDescription = null,
                tint = if (selected.value == Icons.Default.Person) Color.White
                else Color.DarkGray
            )
        }
    }
}