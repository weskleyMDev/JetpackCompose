package com.weskley.buttons

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.weskley.buttons.ui.theme.ButtonsTheme
import com.weskley.buttons.ui.theme.color1
import com.weskley.buttons.ui.theme.color2

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ButtonsTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) {
                    Column(modifier = Modifier.padding(it)) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            GradientButton(
                                text = "SAVE",
                                textColor = Color.White,
                                gradient = Brush.horizontalGradient(
                                    colors = listOf(
                                        color1,
                                        color2,
                                    )
                                ),
                            ) {

                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ButtonsTheme {
        GradientButton(
            text = "SAVE",
            textColor = Color.White,
            gradient = Brush.horizontalGradient(
                colors = listOf(
                    color1,
                    color2,
                )
            ),
        ) {

        }
    }
}