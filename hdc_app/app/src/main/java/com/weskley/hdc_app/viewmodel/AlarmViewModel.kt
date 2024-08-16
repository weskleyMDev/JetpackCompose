package com.weskley.hdc_app.viewmodel

import android.app.NotificationManager
import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.app.NotificationCompat
import androidx.lifecycle.ViewModel
import com.weskley.hdc_app.R
import com.weskley.hdc_app.component.MyTimePicker
import com.weskley.hdc_app.constant.Constants
import com.weskley.hdc_app.model.InputModel
import com.weskley.hdc_app.module.Channel
import com.weskley.hdc_app.module.Notification
import com.weskley.hdc_app.service.NotificationService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@OptIn(ExperimentalMaterial3Api::class)
@HiltViewModel
class AlarmViewModel @Inject constructor(
    @Notification private val service: NotificationService,
    @Channel private val channel: NotificationManager,
    private val inputModel: InputModel
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

    @Composable
    fun ShowTimePicker() {
        if (isPickerOpen) {
            MyTimePicker(
                onDismiss = { isPickerOpen = false },
                onConfirm = {
                    isPickerOpen = false
                    hora = it.hour
                    minuto = it.minute
                }
            )
        }
    }

    fun changeDesc(newDesc: String) {
        inputModel.descricao = newDesc
        descricao.value = newDesc
    }
    fun changeTitulo(newTitulo: String) {
        inputModel.titulo = newTitulo
        titulo.value = newTitulo
    }

    fun showNotification(
        context: Context,
        title: String,
        text: String,
        img: Int
    ) {
        channel.notify(
            Constants.REQUEST_CODE,
            service.createNotification(
                Constants.CHANNEL_ID,
                title,
                R.drawable.logo_circ_branco,
                title,
                text,
                Constants.HIGH_PRIORITY,
                selectImage(context.resources, img),
                NotificationCompat
                    .BigPictureStyle()
                    .bigPicture(selectImage(context.resources, img))
                    .bigLargeIcon(null as Bitmap?),
            ).build()
        )
    }

    fun setScheduleNotification(
        context: Context,
        hour: Int,
        minute: Int,
        title: String,
        text: String,
        img: Int
    ) {
        service.scheduleNotification(context, hour, minute, title, text, img)
    }
}

private fun selectImage(resource: Resources, image: Int): Bitmap {
    return BitmapFactory.decodeResource(resource, image)
}