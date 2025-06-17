package org.hyun.projectkmp.core.presentation

sealed interface UiEffect {
    data class ShowError(val message:String):UiEffect
}