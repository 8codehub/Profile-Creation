package com.dev.profilecreation.presentation.core

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel<S : State, I : Intent, SE : SingleEvent> : ViewModel() {

    val viewState = MutableStateFlow(initState())

    var singleIntent = MutableSharedFlow<SE>(replay = 1, onBufferOverflow = BufferOverflow.DROP_LATEST)

    fun sendIntent(intent: I) {
        viewModelScope.launch {
            viewState.emit(reduceIntent(intent, viewState.value))
            handleIntent(intent)
        }
    }

    fun triggerSingleEvent(event: SE) = viewModelScope.launch {
        singleIntent.tryEmit(event)
    }

    abstract suspend fun handleIntent(intent: I)

    abstract fun reduceIntent(intent: I, state: S): S

    abstract fun initState(): S
}
