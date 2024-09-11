package com.weskley.hdc_app.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.weskley.hdc_app.dao.NotificationDao
import com.weskley.hdc_app.model.CustomNotification
import com.weskley.hdc_app.service.NotificationService
import com.weskley.hdc_app.event.NotificationEvent
import com.weskley.hdc_app.model.Medicine
import com.weskley.hdc_app.state.NotificationState
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
class AlarmViewModel @Inject constructor(
    private val database: NotificationDao,
    private val service: NotificationService
) : ViewModel() {

    private val _notifications =
        database.findAll()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
    private val _state = MutableStateFlow(NotificationState())
    val state = combine(_state, _notifications) { state, notifications ->
        state.copy(
            notifications = notifications
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), NotificationState())

    val selected = mutableStateOf(0)
    private val pickerState = mutableStateOf(false)
    fun pickerState() {
        pickerState.value = !pickerState.value
    }

    fun setAlarm(item: Medicine) {
        viewModelScope.launch {
            service.setDailyAlarm(item)
        }
    }

    fun cancelAlarm(id: Int) {
        viewModelScope.launch {
            service.cancelDailyAlarm(id)
        }
    }

    fun onEvent(event: NotificationEvent) {
        when (event) {
            is NotificationEvent.DeleteNotification -> {
                viewModelScope.launch(Dispatchers.IO) {
                    database.deleteNotification(event.notification)
                }
            }

            NotificationEvent.SaveNotification -> {
                viewModelScope.launch {
                    val title = state.value.title.value
                    val body = state.value.type.value
                    val time = state.value.time.value
                    val image = state.value.image.value

                    if (title.isBlank() || body.isBlank() || time.isBlank()) {
                        return@launch
                    }

                    val notification = CustomNotification(
                        title = title.trim(),
                        type = body.trim(),
                        time = time,
                        image = image,
                    )
                    viewModelScope.launch(Dispatchers.IO) {
                        database.upsertNotification(notification)
                    }

                    _state.update { clear ->
                        clear.copy(
                            title = mutableStateOf(""),
//                            body = mutableStateOf(""),
                            time = mutableStateOf(""),
                            image = mutableStateOf(""),
                        )
                    }
                }
            }

            is NotificationEvent.UpdateNotification -> {
                viewModelScope.launch {
                    val title = state.value.title.value.trim()
//                    val body = state.value.body.value.trim()
                    val time = state.value.time.value
                    val image = state.value.image.value
                    val active = state.value.active.value

                    /*if (title.isBlank() || body.isBlank() || time.isBlank()) {
                        return@launch
                    }*/

                    val notification = event.notification.copy(
                        title = title,
//                        body = body,
                        time = time,
                        image = image,
                        active = active
                    )
                    viewModelScope.launch(Dispatchers.IO) {
                        database.upsertNotification(notification)
                    }

                    _state.update { clear ->
                        clear.copy(
                            title = mutableStateOf(""),
//                            body = mutableStateOf(""),
                            time = mutableStateOf(""),
                            image = mutableStateOf(""),
                        )
                    }
                }
            }

            is NotificationEvent.SetActive -> {
                viewModelScope.launch(Dispatchers.IO) {
                    database.updateActive(event.active, event.id)
                }
            }

            is NotificationEvent.FindNotificationById -> {
                viewModelScope.launch(Dispatchers.IO) {
                    database.findNotificationById(event.id).collect { notification ->
                        if (notification != null) {
                            _state.update {
                                it.copy(
                                    title = mutableStateOf(notification.title),
//                                    body = mutableStateOf(notification.body),
                                    time = mutableStateOf(notification.time),
                                    image = mutableStateOf(notification.image),
                                    active = mutableStateOf(notification.active),
                                )
                            }
                        } else {
                            Log.e("AlarmViewModel", "Notification not found")
                        }
                    }
                }
            }

            NotificationEvent.ClearTextFields -> {
                viewModelScope.launch {
                    _state.update {
                        it.copy(
                            title = mutableStateOf(""),
//                            body = mutableStateOf(""),
                            time = mutableStateOf(""),
                            image = mutableStateOf(""),
                        )
                    }
                }
            }
        }
    }
}
