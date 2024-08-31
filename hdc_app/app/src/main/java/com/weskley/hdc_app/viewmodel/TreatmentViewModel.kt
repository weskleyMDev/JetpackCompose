package com.weskley.hdc_app.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.weskley.hdc_app.dao.TreatmentDao
import com.weskley.hdc_app.model.Treatment
import com.weskley.hdc_app.state.TreatmentEvent
import com.weskley.hdc_app.state.TreatmentState
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
class TreatmentViewModel @Inject constructor(
    private val treatmentDao: TreatmentDao
): ViewModel() {

    private val _treatmentList = treatmentDao.getTreatments().stateIn(viewModelScope,
        SharingStarted.WhileSubscribed(), emptyList())
    private val _treatmentState = MutableStateFlow(TreatmentState())
    val treatmentState = combine(_treatmentState, _treatmentList) { state, list ->
        state.copy(
            treatmentList = list
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), TreatmentState())

    fun treatmentEvent(event: TreatmentEvent) {
        when(event) {
            is TreatmentEvent.DeleteTreatment -> {
                viewModelScope.launch(Dispatchers.IO) {
                    treatmentDao.deleteTreatment(event.treatment)
                }
            }
            TreatmentEvent.SaveTreatment -> {
                viewModelScope.launch {
                    val title = treatmentState.value.title.value
                    val startDate = treatmentState.value.startDate.value
                    val endDate = treatmentState.value.endDate.value
                    if(title.isBlank() || startDate.isBlank() || endDate.isBlank()) {
                        return@launch
                    }
                    val treatment = Treatment(
                        title = title,
                        startDate = startDate,
                        endDate = endDate
                    )
                    viewModelScope.launch(Dispatchers.IO) {
                        treatmentDao.upsertTreatment(treatment)
                    }
                    _treatmentState.update { clear ->
                        clear.copy(
                            title = mutableStateOf(""),
                            startDate = mutableStateOf(""),
                            endDate = mutableStateOf("")
                        )
                    }
                }
            }
            TreatmentEvent.hideAddDialog -> {
                _treatmentState.update {
                    it.copy(
                        addDialog = mutableStateOf(false)
                    )
                }
            }
            TreatmentEvent.hideStatusDialog -> {
                _treatmentState.update {
                    it.copy(
                        statusDialog = mutableStateOf(false)
                    )
                }
            }
            TreatmentEvent.showAddDialog -> {
                _treatmentState.update {
                    it.copy(
                        addDialog = mutableStateOf(true)
                    )
                }
            }
            TreatmentEvent.showStatusDialog -> {
                _treatmentState.update {
                    it.copy(
                        statusDialog = mutableStateOf(true)
                    )
                }
            }
        }
    }
}