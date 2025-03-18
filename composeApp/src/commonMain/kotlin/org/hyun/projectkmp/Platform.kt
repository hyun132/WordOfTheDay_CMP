package org.hyun.projectkmp

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform