package com.weskley.hdc_app.event

import com.weskley.hdc_app.model.Medicine

sealed interface MedicineEvent {
    data object SaveMedicine : MedicineEvent
    data class DeleteMedicine(val medicine: Medicine) : MedicineEvent
    data class UpdateActiveStatus(val active: Boolean, val id: Int) : MedicineEvent
    data class UpdateMedicine(val medicine: Medicine) : MedicineEvent
    data object ShowAddMedicineDialog : MedicineEvent
    data object HideAddMedicineDialog : MedicineEvent
    data class IncrementCount(val id: Int, val amount: Int) : MedicineEvent
    data class SetTreatmentId(val treatmentId: Int) : MedicineEvent
    data object ClearFields : MedicineEvent
    data class GetMedicineById(val id: Int) : MedicineEvent
}