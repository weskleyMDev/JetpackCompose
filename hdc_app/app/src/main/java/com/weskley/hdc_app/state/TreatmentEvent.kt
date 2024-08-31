package com.weskley.hdc_app.state

import com.weskley.hdc_app.model.Treatment

sealed interface TreatmentEvent {
    data object SaveTreatment : TreatmentEvent
    data object showAddDialog : TreatmentEvent
    data object hideAddDialog : TreatmentEvent
    data object showStatusDialog : TreatmentEvent
    data object hideStatusDialog : TreatmentEvent
    data class DeleteTreatment(val treatment: Treatment) : TreatmentEvent
}