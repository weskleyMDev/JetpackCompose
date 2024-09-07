package com.weskley.hdc_app.event

import com.weskley.hdc_app.model.Medicine

sealed interface MedicineEvent {
    data object SaveMedicine : MedicineEvent
    data class DeleteMedicine(val medicine: Medicine) : MedicineEvent
    data class UpdateActiveStatus(val active: Boolean, val id: Int) : MedicineEvent
    data class UpdateMedicine(val medicine: Medicine) : MedicineEvent
    data object ShowAddMedicineDialog : MedicineEvent
    data object HideAddMedicineDialog : MedicineEvent
    data object ShowUpdateMedicineDialog : MedicineEvent
    data object HideUpdateMedicineDialog : MedicineEvent
}