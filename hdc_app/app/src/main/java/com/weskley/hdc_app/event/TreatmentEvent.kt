package com.weskley.hdc_app.event

import com.weskley.hdc_app.model.Treatment

sealed interface TreatmentEvent {
    data object SaveTreatment : TreatmentEvent
    data object showAddDialog : TreatmentEvent
    data object hideAddDialog : TreatmentEvent
    data object showStatusDialog : TreatmentEvent
    data object hideStatusDialog : TreatmentEvent
    data object showUpdateDialog : TreatmentEvent
    data object hideUpdateDialog : TreatmentEvent
    data class DeleteTreatment(val treatment: Treatment) : TreatmentEvent
    data class UpdateTreatment(val treatment: Treatment) : TreatmentEvent
}