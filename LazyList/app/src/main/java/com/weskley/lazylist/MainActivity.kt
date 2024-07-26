package com.weskley.lazylist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.weskley.lazylist.ui.theme.LazyListTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LazyListTheme {
                Scaffold { innerPadding ->
                    val fruits = listOf("Banana", "Apple", "Mango",
                        "Pineapple", "Grape", "Orange", "Lemon", "Kiwi",
                        "Cherry", "Papaya", "Peach", "Melon", "Coconut",
                        "Banana", "Apple", "Mango", "Pineapple", "Grape",
                        "Orange", "Lemon", "Kiwi", "Cherry", "Papaya",
                        "Peach", "Melon", "Coconut", "Banana", "Apple",
                        "Mango", "Pineapple", "Grape", "Orange", "Lemon",
                        "Kiwi", "Cherry", "Papaya", "Peach", "Melon", "Coconut")
                    Box(
                        modifier = Modifier
                            .padding(innerPadding)
                            .background
                                (Color.White)
                            .fillMaxSize()
                    ) {
                        ShowList(list = fruits)
                    }
                }
            }
        }
    }
}

@Composable
fun ShowList(list: List<String>) {
    LazyColumn {
        items(list) { elem ->
            Text(text = elem, fontSize = 30.sp, color = Color.Red)
        }
    }
    /*Column {
        list.forEach {
            Text(text = it, fontSize = 30.sp, color = Color.Red)
        }
     }*/

    /*LazyRow {
        items(list) { elem ->
            Text(text = elem, fontSize = 30.sp, color = Color.Red)
        }
    }*/
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    LazyListTheme {

    }
}