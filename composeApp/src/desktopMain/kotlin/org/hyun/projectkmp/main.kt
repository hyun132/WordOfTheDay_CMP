package org.hyun.projectkmp

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import org.hyun.projectkmp.app.App
import org.hyun.projectkmp.di.initKoin

fun main() = application {
    initKoin()
    Window(
        onCloseRequest = ::exitApplication,
        title = "WordOfTheDay",
    ) {
        App()
    }
}