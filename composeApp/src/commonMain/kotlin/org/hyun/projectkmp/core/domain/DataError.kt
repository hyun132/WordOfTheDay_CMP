package org.hyun.projectkmp.core.domain

sealed interface DataError : Error {
    enum class Remote:DataError{
        BAD_REQUEST,
        UNAUTHORIZED,
        FORBIDDEN,
        REQUEST_TIMEOUT,
        TOO_MANY_REQUESTS,
        NO_INTERNET,
        SERVER,
        SERIALIZATION,
        UNKNOWN
    }

    enum class Local: DataError {
        DISK_FULL,
        UNKNOWN
    }
}