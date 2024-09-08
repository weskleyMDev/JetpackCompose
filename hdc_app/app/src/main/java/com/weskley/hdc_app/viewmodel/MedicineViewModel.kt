package com.weskley.hdc_app.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.weskley.hdc_app.dao.MedicineDao
import com.weskley.hdc_app.event.MedicineEvent
import com.weskley.hdc_app.model.Medicine
import com.weskley.hdc_app.service.NotificationService
import com.weskley.hdc_app.state.MedicineState
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
class MedicineViewModel @Inject constructor(
    private val medicineDao: MedicineDao
) : ViewModel() {

    private val _medicines = medicineDao.getMedicines()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
    private val _state = MutableStateFlow(MedicineState())
    val state = combine(_state, _medicines) { state, medicines ->
        state.copy(
            medicines = medicines
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), MedicineState())

    fun medicineEvent(event: MedicineEvent) {
        when (event) {
            is MedicineEvent.DeleteMedicine -> {
                viewModelScope.launch(Dispatchers.IO) {
                    medicineDao.deleteMedicine(event.medicine)
                }
            }

            MedicineEvent.ShowAddMedicineDialog -> {
                viewModelScope.launch {
                    _state.update {
                        it.copy(
                            showAddMedicine = mutableStateOf(true)
                        )
                    }
                }
            }

            MedicineEvent.HideAddMedicineDialog -> {
                viewModelScope.launch {
                    _state.update {
                        it.copy(
                            showAddMedicine = mutableStateOf(false)
                        )
                    }
                }
            }

            MedicineEvent.SaveMedicine -> {
                viewModelScope.launch {
                    val name = state.value.name.value
                    val amount = state.value.amount.value
                    val type = state.value.type.value
                    val time = state.value.time.value
                    val image = state.value.image.value
                    val repetition = state.value.repetition.value
                    val count = state.value.count.value
                    val treatmentId = state.value.treatmentId.value ?: return@launch

                    Log.d("MedicineViewModel", "Saving medicine with treatmentId: $treatmentId")

                    if (name.isBlank() || amount.isBlank() || type.isBlank() || time.isBlank() || image.isBlank() || repetition.isBlank()) {
                        return@launch
                    }

                    val medicine = state.value.updateMedicine?.copy(
                        name = name,
                        amount = amount,
                        type = type,
                        time = time,
                        image = image,
                        repetition = repetition,
                        active = false,
                        count = count,
                        treatmentId = treatmentId
                    ) ?: Medicine(
                        name = name,
                        amount = amount,
                        type = type,
                        time = time,
                        image = image,
                        repetition = repetition,
                        active = false,
                        count = count,
                        treatmentId = treatmentId
                    )

                    viewModelScope.launch(Dispatchers.IO) {
                        medicineDao.upsertMedicine(medicine)
                    }

                    _state.update {
                        it.copy(
                            name = mutableStateOf(""),
                            amount = mutableStateOf(""),
                            type = mutableStateOf(""),
                            time = mutableStateOf(""),
                            image = mutableStateOf(""),
                            repetition = mutableStateOf(""),
                            count = mutableStateOf(0),
                            active = mutableStateOf(false),
                            showAddMedicine = mutableStateOf(false),
                            updateMedicine = null,
                            treatmentId = mutableStateOf(null)
                        )
                    }
                }
            }

            is MedicineEvent.UpdateActiveStatus -> {
                viewModelScope.launch(Dispatchers.IO) {
                    medicineDao.updateActiveStatus(event.active, event.id)
                }
            }

            is MedicineEvent.UpdateMedicine -> {
                _state.update {
                    it.copy(
                        showAddMedicine = mutableStateOf(true),
                        updateMedicine = event.medicine
                    )
                }
            }

            is MedicineEvent.IncrementCount -> {
                viewModelScope.launch(Dispatchers.IO) {
                    val medicine = medicineDao.getMedicineById(event.id) ?: return@launch
                    val newCount = medicine.count + event.amount
                    val updatedMedicine = medicine.copy(count = newCount)
                    medicineDao.upsertMedicine(updatedMedicine)
                }
            }

            is MedicineEvent.SetTreatmentId -> {
                viewModelScope.launch {
                    Log.d("MedicineViewModel", "Setting treatmentId to: ${event.treatmentId}")
                    _state.update {
                        it.copy(treatmentId = mutableStateOf(event.treatmentId))
                    }
                }
            }
        }
    }
}