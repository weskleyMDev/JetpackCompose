package com.weskley.mvvm

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class MyViewModel: ViewModel() {

    var count by mutableIntStateOf(0)

    fun increaseCount() {
        this.count++
    }

}