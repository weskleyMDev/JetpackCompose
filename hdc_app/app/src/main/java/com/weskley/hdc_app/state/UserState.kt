package com.weskley.hdc_app.state

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.weskley.hdc_app.model.User

data class UserState(
    val users: List<User> = emptyList(),
    val name: MutableState<String> = mutableStateOf(""),
    val age: MutableState<String> = mutableStateOf(""),
    val bloodType: MutableState<String> = mutableStateOf(""),
    val openDialog: MutableState<Boolean> = mutableStateOf(false),
    val userUpdated: User? = null
)
