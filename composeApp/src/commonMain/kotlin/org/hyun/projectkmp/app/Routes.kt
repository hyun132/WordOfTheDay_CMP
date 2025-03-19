package org.hyun.projectkmp.app

import kotlinx.serialization.Serializable

interface Routes {
    @Serializable
    data object MainGraph:Routes

    @Serializable
    data object Home:Routes

    @Serializable
    data class Word(val word:String):Routes

    @Serializable
    data object History:Routes

    @Serializable
    data object Profile:Routes
}