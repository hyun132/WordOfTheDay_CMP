package org.hyun.projectkmp

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.window.ComposeUIViewController
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.hyun.projectkmp.app.App
import org.hyun.projectkmp.di.initKoin

fun MainViewController() = ComposeUIViewController(
    configure = {
        initKoin()
    }
) {
    val permissionManager = AudioPermissionManager() // Activity 전달

    LaunchedEffect(Unit) {
        val granted = permissionManager.requestPermission()
        if (granted) {
            // 권한 허용됨
        } else {
            println("권한 거부됨")
        }
    }
    App()
}