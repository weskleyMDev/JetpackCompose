package com.weskley.hdc_app.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.weskley.hdc_app.dao.UserDao
import com.weskley.hdc_app.event.UserEvent
import com.weskley.hdc_app.model.User
import com.weskley.hdc_app.state.UserState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userDao: UserDao
): ViewModel() {

    private val _users = userDao.getUsers().stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
    private val _userState = MutableStateFlow(UserState())
    val userState = combine(_userState, _users) { state, users ->
        state.copy(
            users = users
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), UserState())

    fun userEvent(event: UserEvent) {
        when(event) {
            is UserEvent.DeleteUser -> {
                viewModelScope.launch(Dispatchers.IO) {
                    userDao.deleteUser(event.user)
                }
            }
            UserEvent.SaveUser -> {
                viewModelScope.launch {
                    val name = userState.value.name.value.trim().replaceFirstChar { it.uppercase() }
                    val age = userState.value.age.value.trim()
                    val bloodType = userState.value.bloodType.value.trim().replaceFirstChar { it.uppercase() }
                    val imageUri = userState.value.imageUri.value

                    if(name.isBlank() || age.isBlank() || bloodType.isBlank()) {
                        return@launch
                    }

                    val user = userState.value.userUpdated?.copy(
                        name = name,
                        age = age.toInt(),
                        bloodType = bloodType,
                        imageUri = imageUri
                    ) ?: User(
                        name = name,
                        age = age.toInt(),
                        bloodType = bloodType,
                        imageUri = imageUri
                    )

                    viewModelScope.launch(Dispatchers.IO) {
                        userDao.upsertUser(user)
                    }

                    _userState.update { clear ->
                        clear.copy(
                            name = mutableStateOf(""),
                            age = mutableStateOf(""),
                            bloodType = mutableStateOf(""),
                            imageUri = mutableStateOf(""),
                            userUpdated = null
                        )
                    }
                }
            }
            UserEvent.HideDialog -> {
                viewModelScope.launch {
                    _userState.update {
                        it.copy(
                            openDialog = mutableStateOf(false),
                        )
                    }
                }
            }
            UserEvent.ShowDialog -> {
                viewModelScope.launch {
                    _userState.update {
                        it.copy(
                            openDialog = mutableStateOf(true),
                        )
                    }
                }
            }

            is UserEvent.EditUser -> {
                viewModelScope.launch {
                    _userState.update {
                        it.copy(
                            openDialog = mutableStateOf(true),
                            userUpdated = event.user
                        )
                    }
                }
            }
        }
    }
}