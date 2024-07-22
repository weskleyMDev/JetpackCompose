package com.weskley.drawertopbottombar

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.weskley.drawertopbottombar.components.HomeScreen
import com.weskley.drawertopbottombar.components.NotificationScreen
import com.weskley.drawertopbottombar.components.PostScreen
import com.weskley.drawertopbottombar.components.ProfileScreen
import com.weskley.drawertopbottombar.components.SearchScreen
import com.weskley.drawertopbottombar.components.SettingsScreen
import com.weskley.drawertopbottombar.screens.Screens
import com.weskley.drawertopbottombar.ui.theme.DrawerTopBottomBarTheme
import com.weskley.drawertopbottombar.ui.theme.Green
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DrawerTopBottomBarTheme {
                Surface(
                    color = MaterialTheme.colorScheme.primary
                ) {
                    AppScreen()
                }
            }
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppScreen() {
    val navigationController = rememberNavController()
    val coroutineScope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val context = LocalContext.current.applicationContext
    val selected = remember {
        mutableStateOf(Icons.Default.Home)
    }
    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember {
        mutableStateOf(false)
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = true,
        drawerContent = {
            ModalDrawerSheet {
                Box(
                    modifier = Modifier
                        .background(Green)
                        .fillMaxWidth()
                        .height(150.dp)
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
                        Icon(
                            imageVector = Icons.Default.Home,
                            contentDescription = null,
                            tint = Color.White
                        )
                    },
                    onClick = {
                        coroutineScope.launch {
                            drawerState.close()
                        }
                        navigationController.navigate(
                            Screens.Home.screen
                        ) {
                            popUpTo(0)
                        }
                    }
                )
                NavigationDrawerItem(
                    label = {
                        Text(text = "Profile", color = Green)
                    },
                    selected = false,
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            tint = Color.White
                        )
                    },
                    onClick = {
                        coroutineScope.launch {
                            drawerState.close()
                        }
                        navigationController.navigate(
                            Screens.Profile.screen
                        ) {
                            popUpTo(0)
                        }
                    }
                )
                NavigationDrawerItem(
                    label = {
                        Text(text = "Setting", color = Green)
                    },
                    selected = false,
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = null,
                            tint = Color.White
                        )
                    },
                    onClick = {
                        coroutineScope.launch {
                            drawerState.close()
                        }
                        navigationController.navigate(
                            Screens.Setting.screen
                        ) {
                            popUpTo(0)
                        }
                    }
                )
                NavigationDrawerItem(
                    label = {
                        Text(text = "Logout", color = Green)
                    },
                    selected = false,
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = null,
                            tint = Color.White
                        )
                    },
                    onClick = {
                        coroutineScope.launch {
                            drawerState.close()
                        }
                        Toast.makeText(
                            context,
                            "Logout",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                )
            }
        }
    ) {
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
                            Icon(imageVector = Icons.Rounded.Menu, contentDescription = null)
                        }
                    },
                    actions = {
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(
                                painterResource(R.drawable.outline_photo_camera_24), contentDescription =
                            null,
                                tint = Color.White)
                        }
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(imageVector = Icons.Default.MoreVert, contentDescription =
                            null,
                                tint = Color.White)
                        }
                    }
                )
            },
            bottomBar = {
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
        ) {
            NavHost(
                navController = navigationController,
                startDestination = Screens.Home.screen
            ) {
                composable(Screens.Home.screen) { HomeScreen()}
                composable(Screens.Search.screen) { SearchScreen() }
                composable(Screens.Notification.screen) { NotificationScreen() }
                composable(Screens.Setting.screen) { SettingsScreen() }
                composable(Screens.Profile.screen) { ProfileScreen() }
                composable(Screens.Post.screen) { PostScreen() }
            }
        }
        if (showBottomSheet) {
            ModalBottomSheet(onDismissRequest = {
                showBottomSheet = false
            },
                sheetState = sheetState
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    BottomSheetItem(icon = Icons.Default.Share, title = "Share a Post") {
                        showBottomSheet = false
                        navigationController.navigate(Screens.Post.screen) {
                            popUpTo(0)
                        }
                    }
                    BottomSheetItem(icon = Icons.Default.Star, title = "Favorites") {
                        showBottomSheet = false

                    }
                    BottomSheetItem(icon = Icons.Default.ThumbUp, title = "Liked") {
                        showBottomSheet = false

                    }
                    BottomSheetItem(icon = Icons.Default.PlayArrow, title = "Share a Video") {
                        showBottomSheet = false

                    }
                }
            }
        }
    }
}

@Composable
fun BottomSheetItem(
    icon: ImageVector,
    title: String,
    onClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.clickable { onClick() }
    ) {
        Icon(icon, null, tint = Green)
        Text(text = title, color = Green, fontSize = 22.sp)
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    DrawerTopBottomBarTheme {
        AppScreen()
    }
}