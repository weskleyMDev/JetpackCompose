package com.weskley.hdc_app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.weskley.hdc_app.dao.FeedbackDao
import com.weskley.hdc_app.event.FeedbackEvent
import com.weskley.hdc_app.state.FeedbackState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FeedbackViewModel @Inject constructor(
    private val feedbackDao: FeedbackDao
): ViewModel() {
    private val _feedbacks = feedbackDao.getAllFeedbacks().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = emptyList()
    )
    private val _feedbackState = MutableStateFlow(FeedbackState())
    val feedbackState = combine(_feedbackState, _feedbacks) { state, feedbacks ->
        state.copy(
            feedbackList = feedbacks
        )}.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = FeedbackState()
        )

    fun feedBackEvent(event: FeedbackEvent) {
        when(event) {
            is FeedbackEvent.DeleteFeedback -> {
                viewModelScope.launch(Dispatchers.IO) {
                    feedbackDao.deleteFeedback(event.feedback)
                }
            }
            FeedbackEvent.SaveFeedback -> TODO()
            FeedbackEvent.ShowAddFeedback -> TODO()
            FeedbackEvent.HideAddFeedback -> TODO()
            FeedbackEvent.ShowUpdateFeedback -> TODO()
            FeedbackEvent.HideUpdateFeedback -> TODO()
        }
    }
}