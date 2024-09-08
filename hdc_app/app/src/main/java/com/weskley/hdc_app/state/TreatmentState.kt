package com.weskley.hdc_app.state

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.weskley.hdc_app.model.Treatment

data class TreatmentState(
    val treatmentList: List<Treatment> = emptyList(),
    val title: MutableState<String> = mutableStateOf(""),
    val startDate: MutableState<String> = mutableStateOf(""),
    val endDate: MutableState<String> = mutableStateOf(""),
    val duration: MutableState<String> = mutableStateOf(""),
    val addDialog: MutableState<Boolean> = mutableStateOf(false),
    val statusDialog: MutableState<Boolean> = mutableStateOf(false),
    val updateDialog: MutableState<Boolean> = mutableStateOf(false),
    val treatmentUpdated: Treatment? = null,
    val treatmentId: MutableState<Int> = mutableStateOf(0)
)
