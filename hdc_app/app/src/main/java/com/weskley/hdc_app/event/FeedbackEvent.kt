package com.weskley.hdc_app.event

import com.weskley.hdc_app.model.Feedback

sealed interface FeedbackEvent {
    data object SaveFeedback : FeedbackEvent
    data object ShowAddFeedback : FeedbackEvent
    data object HideAddFeedback : FeedbackEvent
    data object ShowUpdateFeedback : FeedbackEvent
    data object HideUpdateFeedback : FeedbackEvent
    data class DeleteFeedback(val feedback: Feedback): FeedbackEvent

}