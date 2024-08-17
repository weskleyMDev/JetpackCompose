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
import com.weskley.hdc_app.R
import com.weskley.hdc_app.model.InputModel
import com.weskley.hdc_app.module.Manager
import com.weskley.hdc_app.receiver.NotificationReceiver
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class AlarmViewModel @Inject constructor(
    @Manager private val manager: AlarmManager,
    private val inputModel: InputModel,
    @ApplicationContext private val context: Context
) : ViewModel() {
    var selected = mutableIntStateOf(0)
    var myImage by mutableIntStateOf(R.drawable.logo_circ_branco)
    var hora by mutableIntStateOf(0)
    var minuto by mutableIntStateOf(0)
    var isPickerOpen by mutableStateOf(false)
    var titulo = mutableStateOf(inputModel.titulo)
    var descricao = mutableStateOf(inputModel.descricao)
    var label by mutableStateOf("")
    var body by mutableStateOf("")

    fun pickerState() {
        isPickerOpen = !isPickerOpen
    }

    fun changeDesc(newDesc: String) {
        inputModel.descricao = newDesc
        descricao.value = newDesc
    }
    fun changeTitulo(newTitulo: String) {
        inputModel.titulo = newTitulo
        titulo.value = newTitulo
    }

    fun clearFields() {
        changeTitulo("")
        changeDesc("")
        hora = 0
        minuto = 0
        label = ""
        body = ""
        myImage = R.drawable.logo_circ_branco
    }

    fun setAlarm() {
        val time = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, hora)
            set(Calendar.MINUTE, minuto)
        }
        val alarmIntent = Intent(context, NotificationReceiver::class.java).apply {
            putExtra("title", titulo.value)
            putExtra("text", descricao.value)
            putExtra("img", myImage)
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            alarmIntent,
            PendingIntent.FLAG_IMMUTABLE
        )
        manager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, time.timeInMillis, pendingIntent)
    }
}
