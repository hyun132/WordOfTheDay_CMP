package org.hyun.projectkmp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import org.hyun.projectkmp.app.App

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val permissionManager = AudioPermissionManager(this) // Activity 전달
        lifecycleScope.launch {
            val granted = permissionManager.requestPermission()
            if (granted) {
                // 권한 허용됨
            } else {
               Toast.makeText(this@MainActivity,"앱 사용을 위해 권한을 허용해 주세요",Toast.LENGTH_SHORT).show()
            }
        }
        setContent {
            App()
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}