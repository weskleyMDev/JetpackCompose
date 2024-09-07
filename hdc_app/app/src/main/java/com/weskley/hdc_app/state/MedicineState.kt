package com.weskley.hdc_app.state

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.weskley.hdc_app.model.Medicine

data class MedicineState(
    val medicines: List<Medicine> = emptyList(),
    val name: MutableState<String> = mutableStateOf(""),
    val amount: MutableState<String> = mutableStateOf(""),
    val type: MutableState<String> = mutableStateOf(""),
    val time: MutableState<String> = mutableStateOf(""),
    val image: MutableState<String> = mutableStateOf(""),
    val repetition: MutableState<String> = mutableStateOf(""),
    val count: MutableState<Int> = mutableStateOf(0),
    val active: MutableState<Boolean> = mutableStateOf(false),
    val showAddMedicine: MutableState<Boolean> = mutableStateOf(false),
    val showUpdateMedicine: MutableState<Boolean> = mutableStateOf(false),
    val updateMedicine: Medicine? = null
)
