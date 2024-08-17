package com.weskley.floatingbutton

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.weskley.floatingbutton.ui.theme.FloatingButtonTheme
import com.weskley.floatingbutton.ui.theme.Green

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FloatingButtonTheme {
                Scaffold { innerPadding ->
                    Box(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                    ) { MyFloatingBtn() }
                }
            }
        }
    }
}

@Composable
fun MyFloatingBtn() {
    val context = LocalContext.current.applicationContext
    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        val (exFloatingBtn, floatingBtn) = createRefs()

        ExtendedFloatingActionButton(
            onClick = {
                Toast.makeText(
                    context,
                    "MENU!",
                    Toast.LENGTH_SHORT
                ).show()
            },
            modifier = Modifier
                .constrainAs(exFloatingBtn) {
                    bottom.linkTo(parent.bottom)
                    end.linkTo(parent.end)
                }
                .padding(bottom = 16.dp, end = 16.dp, top = 8.dp),
            containerColor = Green
        ) {
            Icon(
                imageVector = Icons.Filled.Menu,
                contentDescription = null,
                modifier = Modifier.size(30.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Menu".uppercase())
        }
        FloatingActionButton(
            onClick = {
                Toast.makeText(
                    context,
                    "ADD!",
                    Toast.LENGTH_SHORT
                ).show()
            },
            modifier = Modifier
                .constrainAs(floatingBtn) {
                    bottom.linkTo(exFloatingBtn.top)
                    end.linkTo(parent.end)
                }
                .padding(end = 16.dp),
            containerColor = Green
        ) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = null,
                modifier = Modifier.size(30.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MyFloatingBtnPreview() {
    FloatingButtonTheme {
        MyFloatingBtn()
    }
}