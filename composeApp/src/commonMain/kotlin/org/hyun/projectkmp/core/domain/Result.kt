package org.hyun.projectkmp.core.domain

sealed interface Result<out D, out E : Error> {
    data class Success<out D>(val data: D) : Result<D, Nothing>
    data class Error<out E : org.hyun.projectkmp.core.domain.Error>(val error: E) :
        Result<Nothing, E>
}

inline fun <T, E : Error, R> Result<T, E>.map(map: (T) -> R): Result<R, E> {
    return when (this) {
        is Result.Success -> Result.Success(map(data))
        is Result.Error -> Result.Error(error)
    }
}

inline fun <T, E : Error> Result<T, E>.onSuccess(action: (T) -> Unit): Result<T, E> {
    return when (this) {
        is Result.Success -> {
            action(data)
            this
        }

        is Result.Error -> this
    }
}

inline fun <T, E : Error> Result<T, E>.onError(action: (E) -> Unit): Result<T, E> {
    return when (this) {
        is Result.Success -> this
        is Result.Error -> {
            action(error)
            this
        }
    }
}

typealias EmptyResult<E> = Result<Unit, E>