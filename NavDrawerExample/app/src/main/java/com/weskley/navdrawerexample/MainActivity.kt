package com.weskley.navdrawerexample

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.weskley.navdrawerexample.ui.theme.Green
import com.weskley.navdrawerexample.ui.theme.NavDrawerExampleTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NavDrawerExampleTheme {
                Surface(color = MaterialTheme.colorScheme.primary) {
                        NavDrawerModel()
                }
            }
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavDrawerModel() {
    val navigationController = rememberNavController()
    val coroutineScope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val context = LocalContext.current.applicationContext

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = true,
        drawerContent = {
            ModalDrawerSheet {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                        .background(Green)
                ) {
                    Text(text = "")
                }
                HorizontalDivider()
                NavigationDrawerItem(
                    label = {
                        Text(text = "Home", color = Green)
                    },
                    selected = false,
                    icon = {
                        Icon(imageVector = Icons.Default.Home, contentDescription = "home",
                            tint = Green)
                    },
                    onClick = {
                        coroutineScope.launch {
                            drawerState.close()
                        }
                        navigationController.navigate(Screens.Home.screen) {
                            popUpTo(0)
                        }
                    })
                NavigationDrawerItem(
                    label = {
                        Text(text = "Profile", color = Green)
                    },
                    selected = false,
                    icon = {
                        Icon(imageVector = Icons.Default.Person, contentDescription = "profile",
                            tint = Green)
                    },
                    onClick = {
                        coroutineScope.launch {
                            drawerState.close()
                        }
                        navigationController.navigate(Screens.Profile.screen) {
                            popUpTo(0)
                        }
                    })
                NavigationDrawerItem(
                    label = {
                        Text(text = "Settings", color = Green)
                    },
                    selected = false,
                    icon = {
                        Icon(imageVector = Icons.Default.Settings, contentDescription = "settings",
                            tint = Green)
                    },
                    onClick = {
                        coroutineScope.launch {
                            drawerState.close()
                        }
                        navigationController.navigate(Screens.Settings.screen) {
                            popUpTo(0)
                        }
                    })
                NavigationDrawerItem(
                    label = {
                        Text(text = "Logout", color = Green)
                    },
                    selected = false,
                    icon = {
                        Icon(imageVector = Icons.AutoMirrored.Default.ExitToApp, contentDescription =
                        "logout",
                            tint = Green)
                    },
                    onClick = {
                        coroutineScope.launch {
                            drawerState.close()
                        }
                        Toast.makeText(context, "Logout", Toast.LENGTH_SHORT).show()
                    })
            }
        }) {
        Scaffold(
            topBar = {
                val coroutineScopes = rememberCoroutineScope()
                TopAppBar(
                    title = {
                        Text(text = "WhatsApp")
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Green,
                        titleContentColor = Color.White,
                        navigationIconContentColor = Color.White
                    ),
                    navigationIcon = {
                        IconButton(onClick = {
                            coroutineScopes.launch {
                                drawerState.open()
                            }
                        }) {
                            Icon(imageVector = Icons.Rounded.Menu, contentDescription = "Menu")
                        }
                    }
                )
            }
        ) {
            NavHost(navController = navigationController, startDestination = Screens.Home
                .screen ) {
                composable(Screens.Home.screen) {
                    HomeScreen()
                }
                composable(Screens.Profile.screen) {
                    ProfileScreen()
                }
                composable(Screens.Settings.screen) {
                    SettingsScreen()
                }
            }
        }
    }
}