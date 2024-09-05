package com.weskley.hdc_app.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.weskley.hdc_app.dao.FeedbackDao
import com.weskley.hdc_app.event.FeedbackEvent
import com.weskley.hdc_app.model.Feedback
import com.weskley.hdc_app.state.FeedbackState
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
class FeedbackViewModel @Inject constructor(
    private val feedbackDao: FeedbackDao
) : ViewModel() {
    private val _feedbacks = feedbackDao.getAllFeedbacks().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = emptyList()
    )
    private val _feedbackState = MutableStateFlow(FeedbackState())
    val feedbackState = combine(_feedbackState, _feedbacks) { state, feedbacks ->
        state.copy(
            feedbackList = feedbacks
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = FeedbackState()
    )

    fun feedBackEvent(event: FeedbackEvent) {
        when (event) {
            is FeedbackEvent.DeleteFeedback -> {
                viewModelScope.launch(Dispatchers.IO) {
                    feedbackDao.deleteFeedback(event.feedback)
                }
            }

            FeedbackEvent.SaveFeedback -> {
                viewModelScope.launch {
                    val medicine = feedbackState.value.medicine.value
                    val answer = feedbackState.value.answer.value
                    val justification = feedbackState.value.justification.value
                    val entryTime = feedbackState.value.entryTime.value
                    val shippingTime = feedbackState.value.shippingTime.value

                    if (
                        medicine.isBlank() ||
                        answer.isBlank() ||
                        justification.isBlank() ||
                        entryTime.isBlank() ||
                        shippingTime.isBlank()
                    ) {
                        return@launch
                    }
                    val feedback = feedbackState.value.feedbackUpdated?.copy(
                        medicine = medicine,
                        answer = answer,
                        justification = justification,
                        entryTime = entryTime,
                        shippingTime = shippingTime
                    ) ?: Feedback(
                        medicine = medicine,
                        answer = answer,
                        justification = justification,
                        entryTime = entryTime,
                        shippingTime = shippingTime
                    )
                    viewModelScope.launch(Dispatchers.IO) {
                        feedbackDao.upsertFeedback(feedback)
                    }
                    _feedbackState.update {
                        it.copy(
                            medicine = mutableStateOf(""),
                            answer = mutableStateOf(""),
                            justification = mutableStateOf(""),
                            entryTime = mutableStateOf(""),
                            shippingTime = mutableStateOf("")
                        )
                    }
                }
            }

            FeedbackEvent.ShowAddFeedback -> {
                viewModelScope.launch {
                    _feedbackState.update {
                        it.copy(
                            isAddDialogOpen = mutableStateOf(true)
                        )
                    }
                }
            }

            FeedbackEvent.HideAddFeedback -> {
                viewModelScope.launch {
                    _feedbackState.update {
                        it.copy(
                            isAddDialogOpen = mutableStateOf(false)
                        )
                    }
                }
            }

            FeedbackEvent.ShowUpdateFeedback -> {
                viewModelScope.launch {
                    _feedbackState.update {
                        it.copy(
                            isUpdateDialogOpen = mutableStateOf(true)
                        )
                    }
                }
            }
            FeedbackEvent.HideUpdateFeedback -> {
                viewModelScope.launch {
                    _feedbackState.update {
                        it.copy(
                            isUpdateDialogOpen = mutableStateOf(false)
                        )
                    }
                }
            }
        }
    }
}