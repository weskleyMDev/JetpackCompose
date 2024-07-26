package com.weskley.loginpage

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.weskley.loginpage.screens.Home
import com.weskley.loginpage.ui.theme.Green
import com.weskley.loginpage.ui.theme.LoginPageTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LoginPageTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .padding(innerPadding)
                            .paint(
                                painterResource(id = R.drawable.jclogin),
                                contentScale = ContentScale.FillBounds
                            )
                    ) {
                        val navController = rememberNavController()
                        NavGraph(navController)
                    }
                }
            }
        }
    }
}

@Composable
fun LoginScreen(loginSuccess: () -> Unit) {
    var username by remember {
        mutableStateOf("")
    }
    var password by remember {
        mutableStateOf("")
    }
    val context = LocalContext.current.applicationContext

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                horizontal = 26.dp,
                vertical = 140.dp
            ),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = username,
            onValueChange = {newText ->
                username = newText
            },
            label = {
                Text(text = "username".replaceFirstChar { it.uppercase() })
            },
            shape = RoundedCornerShape(20.dp),
            colors = TextFieldDefaults.colors(
                focusedLeadingIconColor = Green,
                unfocusedLeadingIconColor = Green,
                focusedLabelColor = Green,
                unfocusedLabelColor = Green,
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedIndicatorColor = Green,
                unfocusedIndicatorColor = Green,
                unfocusedPlaceholderColor = Green
            ),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
        )
        OutlinedTextField(
            value = password,
            onValueChange = {newText ->
                password = newText
            },
            label = {
                Text(text = "password".replaceFirstChar { it.uppercase() })
            },
            shape = RoundedCornerShape(20.dp),
            colors = TextFieldDefaults.colors(
                focusedLeadingIconColor = Green,
                unfocusedLeadingIconColor = Green,
                focusedLabelColor = Green,
                unfocusedLabelColor = Green,
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedIndicatorColor = Green,
                unfocusedIndicatorColor = Green,
                unfocusedPlaceholderColor = Green
            ),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = null
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            visualTransformation = PasswordVisualTransformation()
        )
        Button(
            onClick = {
                if (auth(username, password)) {
                    loginSuccess()
                    Toast.makeText(
                        context,
                        "Login Success!",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        context,
                        "Login Failed!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            },
            colors = ButtonDefaults.buttonColors(Green),
            contentPadding = PaddingValues(
                start = 60.dp,
                end = 60.dp,
                top = 8.dp,
                bottom = 8.dp
            ),
            modifier = Modifier.padding(top = 18.dp)
        ) {
            Text(text = "Login", fontSize = 22.sp, color = Color.White)
        }
    }
}

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        composable("login") {
            LoginScreen(
                loginSuccess = {
                    navController.navigate("home") {
                        popUpTo(0)
                    }
                }
            )
        }
        composable("home") {
            Home()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    LoginPageTheme {
        LoginScreen {}
    }
}

private fun auth(username: String, password: String): Boolean {
    val user = "admin"
    val pass = "admin"
    return username == user && password == pass
}