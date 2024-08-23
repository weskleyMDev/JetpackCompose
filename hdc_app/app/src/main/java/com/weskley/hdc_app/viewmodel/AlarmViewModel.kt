package com.weskley.hdc_app.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.weskley.hdc_app.dao.NotificationDao
import com.weskley.hdc_app.model.CustomNotification
import com.weskley.hdc_app.service.NotificationService
import com.weskley.hdc_app.state.NotificationEvent
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
    private val service: NotificationService,
    private val database: NotificationDao,
) : ViewModel() {

    private val _notifications =
        database.findAll().stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
    private val _state = MutableStateFlow(NotificationState())
    val state = combine(_state, _notifications) { state, notifications ->
        state.copy(
            notifications = notifications
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), NotificationState())

    fun onEvent(event: NotificationEvent) {
        when (event) {
            is NotificationEvent.DeleteNotification -> {
                viewModelScope.launch(Dispatchers.IO) {
                    database.deleteNotification(event.id)
                }
            }

            NotificationEvent.HideBottomSheet -> {
                _state.update {
                    it.copy(
                        isOpened = false
                    )
                }
            }

            NotificationEvent.SaveNotification -> {
                val title = state.value.title.trim()
                val body = state.value.body.trim()
                val time = state.value.time
                val image = state.value.image

                if (title.isBlank() || body.isBlank() || time.isBlank()) {
                    return
                }

                val notification = CustomNotification(
                    title = title,
                    body = body,
                    time = time,
                    image = image,
                )
                viewModelScope.launch(Dispatchers.IO) {
                    database.insertOrUpdateNotification(notification)
                }
                _state.update {
                    it.copy(
                        isOpened = false,
                        title = "",
                        body = "",
                        time = "",
                        image = 0,
                        active = false,
                        showAlert = false
                    )
                }
                Log.i("AlarmViewModel", "Notification saved: ${notification.id} - ${notification.title} - ${notification.body} - ${notification.time} - ${notification.image}")
            }

            is NotificationEvent.SetBody -> {
                _state.update {
                    it.copy(
                        body = event.body
                    )
                }
            }

            is NotificationEvent.SetTime -> {
                _state.update {
                    it.copy(
                        time = event.time
                    )
                }
            }

            is NotificationEvent.SetTitle -> {
                _state.update {
                    it.copy(
                        title = event.title
                    )
                }
            }

            NotificationEvent.ShowBottomSheet -> {
                _state.update {
                    it.copy(
                        isOpened = true
                    )
                }
            }

            is NotificationEvent.SetImage -> {
                _state.update {
                    it.copy(
                        image = event.image
                    )
                }
            }

            NotificationEvent.ClearTextFields -> {
                _state.update {
                    it.copy(
                        isOpened = false,
                        title = "",
                        body = "",
                        time = "",
                        image = 0,
                        active = false,
                        showAlert = false
                    )
                }
            }

            is NotificationEvent.SetActive -> {
                viewModelScope.launch(Dispatchers.IO) {
                    database.updateActive(event.active, event.id)
                }
            }

            NotificationEvent.ShowAlert -> {
                _state.update {
                    it.copy(
                        showAlert = !it.showAlert
                    )
                }
            }

        }
    }

    var selected = mutableIntStateOf(0)
    var isPickerOpen by mutableStateOf(false)
    private val _feedback = MutableStateFlow("")
    val feedback get() = _feedback

    fun pickerState() {
        isPickerOpen = !isPickerOpen
    }

    fun setAlarm(item: CustomNotification) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                service.setAlarm(item)
            } catch (e: Exception) {
                Log.e("AlarmViewModel", "Error occurred while setting alarm", e)
            }
        }
    }

    fun resetAllAlarms() {
        viewModelScope.launch {
            val notifications = _notifications.value

            notifications.forEach { notification ->
                resetAlarm(notification)
            }
        }
    }

    fun resetAlarm(item: CustomNotification) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                service.resetAlarm(item)
            } catch (e: Exception) {
                Log.e("AlarmViewModel", "Error occurred while resetting alarm", e)
            }
        }
    }

    fun cancelAlarm(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                service.cancelAlarm(id)
            } catch (e: Exception) {
                Log.e("AlarmViewModel", "Error occurred while canceling alarm", e)
            }
        }
    }
}
