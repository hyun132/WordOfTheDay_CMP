package org.hyun.projectkmp

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform

expect class AudioPermissionManager {
    suspend fun requestPermission(): Boolean
}