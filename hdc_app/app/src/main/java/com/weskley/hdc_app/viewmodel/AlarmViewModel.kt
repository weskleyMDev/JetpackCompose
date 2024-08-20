package com.weskley.hdc_app.viewmodel

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.weskley.hdc_app.dao.NotificationDao
import com.weskley.hdc_app.model.CustomNotification
import com.weskley.hdc_app.module.DatabaseDao
import com.weskley.hdc_app.module.Manager
import com.weskley.hdc_app.receiver.NotificationReceiver
import com.weskley.hdc_app.state.NotificationEvent
import com.weskley.hdc_app.state.NotificationState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class AlarmViewModel @Inject constructor(
    @Manager private val manager: AlarmManager,
    @DatabaseDao private val database: NotificationDao,
    @ApplicationContext private val context: Context
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
                    database.insertNotification(notification)
                }
                _state.update {
                    it.copy(
                        isOpened = false,
                        title = "",
                        body = "",
                        time = "",
                        image = 0,
                    )
                }
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
                        image = 0
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

    fun pickerState() {
        isPickerOpen = !isPickerOpen
    }

    fun setAlarm(id: Int, title: String, body: String, image: Int, hour: Int, minute: Int) {
        val time = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
        }
        val alarmIntent = Intent(context, NotificationReceiver::class.java).apply {
            putExtra("title", title)
            putExtra("text", body)
            putExtra("img", image)
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            id,
            alarmIntent,
            PendingIntent.FLAG_IMMUTABLE
        )
        manager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, time.timeInMillis, pendingIntent)
    }

    fun cancelAlarm(id: Int) {
        val pendingIntent = Intent(context, NotificationReceiver::class.java).let { intent ->
            PendingIntent.getBroadcast(
                context,
                id,
                intent,
                PendingIntent.FLAG_IMMUTABLE
            )
        }
        manager.cancel(pendingIntent)
    }
}
