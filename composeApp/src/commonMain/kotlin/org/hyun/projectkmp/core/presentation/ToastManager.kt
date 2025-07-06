package org.hyun.projectkmp.core.presentation

expect open class ToastManager() {
    fun showToast(message:String,toastDurationType: ToastDurationType)
}

