package com.weskley.passfield

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import com.weskley.passfield.ui.theme.PassFieldTheme
import com.weskley.passfield.ui.theme.Shapes

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PassFieldTheme {
                Scaffold { innerPadding ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        PassField()
                    }
                }
            }
        }
    }
}

@Composable
fun PassField() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        var password by rememberSaveable {
            mutableStateOf("")
        }
        var isVisible by remember {
            mutableStateOf(false)
        }
        val icon = if (!isVisible) painterResource(id = R.drawable.baseline_visibility_24)
        else painterResource(id = R.drawable.baseline_visibility_off_24)
        val maxChar = 10

        OutlinedTextField(
            value = password,
            onValueChange = {
                if (it.length <= maxChar) {
                    password = it
                }
            },
            label = {
                Text(text = "Password")
            },
            placeholder = {
                Text(text = "Password")
            },
            leadingIcon = {
                Icon(imageVector = Icons.Filled.Lock, contentDescription = "pass")
            },
            trailingIcon = {
                IconButton(
                    onClick = {
                        isVisible = !isVisible
                    },
                ) {
                    Icon(
                        painter = icon,
                        contentDescription = "isVisible",
                    )
                }
            },
            shape = Shapes.medium,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.NumberPassword,
                imeAction = ImeAction.Done
            ),
            visualTransformation = if (isVisible) VisualTransformation.None else
                PasswordVisualTransformation()
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    PassFieldTheme {
        PassField()
    }
}