package org.hyun.projectkmp

import androidx.compose.ui.window.ComposeUIViewController
import org.hyun.projectkmp.app.App
import org.hyun.projectkmp.di.initKoin

fun MainViewController() = ComposeUIViewController(
    configure = {
        initKoin()
    }
) {
    App()
}