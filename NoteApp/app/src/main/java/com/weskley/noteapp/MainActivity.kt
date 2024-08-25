package com.weskley.noteapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.weskley.noteapp.screen.HomeScreen
import com.weskley.noteapp.ui.theme.NoteAppTheme
import com.weskley.noteapp.viewmodel.NoteViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private lateinit var viewModel: NoteViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NoteAppTheme {
                viewModel = hiltViewModel()
                val state by viewModel.state.collectAsState()
                HomeScreen(state = state, onEvent = viewModel::onEvent)
            }
        }
    }
}