package org.hyun.projectkmp

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class AndroidPlatform : Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
}

actual fun getPlatform(): Platform = AndroidPlatform()

actual class AudioPermissionManager(private val activity: Activity) {

    @SuppressLint("InlinedApi")
    actual suspend fun requestPermission(): Boolean = suspendCancellableCoroutine { cont ->
        val permission = android.Manifest.permission.RECORD_AUDIO

        if (ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED) {
            cont.resume(true)
        } else {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(permission),
                1001
            )
            // NOTE: 실제 사용 시에는 request result를 Activity에서 받아 콜백이나 Channel로 전달해야 함
            cont.resume(false) // 임시로 false 반환
        }
    }
}