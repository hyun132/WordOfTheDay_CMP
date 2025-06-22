package org.hyun.projectkmp.core.presentation

import org.hyun.projectkmp.app.Routes

sealed interface UiEffect {
    data class ShowError(val message:String):UiEffect
    data class NavigateTo(val destination:Routes):UiEffect
}