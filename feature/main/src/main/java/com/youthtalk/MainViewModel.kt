package com.youthtalk

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

sealed interface MainUiEffect {
    data object Notification : MainUiEffect
}

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {
    private val _effect = MutableSharedFlow<MainUiEffect>()
    val effect = _effect.asSharedFlow()

    fun setNotificationDialog() {
        _effect.tryEmit(MainUiEffect.Notification)
    }
}
